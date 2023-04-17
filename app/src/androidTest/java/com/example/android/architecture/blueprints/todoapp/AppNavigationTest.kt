package com.example.android.architecture.blueprints.todoapp

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.DrawerMatchers.isOpen
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.taskdetail.TaskDetailFragmentDirections
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.tasks.TasksFragmentDirections
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.lang.Thread.sleep

@RunWith(AndroidJUnit4::class)
@LargeTest
class AppNavigationTest {

    private lateinit var tasksRepository: TasksRepository
    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setupRepository() {
        tasksRepository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking {
            tasksRepository.deleteAllTasks()
        }
    }

    @After
    fun cleanupRepository() {
        ServiceLocator.resetRepository()
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun tasksScreen_clickOnDrawerIcon_opensDrawerNavigation() = runBlocking {
        // GIVEN two tasks, one is active and the other is completed
        tasksRepository.saveTask(Task("Title 1", "Description 1"))
        tasksRepository.saveTask(Task("Title 2", "Description 2", true))

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // WHEN click on navigation button
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(
            click()
        )

        // THEN drawer navigation is open
        onView(withId(R.id.drawer_layout)).check(matches(isOpen()))

        activityScenario.close()
    }

    @Test
    fun tasksScreenAndTaskDetailScreen_clickOnUpButton() = runBlocking {
        // GIVEN a task
        val task = Task("Up Button", "navigate back to TasksScreen")
        tasksRepository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task
        onView(withId(R.id.title_text)).perform(click())
        // Click on edit FAB
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Click on Up button
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(
            click()
        )
        // Verify navigate back to TaskDetailFragment
        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        // Click on Up button
        onView(withContentDescription(activityScenario.getToolbarNavigationContentDescription())).perform(
            click()
        )
        // Verify navigate back to TasksFragment
        onView(withId(R.id.title_text)).check(matches(isDisplayed()))


        activityScenario.close()
    }

    @Test
    fun tasksScreenAndTaskDetailScreen_clickOnBackButton() = runBlocking {
        // GIVEN a task
        val task = Task("Back Button", "navigate back to TasksScreen")
        tasksRepository.saveTask(task)

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        // Click on the task
        onView(withId(R.id.title_text)).perform(click())
        // Click on edit FAB
        onView(withId(R.id.edit_task_fab)).perform(click())

        // Click on back button
        pressBack()
        // Verify navigate back to TaskDetailFragment
        onView(withId(R.id.task_detail_title_text)).check(matches(isDisplayed()))
        // Click on Up button
        pressBack()
        // Verify navigate back to TasksFragment
        onView(withId(R.id.title_text)).check(matches(isDisplayed()))


        activityScenario.close()
    }
}


fun <T : Activity> ActivityScenario<T>.getToolbarNavigationContentDescription(): String {
    lateinit var description: String
    onActivity {
        description = it.findViewById<Toolbar>(R.id.toolbar).navigationContentDescription as String
    }
    return description
}
