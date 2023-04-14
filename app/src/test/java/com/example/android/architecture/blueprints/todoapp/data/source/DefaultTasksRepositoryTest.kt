package com.example.android.architecture.blueprints.todoapp.data.source

import com.example.android.architecture.blueprints.todoapp.data.Task
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.core.IsEqual
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class DefaultTasksRepositoryTest {
    private lateinit var repository: DefaultTasksRepository
    private lateinit var tasksRemoteDataSource: FakeDataSource
    private lateinit var tasksLocalDataSource: FakeDataSource

    private val task1 = Task("title1", "description1")
    private val task2 = Task("title2", "description2")
    private val task3 = Task("title3", "description3")

    private val remoteTasks = listOf(task1, task2).sortedBy { it.id }
    private val localTasks = listOf(task3).sortedBy { it.id }
    private val newTasks = listOf(task3).sortedBy { it.id }

    @Before
    fun setupRepository() {
        tasksRemoteDataSource = FakeDataSource(remoteTasks.toMutableList())
        tasksLocalDataSource = FakeDataSource(localTasks.toMutableList())
        repository = DefaultTasksRepository(
            tasksRemoteDataSource,
            tasksLocalDataSource,
            Dispatchers.Unconfined
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getTasks_true_requestsAllTasksFromRemoteDataSource() = runBlockingTest {
        // WHEN call getTasks, with forceUpdate = true
        repository.getTasks(true)

        // THEN all of local tasks are equal to remote tasks
        assertThat(tasksLocalDataSource.getTasks(), IsEqual(tasksRemoteDataSource.getTasks()))
    }
}