package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.not
import org.hamcrest.CoreMatchers.nullValue
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // GIVEN a new TasksViewModel instance
        val viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())

        // WHEN call addNewTask
        viewModel.addNewTask()
        val value = viewModel.newTaskEvent.getOrAwaitValue()

        //THEN the new task event is triggered
        assertThat(value?.getContentIfNotHandled(), not(nullValue()))
    }
}