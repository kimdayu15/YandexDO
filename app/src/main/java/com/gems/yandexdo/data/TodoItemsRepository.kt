package com.gems.yandexdo.data

import android.content.Context
import com.gems.yandexdo.MySharedPreferences
import kotlinx.serialization.json.Json

open class TodoItemsRepository(context: Context) {
    private val prefs = MySharedPreferences.getInstance(context)

    companion object {
        private const val TASKS_KEY = "tasks_key"
    }

    fun saveTasks(tasks: List<TodoItem>) {
        val jsonTasks = tasks.map { Json.encodeToString(TodoItem.serializer(), it) }.toSet()
        prefs.edit().putStringSet(TASKS_KEY, jsonTasks).apply()
    }

    open fun getTasks(): List<TodoItem> {
        val jsonTasks = prefs.getStringSet(TASKS_KEY, emptySet()).orEmpty()
        return jsonTasks.map { Json.decodeFromString(TodoItem.serializer(), it) }
    }

    fun addTask(task: TodoItem) {
        val tasks = getTasks().toMutableList()
        tasks.add(task)
        saveTasks(tasks)
    }

    fun deleteTask(taskId: String) {
        val tasks = getTasks().filterNot { it.id == taskId }
        saveTasks(tasks)
    }

    fun updateTask(updatedTask: TodoItem) {
        val tasks = getTasks().map {
            if (it.id == updatedTask.id) updatedTask else it
        }
        saveTasks(tasks)
    }
}
