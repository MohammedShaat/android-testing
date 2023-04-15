package com.example.android.architecture.blueprints.todoapp.taskdetail

import android.os.SystemClock.sleep
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.architecture.blueprints.todoapp.R
import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class TaskDetailFragmentTest {

    @Test
    fun taskDetailFragment_activeTask_displaysTaskInUi() {
        // GIVEN an active task
        val task = Task("title1", "description1", false)

        // WHEN TaskDetailFragment launched to display the task
        val bundle = TaskDetailFragmentArgs(task.id).toBundle()
        launchFragmentInContainer<TaskDetailFragment>(bundle, R.style.AppTheme)

        sleep(5000)
        // THEN
    }
}