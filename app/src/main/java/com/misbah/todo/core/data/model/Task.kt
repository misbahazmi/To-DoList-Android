package com.misbah.todo.core.data.model


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nytimes.utils.AppEnums
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
@Entity(tableName = "task_table")
@Parcelize
data class Task(
    val title: String,
    val name: String,
    val category: Int = AppEnums.TasksCategory.All.value,
    val important: Int = AppEnums.TasksPriority.Normal.value,
    val completed: Boolean = false,
    val due: Long = System.currentTimeMillis(),
    val created: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
    val dueDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(due)
    val displayPriority: String
        get() = when(important){
            AppEnums.TasksPriority.Normal.value->
                AppEnums.TasksPriority.Normal.name
            AppEnums.TasksPriority.Low.value->
                AppEnums.TasksPriority.Low.name
            AppEnums.TasksPriority.Medium.value->
                AppEnums.TasksPriority.Medium.name
            AppEnums.TasksPriority.High.value->
                AppEnums.TasksPriority.High.name
            else->
                AppEnums.TasksPriority.Normal.name

        }
}