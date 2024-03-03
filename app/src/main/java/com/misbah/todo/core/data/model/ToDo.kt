package com.misbah.todo.core.data.model

import android.annotation.SuppressLint
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.nytimes.utils.AppEnums
import com.nytimes.utils.AppLog
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Parcelize
data class ToDo(
    @SerializedName("id"        ) var id        : Int,
    @SerializedName("Title"     ) var title     : String,
    @SerializedName("CategoryValue"  ) var categoryId  : Int?,
    @SerializedName("Category"  ) var category  : String?,
    @SerializedName("todo"      ) var name      : String,
    @SerializedName("completed" ) var completed : Boolean,
    @SerializedName("userId"    ) var userId    : Int,
    @SerializedName("date"      ) var created      : String?,
    @SerializedName("dateValue"      ) var createdLong      : Long = Date().time,
    @SerializedName("due"      ) var due      : Long = Date().time,
    @SerializedName("priority"  ) var important  : String?,
    @SerializedName("priorityValue"  ) var priority  : Int?
) : Parcelable {

    val createdDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(created)
    val dueDateFormatted: String
        get() = DateFormat.getDateTimeInstance().format(due)

    val priorityValue : Int
        get() = when(important){
            AppEnums.TasksPriority.Normal.name->
                AppEnums.TasksPriority.Normal.value
            AppEnums.TasksPriority.Low.name->
                AppEnums.TasksPriority.Low.value
            AppEnums.TasksPriority.Medium.name->
                AppEnums.TasksPriority.Medium.value
            AppEnums.TasksPriority.High.name->
                AppEnums.TasksPriority.High.value
            else->
                AppEnums.TasksPriority.Normal.value
        }

    val displayPriority: String
        get() = "$important"

    val categoryValue : Int
        get() = when(category?.lowercase()){
            AppEnums.TasksCategory.General.name.lowercase()->
                AppEnums.TasksCategory.General.value
            AppEnums.TasksCategory.Work.name.lowercase()->
                AppEnums.TasksCategory.Work.value
            AppEnums.TasksCategory.Personal.name.lowercase()->
                AppEnums.TasksCategory.Personal.value
            AppEnums.TasksCategory.Cooking.name.lowercase()->
                AppEnums.TasksCategory.Cooking.value
            AppEnums.TasksCategory.Shopping.name.lowercase()->
                AppEnums.TasksCategory.Shopping.value
            AppEnums.TasksCategory.Fun.name.lowercase()->
                AppEnums.TasksCategory.Fun.value
            AppEnums.TasksCategory.Tour.name.lowercase()->
                AppEnums.TasksCategory.Tour.value
            AppEnums.TasksCategory.Games.name.lowercase()->
                AppEnums.TasksCategory.Games.value
            AppEnums.TasksCategory.Family.name.lowercase()->
                AppEnums.TasksCategory.Family.value
            else->
                AppEnums.TasksCategory.General.value
        }

    @SuppressLint("SimpleDateFormat")
    fun getDateValue() : Long {
        return createdLong
    }
}
