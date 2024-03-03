package com.misbah.todo.ui.adapters

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misbah.todo.R
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.databinding.ItemTaskBinding
import com.misbah.todo.ui.listeners.OnItemClickListener
import com.misbah.todo.ui.utils.Utils
import com.nytimes.utils.AppEnums
import java.security.AccessController.getContext
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class TasksAdapter(private val listener: OnItemClickListener) :
    ListAdapter<ToDo, TasksAdapter.TasksViewHolder>(DiffCallback()) {
    @Inject
    lateinit var utils: Utils
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TasksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class TasksViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onItemClick(task)
                    }
                }
                checkBoxCompleted.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        listener.onCheckBoxClick(task, checkBoxCompleted.isChecked)
                    }
                }
                labelOption.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        openOptionMenu(root, task, listener)
                    }
                }
            }
        }

        fun bind(task: ToDo) {
            binding.apply {
                checkBoxCompleted.isChecked = task.completed
                textViewName.text = task.name
                textViewTitle.text = task.title
                textViewDateDue.text = task.dueDateFormatted
                textPriority.text = task.displayPriority
                textPriority.isVisible = task.priorityValue != 0
                textViewName.paint.isStrikeThruText = task.completed
                textViewTitle.paint.isStrikeThruText = task.completed
                textViewDateDue.paint.isStrikeThruText = task.completed
                when(task.priorityValue){
                    AppEnums.TasksPriority.High.value->{
                        textPriority.background = ContextCompat.getDrawable(root.context, R.drawable.bg_hight_priority)
                        textPriority.setTextColor( ContextCompat.getColor(root.context, R.color.text_color_high))
                    }

                    AppEnums.TasksPriority.Medium.value->{
                        textPriority.background = ContextCompat.getDrawable(root.context, R.drawable.bg_medium_priority)
                        textPriority.setTextColor( ContextCompat.getColor(root.context, R.color.text_color_medium))
                    }

                    AppEnums.TasksPriority.Low.value->{
                        textPriority.background = ContextCompat.getDrawable(root.context, R.drawable.bg_low_priority)
                        textPriority.setTextColor( ContextCompat.getColor(root.context, R.color.text_color_low))
                    }
                }
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ToDo>() {
        override fun areItemsTheSame(oldItem: ToDo, newItem: ToDo) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ToDo, newItem: ToDo) =
            oldItem == newItem
    }

    @SuppressLint("RestrictedApi")
    fun openOptionMenu(v: View, task: ToDo, listener : OnItemClickListener) {
        val popup = PopupMenu(v.context, v)
        popup.menuInflater.inflate(R.menu.navigation_menu_task_items, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when(item.itemId){
                R.id.nav_delete->{
                    listener.onItemDeleteClick(task)
                }
                R.id.nav_edit->{
                    listener.onItemEditClick(task)
                }
            }
            true
        }
        val menuHelper = MenuPopupHelper(v.context, (popup.menu as MenuBuilder), v)
        menuHelper.setForceShowIcon(true)
        menuHelper.gravity = Gravity.END
        menuHelper.show()
    }
}