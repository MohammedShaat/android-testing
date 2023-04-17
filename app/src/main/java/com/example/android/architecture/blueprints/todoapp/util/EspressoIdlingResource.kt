package com.example.android.architecture.blueprints.todoapp.util

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdlingResource {

    private const val RESOURCE = "GLOBAL"

    val countingIdlingResource = CountingIdlingResource(RESOURCE)

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow)
            countingIdlingResource.decrement()
    }
}

inline fun <T> wrapEspressoIdlingResource(block: () -> T): T {
    try {
        EspressoIdlingResource.increment()
        return block()
    } finally {
        EspressoIdlingResource.decrement()
    }
}