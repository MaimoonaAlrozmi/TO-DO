package com.maimoona.to_do

import androidx.lifecycle.ViewModel

class TaskListViewModel : ViewModel() {
/*
    val tasks = mutableListOf<Task>()

    init {
        for (i in 0 until 100) {
            val task = Task()
            task.no=i
            task.title= "task -$i"
            task.details="details of every task it will be here- $i"
            tasks += task
        }
    }*/

    private val taskRepository = TaskRepository.get()
    val  taskListLiveData = taskRepository.getTasks()

    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }
}