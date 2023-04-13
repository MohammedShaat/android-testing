package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // GIVEN a new TasksViewModel instance
        val viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // WHEN call addNewTask
        viewModel.addNewTask()

        //THEN the new task event is triggered
    }
}