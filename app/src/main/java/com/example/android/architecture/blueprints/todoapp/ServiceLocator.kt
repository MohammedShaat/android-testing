package com.example.android.architecture.blueprints.todoapp

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Room
import com.example.android.architecture.blueprints.todoapp.data.source.DefaultTasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.TasksDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.TasksRepository
import com.example.android.architecture.blueprints.todoapp.data.source.local.TasksLocalDataSource
import com.example.android.architecture.blueprints.todoapp.data.source.local.ToDoDatabase
import com.example.android.architecture.blueprints.todoapp.data.source.remote.TasksRemoteDataSource
import kotlinx.coroutines.runBlocking

object ServiceLocator {
    var tasksRepository: TasksRepository? = null
        @VisibleForTesting set
    private var database: ToDoDatabase? = null

    fun provideTasksRepository(context: Context): TasksRepository {
        synchronized(this) {
            return tasksRepository ?: createTasksRepository(context)
        }
    }

    private fun createTasksRepository(context: Context): TasksRepository {
        tasksRepository = DefaultTasksRepository(
            TasksRemoteDataSource,
            createTasksLocalDataSource(

                context
            )
        )
        return tasksRepository!!
    }

    private fun createTasksLocalDataSource(context: Context): TasksDataSource {
        val database = database ?: createDatabase(context)
        return TasksLocalDataSource(database.taskDao())
    }

    private fun createDatabase(context: Context): ToDoDatabase {
        database = Room.databaseBuilder(
            context.applicationContext,
            ToDoDatabase::class.java,
            "Tasks.db"
        ).build()
        return database!!
    }

    @VisibleForTesting
    fun resetRepository() {
        synchronized(this) {
            runBlocking {
                TasksRemoteDataSource.deleteAllTasks()
            }

            database?.apply {
                clearAllTables()
                close()
            }

            tasksRepository = null
            database = null
        }
    }
}