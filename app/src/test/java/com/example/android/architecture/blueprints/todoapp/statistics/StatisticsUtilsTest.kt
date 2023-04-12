package com.example.android.architecture.blueprints.todoapp.statistics

import com.example.android.architecture.blueprints.todoapp.data.Task
import org.junit.Assert.*
import org.junit.Test

class StatisticsUtilsTest {

    @Test
    fun getActiveAndCompletedStats_oneCompleted_returnsZeroHundred() {
        val tasks = listOf(
            Task("title", "description", true)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.activeTasksPercent)
        assertEquals(100f, result.completedTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_twoCompletedAndThreeActive_returnsFortySixty() {
        val tasks = listOf(
            Task("title", "description", true),
            Task("title", "description", true),
            Task("title", "description", false),
            Task("title", "description", false),
            Task("title", "description", false)
        )
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(40f, result.completedTasksPercent)
        assertEquals(60f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_empty_returnsZero() {
        val tasks = listOf<Task>()
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }

    @Test
    fun getActiveAndCompletedStats_null_returnsZero() {
        val tasks = null
        val result = getActiveAndCompletedStats(tasks)
        assertEquals(0f, result.completedTasksPercent)
        assertEquals(0f, result.activeTasksPercent)
    }
}