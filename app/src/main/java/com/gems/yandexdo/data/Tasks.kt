package com.gems.yandexdo.data

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val id: String,
    val text: String,
    val importance: Int,
    val deadline: Long,
    val isCompleted: Boolean,
    val createdAt: Long,
    val modifiedAt: Long
) {
}
