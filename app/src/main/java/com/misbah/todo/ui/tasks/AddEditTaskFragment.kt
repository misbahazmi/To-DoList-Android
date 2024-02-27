package com.misbah.todo.ui.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.misbah.chips.ChipListener
import com.misbah.todo.R
import com.misbah.todo.core.base.BaseFragment
import com.misbah.todo.core.data.model.Category
import com.misbah.todo.databinding.FragmentAddEditTaskBinding
import com.misbah.todo.ui.adapters.CategoryArrayAdapter
import com.misbah.todo.ui.dialogs.TimePickerFragment
import com.misbah.todo.ui.listeners.OnDateTimeListener
import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.utils.exhaustive
import com.nytimes.utils.AppEnums
import kotlinx.coroutines.launch
import java.text.DateFormat
import javax.inject.Inject

/**
 * @author: Mohammad Misbah
 * @since: 03-Oct-2023
 * @sample: Technology Assessment for Sr. Android Role
 * Email Id: mohammadmisbahazmi@gmail.com
 * GitHub: https://github.com/misbahazmi
 * Expertise: Android||Java/Kotlin||Flutter
 */
class AddEditTaskFragment : BaseFragment<AddEditTaskViewModel>(), OnDateTimeListener {
    private val tasksArgs: AddEditTaskFragmentArgs by navArgs()
    private var _binding: FragmentAddEditTaskBinding? = null
    internal lateinit var viewModel: AddEditTaskViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory
    private val binding get() = _binding!!
    override fun getViewModel(): AddEditTaskViewModel {
        viewModel = ViewModelProvider(viewModelStore, factory)[AddEditTaskViewModel::class.java]
        return viewModel
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEditTaskBinding.inflate(inflater, container, false)
        viewModel.task.value = tasksArgs.task
        binding.farg = this@AddEditTaskFragment
        return binding.root
    }
    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {

            if(viewModel.task.value == null){
                btnSaveUpdate.text = getString(R.string.save)
                textHeading.text = getString(R.string.add_tasks)
                imgHeader.setImageResource(R.drawable.ic_add_task_data)
            }
            else {
                btnSaveUpdate.text = getString(R.string.update)
                textHeading.text = getString(R.string.update_tasks)
                imgHeader.setImageResource(R.drawable.ic_edit_task_data)
            }

            editTextTaskTitle.setText(viewModel.task.value?.title ?: "")
            editTextTaskDescription.setText(viewModel.task.value?.name ?: "")
            textViewDateDue.text = "Due Date: ${viewModel.task.value?.dueDateFormatted ?: DateFormat.getDateTimeInstance().format(viewModel.dueDate)}"
            viewModel.taskTitle = viewModel.task.value?.title ?: ""
            viewModel.taskDescription = viewModel.task.value?.name ?: ""
            viewModel.dueDate = viewModel.task.value?.due ?: System.currentTimeMillis()

            editTextTaskTitle.addTextChangedListener {
                viewModel.taskTitle = it.toString()
            }
            editTextTaskDescription.addTextChangedListener {
                viewModel.taskDescription = it.toString()
            }
            chipTasksPriority.setChipListener( object : ChipListener {
                override fun chipSelected(index: Int) {
                    viewModel.taskImportance =  AppEnums.TasksPriority.values().distinct()
                        .withIndex().first { it.value.value == index }.value.value
                }
                override fun chipDeselected(index: Int) {}
                override fun chipRemoved(index: Int) {}
              }
            )
            for ((index,data)  in AppEnums.TasksPriority.values().distinct().withIndex()){
               chipTasksPriority.addChip(data.name)
            }
            if(viewModel.task.value?.important != null && viewModel.task.value?.important != 0)
                viewModel.task.value?.important?.let { chipTasksPriority.setSelectedChip(it) }
            else
                chipTasksPriority.setSelectedChip(0)
            val catList = arrayListOf<Category>()
            for ((index,data)  in AppEnums.TasksCategory.values().distinct().withIndex()){
                catList.add(Category(data.value, data.name))
            }
            val adapter = CategoryArrayAdapter(requireContext(), R.layout.item_cateory_spinner, catList)
            spinnerCategory.adapter = adapter
            spinnerCategory.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                    viewModel.tasksCategory = catList[position].id
                }
                override fun onNothingSelected(parent: AdapterView<*>) {}
            }
            if(viewModel.task.value?.category != null  && viewModel.task.value?.category != 0)
                viewModel.task.value?.category?.let { spinnerCategory.setSelection(it) }
            else
                spinnerCategory.setSelection(0)
        }

        @Suppress("IMPLICIT_CAST_TO_ANY")
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addEditTaskEvent.collect { event ->
                    when (event) {
                        is AddEditTaskViewModel.AddEditTaskEvent.ShowInvalidInputMessage -> {
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_LONG).show()
                        }
                        is AddEditTaskViewModel.AddEditTaskEvent.NavigateBackWithResult -> {
                            binding.editTextTaskTitle.clearFocus()
                            binding.editTextTaskDescription.clearFocus()
                            setFragmentResult(
                                "add_edit_request",
                                bundleOf("add_edit_result" to event.result)
                            )
                            findNavController().popBackStack()
                        }
                        is AddEditTaskViewModel.AddEditTaskEvent.ShowDateTimePicker ->{
                            TimePickerFragment.newInstance(this@AddEditTaskFragment).show(childFragmentManager, "timePicker")
                        }
                        is AddEditTaskViewModel.AddEditTaskEvent.DateTimeWithResult ->{
                            viewModel.selectedDateTime = event.result
                            viewModel.dueDate = event.result
                            binding.textViewDateDue.text = "Due Date: ${DateFormat.getDateTimeInstance().format(viewModel.dueDate)}"
                        }
                        else -> {}
                    }.exhaustive
                }
            }
        }
        (requireActivity() as MainActivity).hideFAB()
    }

    fun clickOnSave() {
        viewModel.onSaveClick()
    }

    fun clickOnDateTime(){
       viewModel.showDatePicker()
    }

    override fun onDateTimeSelected(timestamp: Long) {
        viewModel.onDateTimeResult(timestamp)
    }
}