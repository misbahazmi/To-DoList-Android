package com.misbah.todo.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.misbah.todo.core.base.BaseFragment
import com.misbah.todo.core.data.model.Category
import com.misbah.todo.databinding.FragmentCategoryBinding
import com.misbah.todo.ui.adapters.CategoryAdapter
import com.misbah.todo.ui.listeners.OnCategoryClickListener
import com.misbah.todo.ui.main.MainActivity
import com.misbah.todo.ui.tasks.TasksFragmentDirections
import com.misbah.todo.ui.tasks.TasksViewModel
import com.misbah.todo.ui.utils.exhaustive
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
class CategoryFragment : BaseFragment<CategoryViewModel>(), OnCategoryClickListener {

    private var _binding: FragmentCategoryBinding? = null
    internal lateinit var viewModel: CategoryViewModel
    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private val binding get() = _binding!!
    override fun getViewModel(): CategoryViewModel {
        viewModel = ViewModelProvider(viewModelStore, factory)[CategoryViewModel::class.java]
        return viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val categoryAdapter =  CategoryAdapter(this@CategoryFragment)
        binding.apply {
            recyclerViewCategory.apply {
                adapter =categoryAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
        }
        categoryAdapter.submitList(viewModel.categoryList().value)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.categoryEvent.collect { event ->
                    when (event) {
                        is CategoryViewModel.CategoryEvent.NavigateToAddCategoryDialog -> {
                            val action = CategoryFragmentDirections.actionAddCategoryDialogFragment()
                            findNavController().navigate(action)
                        }
                        is CategoryViewModel.CategoryEvent.ShowManageCategoryMessage->{
                            Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                        }
                        else ->{}
                    }.exhaustive
                }
            }
        }
        (requireActivity() as MainActivity).showFAB()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(task: Category) {}

    override fun onItemDeleteClick(task: Category) {
        viewModel.displayMessage("To Be Implemented")
    }

    override fun onItemEditClick(task: Category) {
        viewModel.displayMessage("To Be Implemented")
    }

    override fun onCheckBoxClick(task: Category, isChecked: Boolean) {}
}