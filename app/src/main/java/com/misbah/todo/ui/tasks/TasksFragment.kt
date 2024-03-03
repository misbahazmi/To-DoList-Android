package com.misbah.todo.ui.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.collection.arraySetOf
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.misbah.chips.ChipListener
import com.misbah.todo.R
import com.misbah.todo.core.base.BaseFragment
import com.misbah.todo.core.data.model.Category
import com.misbah.todo.core.data.model.Task
import com.misbah.todo.core.data.model.ToDo
import com.misbah.todo.core.data.storage.SortOrder
import com.misbah.todo.core.data.storage.TaskData
import com.misbah.todo.databinding.FragmentTasksBinding
import com.misbah.todo.ui.adapters.TasksAdapter
import com.misbah.todo.ui.listeners.OnItemClickListener
import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.utils.exhaustive
import com.misbah.todo.ui.utils.onQueryTextChanged
import com.nytimes.utils.AppEnums
import com.nytimes.utils.AppLog
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 27-Feb-2024
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class TasksFragment :  BaseFragment<TasksViewModel>(), OnItemClickListener {
    private var _binding: FragmentTasksBinding? = null
    internal lateinit var viewModel: TasksViewModel
    private lateinit var searchView: SearchView

    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val binding get() = _binding!!
    override fun getViewModel(): TasksViewModel {
        viewModel = ViewModelProvider(viewModelStore, factory)[TasksViewModel::class.java]
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTasksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val taskAdapter = TasksAdapter(this)
        binding.apply {
            val catList = arrayListOf<Category>()
            for ((index,data)  in AppEnums.TasksCategory.values().distinct().withIndex()){
                catList.add(Category(data.value, data.name))
            }
            for (data in catList){
                chipTasksCategory.addChip(data.name)
            }
            chipTasksCategory.updateHorizontalSpacing()
            chipTasksCategory.setChipListener( object : ChipListener {
                override fun chipSelected(index: Int) {
                    val catId =  catList[index].id
                    viewModel.onFilterCategoryClick(catId)
                }
                override fun chipDeselected(index: Int) {}
                override fun chipRemoved(index: Int) {}
              }
            )
            recyclerViewTasks.apply {
                adapter = taskAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val task = taskAdapter.currentList[viewHolder.adapterPosition]
                    viewModel.onTaskSwiped(task)
                }
            }).attachToRecyclerView(recyclerViewTasks)
        }

        setFragmentResultListener("add_edit_request") { _, bundle ->
            val result = bundle.getInt("add_edit_result")
            viewModel.onAddEditResult(result)
        }

        viewModel.tasks.observe(viewLifecycleOwner) {
            taskAdapter.submitList(it)
        }

        viewModel.remoteTasks.observe(viewLifecycleOwner) {
            AppLog.debugD("REMOTE LIST: ${it.data?.todos?.size}")
            if (it.data != null) {
                for (todo in it.data.todos) {
                    viewModel.localTasks.value.let {
                        viewModel.saveRemoteTask(todo)
                    }
                }
            }
        }

        viewModel.localTasks.observe(viewLifecycleOwner) {
            AppLog.debugD("LOCAL LIST FIRST: ${it}")
            //viewModel.deleteTask(20, it)
            //AppLog.debugD("LOCAL LIST LAST: ${it}")
        }

        viewModel.localTaskByCat(AppEnums.TasksCategory.General.value).observe(viewLifecycleOwner) {
            AppLog.debugD("LOCAL LIST General: ${it}")
            //viewModel.deleteTask(20, it)
            //AppLog.debugD("LOCAL LIST LAST: ${it}")
        }


        viewModel.remainingTasks?.observe(viewLifecycleOwner){
            AppLog.debugD("SIZE: ${it.size}")
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.tasksEvent.collect { event ->
                    when (event) {
                        is TasksViewModel.TasksEvent.ShowUndoDeleteTaskMessage -> {
                            Snackbar.make(requireView(), "Task deleted", Snackbar.LENGTH_LONG)
                                .setAction("UNDO") {
                                    viewModel.onUndoDeleteClick(event.task)
                                }.show()
                        }
                        is TasksViewModel.TasksEvent.ShowTaskSavedConfirmationMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                        }
                        is TasksViewModel.TasksEvent.NavigateToAddTaskScreen -> {
                            val action =
                                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                                    "New Task",
                                    null
                                )
                            findNavController().navigate(action)

                        }
                        is TasksViewModel.TasksEvent.NavigateToEditTaskScreen -> {
                            val action =
                                TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                                    "Edit Tasks",
                                    event.task
                                )
                            findNavController().navigate(action)
                        }
                        is TasksViewModel.TasksEvent.NavigateToDeleteAllCompletedScreen -> {
                            val action =
                                TasksFragmentDirections.actionGlobalDeleteAllCompletedDialogFragment()
                            findNavController().navigate(action)
                        }
                        else ->{}
                    }.exhaustive
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            if(viewModel.preferencesFlow.first().category != 0)
                viewModel.preferencesFlow.first().category.let { binding.chipTasksCategory.setSelectedChip(it) }
            else
                binding.chipTasksCategory.setSelectedChip(0)
        }
        setHasOptionsMenu(true)
        (requireActivity() as MainActivity).showFAB()
        viewModel.getTasksRemainingTask()
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        val pendingQuery = viewModel.searchQuery.value
        if (!pendingQuery.isNullOrEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed_tasks).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_due_date -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_sort_by_priority -> {
                viewModel.onSortOrderSelected(SortOrder.BY_PRIORITY)
                true
            }
            R.id.action_hide_completed_tasks -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            R.id.action_delete_all_completed_tasks -> {
                viewModel.onDeleteAllCompletedClick()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        searchView.setOnQueryTextListener(null)
    }

    override fun onItemClick(task: ToDo) {
        viewModel.onTaskSelected(task)
    }

    override fun onItemDeleteClick(task: ToDo) {
        viewModel.onTaskSwiped(task)
    }

    override fun onItemEditClick(task: ToDo) {
        val action =
            TasksFragmentDirections.actionTasksFragmentToAddEditTaskFragment(
                "Edit Tasks",
                task
            )
        findNavController().navigate(action)
    }

    override fun onCheckBoxClick(task: ToDo, isChecked: Boolean) {
        viewModel.onTaskCheckedChanged(task, isChecked)
    }
}