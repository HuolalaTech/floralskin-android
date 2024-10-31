package com.xlcx.floralskin.core

import android.content.Context
import android.content.pm.PackageManager
import android.content.res.AssetManager
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.util.ArrayMap
import android.util.Log
import androidx.annotation.*
import androidx.core.content.res.ResourcesCompat
import com.xlcx.floralskin.core.utils.SkinMd5Util
import com.xlcx.floralskin.core.utils.ResUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.lang.ref.SoftReference
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
private const val STATUS_NONE = 0
private const val STATUS_ENABLE = 1
private const val STATUS_DISABLE = -1


private const val DRAWABLE = "drawable"
private const val COLOR = "color"
private const val DIMEN = "dimen"
private const val MIPMAP = "mipmap"

internal typealias LoadListener = SkinLoadListener.() -> Unit

internal fun skinLog(tag: String, msg: String) {
    if (!skinLogFlag) {
        return
    }
    Log.d(tag,msg)
}

internal var skinLogFlag = true

object SkinManager {
    // 应用内的资源
    private lateinit var resources:Resources

    @Volatile
    private var enable = STATUS_ENABLE

    // 当前有效的正在使用的皮肤
    @Volatile
    private var mCurrentSkin: XLCurrentSkin? = null

    // 是否正在初始化
    @Volatile
    private var isInitializing = false

    // 是否正常加载皮肤包
    @Volatile
    private var isLoading = false

    // 是否反射生成skinResource 失败
    private var isReflectFail = true

    // 存储类型-name-值
    private var skinIds: ArrayMap<String, SoftReference<ArrayMap<String, Int>>> = ArrayMap(5)

    // 假如init的时候有使用到了皮肤包，那么外部其他的load方法需要等待init方法执行完成之后才能执行
    private var cdt: CountDownLatch? = null

    // 是否是一次无用的皮肤加载
    private var uselessLoad = false

    private var mSkinChangeListeners = mutableSetOf<ISkinChangeListener>()
    lateinit var appContext: Context
    private  var filePath: String = ""
    private  var packName: String = ""

    fun init(context: Context, listener: LoadListener, enableLog: Boolean = true) {
        appContext = context
        resources = appContext.resources
        skinLogFlag = enableLog
        if (isEnable()) {
            skinLog("init", "skin enable")
            loadSkin(listener, filePath, packName)
        } else {
            skinLog("init", "skin disable")
        }
    }

    @Synchronized
    fun attach(observer: ISkinChangeListener) {
        mSkinChangeListeners.add(observer)
    }

    @Synchronized
    fun detach(observer: ISkinChangeListener) {
        mSkinChangeListeners.remove(observer)
    }


    /** 去除皮肤，还原主题 */
    fun resetTheme() {
        skinIds.clear()
        if (isInitializing || isLoading) {
            skinLog("resetTheme", "isInitializing=$isInitializing,isLoading=$isLoading,waiting for loadSkin end")
            uselessLoad = true
        } else if (mCurrentSkin != null) {
            skinLog("resetTheme", "clear current show skin")
            mCurrentSkin = null
            uselessLoad = false
            notifySkinChange()
        } else {
            skinLog("resetTheme", "no skin showing")
        }
    }

    /**
     * 在load skin之前需要调用该方法
     */
    fun enableSkin() {
        enable = STATUS_ENABLE
    }

    /**
     * 在调用resetTheme之前调用该方法
     */
    fun disableSkin() {
        enable = STATUS_DISABLE
    }


    fun getDrawable(@DrawableRes id: Int): Drawable {
        if (!isEnable()) {
            return ResUtil.getDrawable(id)
        }
        val current = mCurrentSkin ?: return ResUtil.getDrawable(id)
        val resId = getResId(id, DRAWABLE)
        if (resId == 0) {
            return ResUtil.getDrawable(id)
        }
        return runCatching { ResourcesCompat.getDrawable(current.resources, resId, null) }
            .getOrNull()
            ?: ResUtil.getDrawable(id)
    }



    fun getMipmap(id: Int): Drawable {
        if (!isEnable()) {
            return ResUtil.getDrawable(id)
        }
        val current = mCurrentSkin ?: return ResUtil.getDrawable(id)
        val resId = getResId(id, MIPMAP)
        if (resId == 0) {
            return ResUtil.getDrawable(id)
        }
        return runCatching { ResourcesCompat.getDrawable(current.resources, resId, null) }
            .getOrNull()
            ?: ResUtil.getDrawable(id)
    }

    fun getDimens(@DimenRes id: Int): Float {
        if (!isEnable()) {
            return resources.getDimension(id)
        }
        val current = mCurrentSkin ?: return resources.getDimension(id)
        val resId = getResId(id, DIMEN)
        if (resId == 0) {
            return resources.getDimension(id)
        }
        return runCatching { current.resources.getDimension(resId) }
            .getOrNull()
            ?: resources.getDimension(id)
    }

