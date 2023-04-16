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
        // WHEN call refresh(), with pend coroutines
        val indicator = viewModel.dataLoading
        mainCoroutineRule.pauseDispatcher()
        viewModel.refresh()

        // THEN indicator is shown
        assertThat(indicator.getOrAwaitValue(), `is`(true))

        // WHEN execute pending coroutines
        mainCoroutineRule.resumeDispatcher()

        // THEN indicator is hidden
        assertThat(indicator.getOrAwaitValue(), `is`(false))
    }

    @Test
    fun loadStatisticsWhenTasksAreUnavailable_callErrorToDisplay() {
        // GIVEN error flag is true
        repository.shouldReturnError = true

        // WHEN call refresh()
        viewModel.refresh()

        // THEN error and empty are true
        assertThat(viewModel.error.getOrAwaitValue(), `is`(true))
        assertThat(viewModel.empty.getOrAwaitValue(), `is`(true))
    }
}