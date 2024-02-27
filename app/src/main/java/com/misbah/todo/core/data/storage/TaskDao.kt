package com.misbah.todo.core.data.storage

import androidx.room.*
import com.misbah.todo.core.data.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    fun getTasks(query: String, sortOrder: SortOrder, hideCompleted: Boolean, category: Int): Flow<List<Task>> {
        return if (category != 0)
            when (sortOrder) {
                SortOrder.BY_DATE -> getTasksSortedByDateCreated(query, hideCompleted, category)
                SortOrder.BY_NAME -> getTasksSortedByName(query, hideCompleted, category)
                SortOrder.BY_PRIORITY -> getTasksSortedByPriority(query, hideCompleted, category)
            }
        else
            when (sortOrder) {
                SortOrder.BY_DATE -> getTasksSortedByDateCreatedAllCategory(query, hideCompleted)
                SortOrder.BY_NAME -> getTasksSortedByNameAllCategory(query, hideCompleted)
                SortOrder.BY_PRIORITY -> getTasksSortedByPriorityAllCategory(query, hideCompleted)
            }
    }

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND category = :categoryId AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTasksSortedByName(searchQuery: String, hideCompleted: Boolean,  categoryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND category = :categoryId AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, important")
    fun getTasksSortedByPriority(searchQuery: String, hideCompleted: Boolean,  categoryId: Int): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND category = :categoryId AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, due")
    fun getTasksSortedByDateCreated(searchQuery: String, hideCompleted: Boolean,  categoryId: Int): Flow<List<Task>>

    //All Category
    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, name")
    fun getTasksSortedByNameAllCategory(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, important")
    fun getTasksSortedByPriorityAllCategory(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0) AND name LIKE '%' || :searchQuery || '%' ORDER BY important DESC, due")
    fun getTasksSortedByDateCreatedAllCategory(searchQuery: String, hideCompleted: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM task_table WHERE (completed != :hideCompleted OR completed = 0)  AND due > :current AND due < :future   ORDER BY important DESC, due")
    fun getTasksRemainingTask(hideCompleted: Boolean, current : Long, future : Long): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("DELETE FROM task_table WHERE completed = 1")
    fun deleteCompletedTasks()
}