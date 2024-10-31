package com.xlcx.floralskin.core

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */



/**
 * 可空Boolean直接判断
 * @receiver Boolean?
 * @return Boolean
 */
fun Boolean?.isTrue() = this == true

fun Boolean?.isFalseStrict() = this == false




