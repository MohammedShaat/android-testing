package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        viewModel = TasksViewModel(ApplicationProvider.getApplicationContext())
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // WHEN call addNewTask
        viewModel.addNewTask()
        val value = viewModel.newTaskEvent.getOrAwaitValue()

        //THEN the new task event is triggered
        assertThat(value?.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFiltering_showAll_makesAddTasksButtonVisible() {
        // WHEN call setFiltering() with ALL_TASKS argument
        viewModel.setFiltering(TasksFilterType.ALL_TASKS)
        val tasksAddViewVisible = viewModel.tasksAddViewVisible.getOrAwaitValue()

        // THEN tasksAddViewVisible is true
        assertThat(tasksAddViewVisible, `is`(true))
    }
}