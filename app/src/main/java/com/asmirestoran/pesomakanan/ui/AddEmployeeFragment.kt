package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentAddEmployeeBinding
import com.asmirestoran.pesomakanan.model.Employee
import com.asmirestoran.pesomakanan.viewmodel.AddEmployeeViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AddEmployeeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmployeeFragment : BaseFragment() {
    lateinit var binding: FragmentAddEmployeeBinding
    var error: String = ""
    private val addEmployeeViewModel: AddEmployeeViewModel by viewModels()

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_employee, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addEmployeeViewModel = addEmployeeViewModel
        initViews()
        addEmployeeViewModel.addEmployeeResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), "Employee registered")
                    requireActivity().onBackPressed()
                }
                Status.ERROR -> {
                    showProgress(false)
                }
            }
        }
    }

    private fun initViews() {
        binding.btnRegister.setOnClickListener {
            addEmployeeViewModel.employeeLiveData.value?.let {
                val error = Employee.valid(it)
                binding.error = error
                if (error.isEmpty()) {
                    addEmployeeViewModel.addEmployee()
                }
            }
        }
        binding.ivInTimePick.setOnClickListener {
            Utils.showTimePicker(requireContext(), binding.etInTime)
        }
        binding.ivOutTimePick.setOnClickListener {
            Utils.showTimePicker(requireContext(), binding.etOutTime)
        }
        binding.btnReset.setOnClickListener {
            addEmployeeViewModel.reset()
            binding.invalidateAll()
        }
    }
}