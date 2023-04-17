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
    private lateinit var tasksDao: TasksDao

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupDB() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ToDoDatabase::class.java
        ).build()
        tasksDao = database.taskDao()
    }

    @After
    fun cleanupDB() {
        database.close()
    }

    @Test
    fun insertTask_getTaskById() = runBlockingTest {
        // Given a task
        val task = Task("title", "description")

        // WHEN insert the task by insert(task), and call getTaskById(task.id)
        tasksDao.insertTask(task)
        val loadedTask = tasksDao.getTaskById(task.id)

        // THEN the loaded task is the inserted one
        assertThat(loadedTask, `is`(task))
    }

    @Test
    fun insertTask_updateTask() = runBlockingTest {
        // GIVEN a task
        val task = Task("Active", "description", false)
        
        // WHEN insert the task, and then update its 'title' & `isCompleted` to "Completed" & true
        tasksDao.insertTask(task)
        task.title = "Completed"
        task.isCompleted = true
        tasksDao.updateTask(task)

        val loadedTask = tasksDao.getTaskById(task.id)
        // THEN loaded task is equal to the updated task
        assertThat(loadedTask?.id, `is`(task.id))
        assertThat(loadedTask?.title, `is`(task.title))
        assertThat(loadedTask?.description, `is`(task.description))
        assertThat(loadedTask?.isCompleted, `is`(task.isCompleted))
    }
}