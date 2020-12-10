package com.maimoona.to_do

import android.app.Application

class ToDoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}