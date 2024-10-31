package com.xlcx.floralskin.core.handle

import android.view.View
import com.xlcx.floralskin.core.isFalseStrict
import com.xlcx.floralskin.core.isTrue

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */
abstract class HandleOfDayNight(view: View) :
    HandleOfDayNightBase(view) {
    private var dayResource2: Int? = null
    private var nightResource2: Int? = null

    override fun detect() {
      if (isDay.isTrue() && day == null && dayResource2 == null) {
            return
        } else if (isDay.isFalseStrict() && night == null && nightResource2 == null) {
            return
        }
        handleResource()
    }

    fun getResourceInt2(): Int? {
        return when {
            isDay.isTrue() -> {
                dayResource2
            }
            isDay.isFalseStrict() -> {
                nightResource2
            }
            else -> null
        }
    }
}
