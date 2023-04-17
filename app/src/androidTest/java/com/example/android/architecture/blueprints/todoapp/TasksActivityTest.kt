package com.example.android.architecture.blueprints.todoapp

import android.provider.ContactsContract.Data
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.android.architecture.blueprints.todoapp.data.Task
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.tasks.TasksActivity
import com.example.android.architecture.blueprints.todoapp.util.EspressoIdlingResource
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class TasksActivityTest {

    private lateinit var repository: TasksRepository

    private var dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun setupRepository() {
        repository = ServiceLocator.provideTasksRepository(getApplicationContext())
        runBlocking {
            repository.deleteAllTasks()
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
    fun editTask() = runBlocking {
        // GIVEN a task
        repository.saveTask(Task("title1", "description1"))

        val activityScenario = ActivityScenario.launch(TasksActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)
        // WHEN edit the task
        // Click on the task
        onView(withText("title1")).perform(click())
        // Click on edit FAB
        onView(withId(R.id.edit_task_fab)).perform(click())
        // Update title
        onView(withId(R.id.add_task_title_edit_text)).perform(replaceText("new title"))
        // Update description
        onView(withId(R.id.add_task_description_edit_text)).perform(replaceText("new description"))
        // Click on save FAB
        onView(withId(R.id.save_task_fab)).perform(click())

        // THEN the task is updated with new title and description
        // Verify task's title is updated
        onView(withText("new title")).check(matches(isDisplayed()))

        activityScenario.close()
    }
}