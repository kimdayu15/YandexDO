package com.gems.yandexdo.data

import android.content.Context
import com.gems.yandexdo.MySharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString

open class TodoItemsRepository(context: Context) {
    private val prefs = MySharedPreferences.getInstance(context)

    companion object {
        private const val TASKS_KEY = "tasks_key"
    }

    private val _tasksFlow = MutableStateFlow(getTasks())
    val tasksFlow: Flow<List<TodoItem>> get() = _tasksFlow.asStateFlow()

    fun saveTasks(tasks: List<TodoItem>) {
        val jsonTasks = tasks.map { Json.encodeToString(it) }.toSet()
        prefs.edit().putStringSet(TASKS_KEY, jsonTasks).apply()
    }

    open fun getTasks(): List<TodoItem> {
        val jsonTasks = prefs.getStringSet(TASKS_KEY, emptySet()).orEmpty()
        return jsonTasks.map { Json.decodeFromString<TodoItem>(it) }
    }

    fun getTaskById(taskId: String): TodoItem? {
        return getTasks().find { it.id == taskId }
    }

    fun addTask(task: TodoItem) {
        val tasks = getTasks().toMutableList()
        tasks.add(task)
        saveTasks(tasks)
    }

    fun updateTask(updatedTask: TodoItem) {
        val tasks = getTasks().map {
            if (it.id == updatedTask.id) updatedTask else it
        }
        saveTasks(tasks)
    }

    fun deleteTask(taskId: String) {
        val tasks = getTasks().filterNot { it.id == taskId }
        saveTasks(tasks)
    }
}