    fun getColor(@ColorRes id: Int): Int {
        if (!isEnable()) {
            return ResUtil.getColor(id)
        }
        val current = mCurrentSkin ?: return ResUtil.getColor(id)
        val resId = getResId(id, COLOR)
        if (resId == 0) {
            return ResUtil.getColor(id)
        }
        return runCatching { ResourcesCompat.getColor(current.resources, resId, null) }
            .getOrNull()
            ?: ResUtil.getColor(id)
    }


    fun loadSkin(pkgName: String, filePath: String, listener: LoadListener) {
        if (isLoading) {
            skinLog(
                "loadSkin",
                "on loading,can't execute new skin:pkgName=$pkgName,filePath=$filePath"
            )
            return
        }
        isLoading = true
        GlobalScope.launch(Dispatchers.IO)  {
            runCatching {
                val count = cdt?.count ?: 0
                skinLog("loadSkin", "init cdt=$count")
                cdt?.await(2, TimeUnit.SECONDS)
            }
            val builder = SkinLoadListener().also(listener)
            val skin = XlSkin.makeSkin(filePath,pkgName)
            builder.mLoadStart?.invoke()
            loadSkin(false, skin, builder)
        }
    }

    private fun loadSkin(listener: LoadListener,filePath: String,packageName:String) {
        if (isInitializing) {
            return
        }
        isInitializing = true

        GlobalScope.launch(Dispatchers.IO) {
            val builder = SkinLoadListener().also(listener)
            builder.mLoadStart?.invoke()
            val skin = XlSkin.makeSkin(filePath, packageName)
            if (!skin.isWorkSkin()) {
                builder.mLoadError?.invoke("local cache skin not work,${skin}")
                skinLog("loadSkin", "local cache skin not work,${skin}")
                isInitializing = false
                return@launch
            }
            cdt = CountDownLatch(1)
            Log.e("loadSkin","start build")
            loadSkin(true, skin, builder)
        }

    }

    /**
     * 加载皮肤 在子线程运行
     * @param isInt 是否正在初始化
     * @param skin
     * @param listener
     */
    @WorkerThread
    private fun loadSkin(isInt: Boolean, skin: XlSkin, listener: SkinLoadListener) {
        skinLog("loadSkin", "isInt=$isInt, skin=${skin}")
        val start = SystemClock.elapsedRealtime()

        val pkgName = parsePackageNameFromFile(appContext, skin)
        val target =
            if (pkgName.isNotEmpty() && pkgName != skin.pkgName) {
                skinLog("loadSkin", "reset package ,old=${skin.pkgName},new=$pkgName")
                XlSkin.makeSkin(pkgName, skin.filePath)
            } else {
                skinLog("loadSkin","first skin")
                    skin
            }
        if (!target.isWorkSkin()) {
            listener.mLoadError?.invoke("skin not work,${skin}")
            skinLog("loadSkin", "skin not work,${skin}")
            loadEnd(isInt)
            return
        }
        if (isSameSkin(target.pkgName ?: "", target.filePath ?: "")) {
            listener.mLoadSuccess?.invoke()
            skinLog("loadSkin", "same Skin,${skin}")
            loadEnd(isInt)
            return
        }
        skinIds.clear()

        val skinResource = reflectAsset(target)
        if (skinResource == null) {
            loadEnd(isInt)
            skinLog("loadSkin", "skin Resource create fail")
            listener.mLoadError?.invoke("skin Resource create fail")
            // 不需要调用notifySkinChange
            return
        }
        val original = mCurrentSkin
        mCurrentSkin = XLCurrentSkin(skinResource, target)
        skinLog("loadSkin", "load success")
        val flag = uselessLoad
        loadEnd(isInt)
        listener.mLoadSuccess?.invoke()
        if (flag) {
            skinLog("loadSkin", "uselessLoad=true")
            // 在加载皮肤过程中，重置了主题
            uselessLoad = false
            mCurrentSkin = null
            if (original != null) {
                // 当前是有皮肤的才需要刷新
                skinLog("loadSkin", "current skin =$original,reset the theme")
                notifySkinChange()
            }
            return
        }
        notifySkinChange()
        skinLog("loadSkin", "done,time = ${SystemClock.elapsedRealtime() - start}")
    }

    /**
     * 反射把资源包路径添加到AssetManager
     * 然后构建出Resources对象
     * @param skin
     * @return
     */
    private fun reflectAsset(skin: XlSkin): Resources? {
        val manager = runCatching {
            val assetManager = AssetManager::class.java.newInstance()
            val addAssetPath =
                assetManager.javaClass.getMethod("addAssetPath", String::class.java)
            addAssetPath.invoke(assetManager, skin.filePath)
            assetManager
        }.onFailure {
            isReflectFail = true
            skinLog("loadSkin", "reflect addAssetPath fail，message=${Log.getStackTraceString(it)}")
        }.getOrNull() ?: return null

        return runCatching {
            Resources(manager, resources.displayMetrics, resources.configuration)
        }.onFailure {
            skinLog("loadSkin", "new Resources fail，message=${Log.getStackTraceString(it)}")
        }.getOrNull()
    }

