package com.xlcx.skin.demo

/**
 *     author : simon
 *     time   : 2024/08/26
 *     desc   :
 */

class Item(id: Int, title: String, description: String) {
    private val id: Int
    private val title: String
    private val description: String

    init {
        this.id = id
        this.title = title
        this.description = description
    }

    fun getTitle(): String {
        return title
    }

    fun getDescription(): String {
        return description
    }
}