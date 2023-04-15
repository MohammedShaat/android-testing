package com.example.android.architecture.blueprints.todoapp.tasks

import android.annotation.SuppressLint
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.ServiceLocator
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.FakeAndroidTestRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@MediumTest
@ExperimentalCoroutinesApi
class TasksFragmentTest {
    private lateinit var repository: FakeAndroidTestRepository

    @Before
    fun setupRepository() {
        repository = FakeAndroidTestRepository()
        ServiceLocator.tasksRepository = repository
    }

    @After
    fun cleanupDB() {
        ServiceLocator.resetRepository()
    }

    @SuppressLint("CheckResult")
    @Test
    fun clickTask_navigatesToTaskDetailFragment() = runBlockingTest {
        // GIVEN two tasks, one is active and the other is completed
        val task1 = Task("task1", "description1", false)
        val task2 = Task("task2", "description2", true)
        repository.saveTask(task1)
        repository.saveTask(task2)

        val scenario = launchFragmentInContainer<TasksFragment>(themeResId = R.style.AppTheme)
        val mockNavController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, mockNavController)
        }
        // WHEN click on task1
        onView(withId(R.id.tasks_list)).perform(
            actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click())
        )

        // THEN navigate to TaskDetailFragment
        /**
         * verify that navigate() was called with 'task1' argument
         */
        verify(mockNavController).navigate(
            TasksFragmentDirections.actionTasksFragmentToTaskDetailFragment(task1.id)
        )
    }
}