    /**
     * 加载完成
     * @param isInt
     */
    private fun loadEnd(isInt: Boolean) {
        if (isInt) {
            isInitializing = false
            cdt?.countDown()
        } else {
            isLoading = false
        }
        uselessLoad = false
    }

    /**
     * 获取资源包名
     * @param context
     * @param skin
     * @return
     */
    private fun parsePackageNameFromFile(context: Context, skin: XlSkin): String {
        try {
            val mPm: PackageManager = context.packageManager
            val mInfo =
                mPm.getPackageArchiveInfo(skin.filePath!!, PackageManager.GET_INSTRUMENTATION)
            // 如果能够从资源当中读取到就使用资源的
            if (mInfo != null) {
                return mInfo.packageName ?: ""
            }
        } catch (e: Exception) {
            skinLog("parsePackageNameFromFile", "exception:" + e.message)
        }
        return ""
    }

    /**
     * 是否是相同的皮肤，是的话就不需要重新换肤了
     * @param pkgName
     * @param filePath
     * @return
     */
    private fun isSameSkin(pkgName: String, filePath: String): Boolean {
        val now = mCurrentSkin?.skin ?: return false
        if (filePath.isNotEmpty() && now.filePath == filePath) {
            // 文件路径一样，则认为是同一个文件，就不需要重复加载了
            skinLog("isSameSkin", "same path")
            return true
        }
        val skin = XlSkin.makeSkin(pkgName, filePath)
        val targetPkgName = parsePackageNameFromFile(appContext, skin)
        if (targetPkgName.isNotEmpty() && targetPkgName != pkgName) {
            if (now.pkgName != targetPkgName) {
                return false
            }
        } else if (now.pkgName != pkgName) {
            return false
        }
        // 文件名称不相同，但是可能文件是一样的
        val targetMd5 = SkinMd5Util.getFileMD5(File(filePath))
        val nowMd5 = SkinMd5Util.getFileMD5(File(now.filePath ?: ""))
        skinLog("isSameSkin", "targetMd5=$targetMd5,nowMd5=$nowMd5")
        return targetMd5 == nowMd5
    }

    /**
     * 当前皮肤是否可被启用
     * @return
     */
    private fun isEnable(): Boolean {
        return enable == STATUS_ENABLE
    }



    @Throws(Resources.NotFoundException::class)
    private fun getResourceEntryName(resId: Int): String {
        return resources.getResourceEntryName(resId)
    }




    /**
     * 获取插件资源id
     * @param id
     * @param type
     * @return
     */
    private fun getResId(id: Int, type: String): Int {
        val typesCache = skinIds[type]?.get()
        val current = mCurrentSkin
        if (current == null) {
            skinLog("getResId", "current is null id=$id,type=$type")
            return 0
        }
        val resName = runCatching { getResourceEntryName(id) }.getOrNull()
        if (resName.isNullOrEmpty()) {
            skinLog("getResId", "resName is empty,type=$type,id=$id")
            return 0
        }
        val cache = typesCache?.get(resName)
        if (cache != null) {
            skinLog("getResId", "from cache:resName=$resName,type=$type,cache=$cache,id=$id")
            return cache
        }
        runCatching {
            val pName = current.skin.pkgName
            skinLog("getResId", "impl:resName=$resName,type=$type,pkgName=$pName,id=$id")
            val resId = current.resources.getIdentifier(resName, type, current.skin.pkgName)
            if (resId != 0) {
                saveIds2Memory(typesCache, resName, resId, type)
                skinLog(
                    "getResId", "getIdentifier success:$resName,type=$type,resId=$resId,id=$id"
                )
                return resId
            }
            skinLog("getResId", "getIdentifier fail $resName,type=$type,resId=$resId,id=$id")
        }.onFailure {
            skinLog(
                "getResId",
                "exception:${Log.getStackTraceString(it)},resName=$resName,type=$type,id=$id"
            )
            if (it is Resources.NotFoundException) {
                // 插件包不存在该资源，那么下次在读取的时候，就不需要继续调用getIdentifier而是直接使用宿主资源了
                saveIds2Memory(typesCache, resName, 0, type)
            }
        }
        return 0
    }

    /**
     * 更新内存缓存
     * @param typesCache
     * @param resName
     * @param resId
     * @param type
     */
    private fun saveIds2Memory(
        typesCache: ArrayMap<String, Int>?,
        resName: String,
        resId: Int,
        type: String
    ) {
        var now = typesCache
        if (now == null) {
            now = ArrayMap()
        }
        now[resName] = resId
        if (skinIds[type]?.get() == null) {
            skinIds[type] = SoftReference(now)
        }
    }

    /**
     * 广播换肤事件
     */
    private fun notifySkinChange() {
        if (mSkinChangeListeners.isEmpty()) {
            return
        }

        GlobalScope.launch (Dispatchers.Main) {
            for (item in mSkinChangeListeners) {
            item.onChange()
        } }
    }

}
