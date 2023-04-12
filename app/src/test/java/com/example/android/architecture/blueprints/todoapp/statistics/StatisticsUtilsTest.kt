package com.example.android.architecture.blueprints.todoapp.statistics


import com.example.android.architecture.blueprints.todoapp.data.Task
import org.hamcrest.CoreMatchers.`is`
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_oneCompleted_returnsZeroHundred() {
        // GIVEN a list of Task, with one completed Task
        val tasks = listOf(
            Task("title", "description", true)
        )

        // WHEN call getActiveAndCompleteStats()
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 0% active tasks and 100% completed tasks
        assertThat(result.activeTasksPercent, `is`(0f))
        assertThat(result.completedTasksPercent, `is`(100f))
    }

    @Test
    fun getActiveAndCompletedStats_twoCompletedAndThreeActive_returnsFortySixty() {
        // GIVEN a list of Task, with two completed Task and three active Task
        val tasks = listOf(
            Task("title", "description", true),
            Task("title", "description", true),
            Task("title", "description", false),
            Task("title", "description", false),
            Task("title", "description", false)
        )

        // WHEN call getActiveAndCompleteStats()
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 40% completed tasks and 60% active tasks
        assertThat(result.completedTasksPercent, `is`(40f))
        assertThat(result.activeTasksPercent, `is`(60f))
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero() {
        // GIVEN an empty list of Task
        val tasks = listOf<Task>()

        // WHEN call getActiveAndCompletedStats()
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 0% completed tasks and 0% active tasks
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }

    @Test
    fun getActiveAndCompletedStats_null_returnsZero() {
        // GIVEN a null
        val tasks = null

        // WHEN call getActiveAndCompletedStats()
        val result = getActiveAndCompletedStats(tasks)

        // THEN there are 0% completed tasks and 0% active tasks
        assertThat(result.completedTasksPercent, `is`(0f))
        assertThat(result.activeTasksPercent, `is`(0f))
    }
}