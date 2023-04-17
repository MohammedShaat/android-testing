package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.data.Result
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksLocalDataSourceTest {

    private lateinit var database: ToDoDatabase
    private lateinit var localDataSource: TasksLocalDataSource

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDbAndLocalDataSource() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        localDataSource = TasksLocalDataSource(database.taskDao(), TestCoroutineDispatcher())
    }

    @After
    fun cleanupDb() {
        database.close()
    }

    @Test
    fun saveTask_THEN_getTask() = runBlockingTest {
        // GIVEN a task
        val task = Task("title", "description")

        // WHEN insert the task using saveTask(task), and then get it by id using getTask(task.id)
        localDataSource.saveTask(task)
        val loadedTask = localDataSource.getTask(task.id) as Result.Success

        // THEN loaded task is the same task
        assertThat(loadedTask.data, `is`(task))
    }
}