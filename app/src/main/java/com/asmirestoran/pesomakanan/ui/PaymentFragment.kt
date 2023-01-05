package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Resource
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentPaymentBinding
import com.asmirestoran.pesomakanan.model.Employee
import com.asmirestoran.pesomakanan.model.Payment
import com.asmirestoran.pesomakanan.model.Type
import com.asmirestoran.pesomakanan.viewmodel.PaymentViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [PaymentFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PaymentFragment : BaseFragment() {
    lateinit var binding: FragmentPaymentBinding
    var error: String = ""
    private val paymentViewModel: PaymentViewModel by viewModels()

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_payment, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.paymentViewModel = paymentViewModel
        initViews()
        paymentViewModel.addPaymentResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    paymentViewModel.paymentLiveData.value?.let { payment ->
                        paymentViewModel.getEmployee()?.let { employee ->
                            if (payment.type == Type.ADVANCE) {
                                employee.advanceAmount += payment.advanceAmount
                                paymentViewModel.updateEmployee(employee)
                            } else {
                                if (employee.advanceAmount > 0) {
                                    employee.advanceAmount = 0
                                    paymentViewModel.updateEmployee(employee)
                                } else {
                                    Utils.showSnack(
                                        requireActivity(),
                                        "Paid to ${payment.employeeName} with payment ID ${payment.paymentId}}"
                                    )
                                }
                            }
                        }
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Payment failed!"
                    )
                }
            }
        }
        paymentViewModel.employeeUpdateResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    paymentViewModel.paymentLiveData.value?.let {
                        Utils.showSnack(
                            requireActivity(),
                            "Paid to ${it.employeeName} with payment ID ${it.paymentId}}"
                        )
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(
                        requireActivity(),
                        "Update user failed!"
                    )
                }
            }
        }
        paymentViewModel.employeeDetailResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    onEmployeeDetailsLoaded(resource)
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), resource.message)
                }
            }
        }
    }

    private fun onEmployeeDetailsLoaded(resource: Resource<Employee>) {
        binding.llDetails.visibility = View.VISIBLE
        paymentViewModel.paymentLiveData.value?.let {
            resource.data?.let { employee ->
                it.employeeName = employee.name
                it.salary = employee.salary
                binding.etSalaryCutOff.setText(employee.salaryCutOff.toString())
                binding.etAdvanceAmountTaken.setText(employee.advanceAmount.toString())
                if (employee.advanceAmount >= employee.salaryCutOff) {
                    binding.rbPaymentTypeSalary.isChecked = true
                    binding.rbPaymentTypeAdvance.isEnabled = false
                    binding.rbPaymentTypeAdvance.text = "No more advance"

                }
                binding.invalidateAll()
            }
        }
    }

    private fun initViews() {
        binding.btnRegister.setOnClickListener {
            paymentViewModel.paymentLiveData.value?.let {
                paymentViewModel.employeeDetailResult.value?.data?.let { employee ->
                    val error = Payment.valid(it, employee)
                    binding.error = error
                    if (error.isEmpty()) {
                        paymentViewModel.addPayment()
                    }
                }
            }
        }
        binding.ivSearch.setOnClickListener {
            paymentViewModel.paymentLiveData.value?.let {
                if (it.employeeId.isNotEmpty()) {
                    paymentViewModel.getEmployeeDetail(it.employeeId)
                }
            }
        }
        binding.ivDatePick.setOnClickListener {
            Utils.showDatePicker(requireContext(), binding.etDate)
        }
        binding.rgPaymentType.setOnCheckedChangeListener { _, id ->
            if (id == R.id.rb_payment_type_salary) {
                paymentViewModel.paymentLiveData.value?.let {
                    it.type = Type.SALARY
                    binding.tilAdvanceAmount.visibility = View.GONE
                }
            } else if (id == R.id.rb_payment_type_advance) {
                paymentViewModel.paymentLiveData.value?.let {
                    it.type = Type.ADVANCE
                    binding.tilAdvanceAmount.visibility = View.VISIBLE
                }
            }
        }
        binding.btnReset.setOnClickListener {
            paymentViewModel.reset()
            binding.invalidateAll()
            binding.llDetails.visibility = View.GONE
        }
    }
}