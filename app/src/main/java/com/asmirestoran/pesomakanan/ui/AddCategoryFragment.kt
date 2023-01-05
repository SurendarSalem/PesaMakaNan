package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.asmirestoran.pesomakanan.model.Category
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentAddCategoryBinding
import com.asmirestoran.pesomakanan.viewmodel.CategoryViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [AddCategoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddCategoryFragment : Fragment() {
    lateinit var binding: FragmentAddCategoryBinding
    private val categoryViewModel: CategoryViewModel by viewModels()
    var error: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_category, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.categoryViewModel = categoryViewModel
        categoryViewModel.addCategoryResult.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Added category ${it.data?.name}"
                    )
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), it.message)
                }
            }
        }
        binding.btnAddCategory.setOnClickListener {
            categoryViewModel.category.value?.let { category ->
                val error = Category.valid(category)
                binding.error = error
                if (error.isEmpty()) {
                    categoryViewModel.addCategory(category)
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        // (requireActivity() as OrderActivity).showProgress(show)
    }
}