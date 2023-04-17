package com.example.android.architecture.blueprints.todoapp.data.source.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@SmallTest
@RunWith(AndroidJUnit4::class)
class TasksDaoTest {

    private lateinit var database: ToDoDatabase

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
    }

    @After
    fun cleanupDB() {
        database.close()
    }

    @Test
    fun insertTaskANDgetTaskById() = runBlockingTest {
        // Given a task
        val task = Task("title", "description")

        val dao = database.taskDao()
        // WHEN insert the task by insert(task), and call getTaskById(task.id)
        dao.insertTask(task)
        val loadedTask = dao.getTaskById(task.id)

        // THEN the loaded task is the inserted one
        assertThat(loadedTask, `is`(task))
    }
}