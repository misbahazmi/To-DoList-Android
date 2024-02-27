package com.misbah.todo.core.data.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.di.module.ApplicationScope
import com.nytimes.utils.AppEnums
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Provider

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    class Callback @Inject constructor(
        private val database: Provider<TaskDatabase>,
        @ApplicationScope private val applicationScope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val dao = database.get().taskDao()

            applicationScope.launch {
                dao.insert(Task("Wash the dishes","Wash the all the dishes"))
                dao.insert(Task("Do the laundry", "Do the laundry and iron"))
                dao.insert(Task("Buy groceries","Buy the groceries and shopping",4, important = AppEnums.TasksPriority.High.value))
                dao.insert(Task("Groceries","Grocery shopping: buy milk, eggs, and vegetables.",4, important = AppEnums.TasksPriority.Medium.value))
                dao.insert(Task("Prepare food", "Prepare food", important =  AppEnums.TasksPriority.Medium.value))
                dao.insert(Task("Call mom", "Calling mom", category = 2, important = AppEnums.TasksPriority.High.value))
                dao.insert(Task("Visit grandma","Visiting native place to meet grandma",  completed = true))
                dao.insert(Task("Repair my bike","Repair and servicing of Bike"))
                dao.insert(Task("Call Elon Musk","Calling Elon Musk", 1))
                dao.insert(Task("Learning","Start learning a new language", 3, important = AppEnums.TasksPriority.Medium.value))
                dao.insert(Task("New Course","Complete a certification course in a new skill", 3, important = AppEnums.TasksPriority.Low.value))
                dao.insert(Task("New Hobby","Explore a new hobby, like painting or playing a musical instrument.", 3))
                dao.insert(Task("Bill payment","Pay the electricity bill online.", 2,  important = AppEnums.TasksPriority.High.value))
                dao.insert(Task("Report & Meeting","Complete the report for the quarterly meeting.", 1,important = AppEnums.TasksPriority.High.value))
                dao.insert(Task("Project Status","Send out the project status update email to the team.", 1,important = AppEnums.TasksPriority.Medium.value))
            }
        }
    }
}