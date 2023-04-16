package com.example.android.architecture.blueprints.todoapp.statistics

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.android.architecture.blueprints.todoapp.MainCoroutineRule
import com.example.android.architecture.blueprints.todoapp.data.source.FakeTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class StatisticsViewModelTest {


    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: StatisticsViewModel
    private lateinit var repository: FakeTasksRepository

    @Before
    fun setupRepository() {
        repository = FakeTasksRepository()
        viewModel = StatisticsViewModel(repository)
    }

    @Test
    fun refresh_refreshesTasksAndShowIndicator() {
        val indicator = viewModel.dataLoading
        mainCoroutineRule.pauseDispatcher()
        viewModel.refresh()
        assertThat(indicator.getOrAwaitValue(), `is`(true))
        mainCoroutineRule.resumeDispatcher()
        assertThat(indicator.getOrAwaitValue(), `is`(false))
    }

}