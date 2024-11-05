package com.gems.yandexdo.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    @SerialName("id")
    val id: String = "",
    @SerialName("text")
    val text: String = "",
    @SerialName("importance")
    val importance: Int = 0,
    @SerialName("deadline")
    val deadline: Long? = null,
    @SerialName("is_completed")
    val isCompleted: Boolean = false,
    @SerialName("created_at")
    val createdAt: Long = 0L,
    @SerialName("modified_at")
    val modifiedAt: Long = 0L
)

