package com.misbah.todo.ui.adapters

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.misbah.todo.R
import com.misbah.todo.core.data.model.Category
import com.misbah.todo.databinding.ItemCategoryBinding
import com.misbah.todo.ui.listeners.OnCategoryClickListener

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class CategoryAdapter(private val listener: OnCategoryClickListener) :
    ListAdapter<Category, CategoryAdapter.CategoryViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
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
                labelOption.setOnClickListener{
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val task = getItem(position)
                        openOptionMenu(root, task, listener)
                    }
                }
            }
        }

        fun bind(task: Category) {
            binding.apply {
                textViewTitle.text = task.name
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<Category>() {
        override fun areItemsTheSame(oldItem: Category, newItem: Category) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Category, newItem: Category) =
            oldItem == newItem
    }

    @SuppressLint("RestrictedApi")
    fun openOptionMenu(v: View, task: Category, listener : OnCategoryClickListener) {
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