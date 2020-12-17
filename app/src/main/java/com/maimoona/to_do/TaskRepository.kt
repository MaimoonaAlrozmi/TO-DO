package com.maimoona.to_do

import Database.TaskDatabase
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "task-database"

class TaskRepository private constructor(context: Context){

    private val database: TaskDatabase =
        Room.databaseBuilder(context.applicationContext,
            TaskDatabase::class.java,
            DATABASE_NAME)
            .build()

    private val taskDao = database.taskDao()
    private val executor = Executors.newSingleThreadExecutor()

    fun getTasks():LiveData<List<Task>> = taskDao.getTasks()
    fun getTask(id: UUID):LiveData<Task?> = taskDao.getTask(id)
    fun getTask(tab: Int): LiveData<List<Task>> = taskDao.getTask(tab)


    fun addTask(task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }


    fun updateTask(task: Task) {
        executor.execute {
            taskDao.updateTask(task)
        }
    }

    fun removeTask(task: Task){
        executor.execute {
            taskDao.removeTask(task)
        }
    }

    companion object {
        private var INSTANCE: TaskRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE =
                    TaskRepository(context)
            }
        }

        fun get(): TaskRepository {
            return INSTANCE ?: throw IllegalStateException("TaskRepository must be initialized")
        }
    }
}