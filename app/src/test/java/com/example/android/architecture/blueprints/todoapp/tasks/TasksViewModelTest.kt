package com.example.android.architecture.blueprints.todoapp.tasks

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.android.architecture.blueprints.todoapp.Event
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.hamcrest.CoreMatchers.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private lateinit var repository: FakeTasksRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        repository = FakeTasksRepository()
        val task1 = Task("title1", "description1")
        val task2 = Task("title2", "description2")
        val task3 = Task("title3", "description3")
        repository.addTasks(task1, task2, task3)

        viewModel = TasksViewModel(repository)
    }

    @Test
    fun addNewTask_setsNewTaskEvent() {
        // WHEN call addNewTask
        viewModel.addNewTask()
        val value = viewModel.newTaskEvent.getOrAwaitValue()

        //THEN the new task event is triggered
        assertThat(value.getContentIfNotHandled(), not(nullValue()))
    }

    @Test
    fun setFiltering_showAll_makesAddTasksButtonVisible() {
        // WHEN call setFiltering() with ALL_TASKS argument
        viewModel.setFiltering(TasksFilterType.ALL_TASKS)
        val tasksAddViewVisible = viewModel.tasksAddViewVisible.getOrAwaitValue()

        // THEN tasksAddViewVisible is true
        assertThat(tasksAddViewVisible, `is`(true))
    }

    @Test
    fun completeTask_dataAndSnackbarUpdated() {
        // GIVEN an active task
        val task = Task("title", "description", false)
        repository.addTasks(task)

        // WHEN complete the task
        viewModel.completeTask(task, true)

        // THEN the task and snackbar are updated
        assertThat(repository.tasksServiceData[task.id]?.isCompleted, `is`(true))
        val snackbar = viewModel.snackbarText.getOrAwaitValue()
        assertThat(snackbar.getContentIfNotHandled(), `is`(R.string.task_marked_complete))
    }
}