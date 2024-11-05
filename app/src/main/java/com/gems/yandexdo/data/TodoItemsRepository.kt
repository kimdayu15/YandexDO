package com.gems.yandexdo.data

import android.content.Context
import android.content.SharedPreferences
import com.gems.yandexdo.MySharedPreferences
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json




class TodoItemsRepository(context: Context) {

    private val prefs = MySharedPreferences.getInstance(context)

    companion object {
        private const val TASKS_KEY = "tasks_key"
    }

    fun saveTasks(tasks: List<TodoItem>) {
        val jsonTasks = Json.encodeToString(tasks)
        prefs.edit().putString(TASKS_KEY, jsonTasks).apply()
    }

    fun getTasks(): List<TodoItem> {
        val jsonTasks = prefs.getString(TASKS_KEY, null)
        return if (jsonTasks != null) {
            Json.decodeFromString(jsonTasks)
        } else {
            emptyList()
        }
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
