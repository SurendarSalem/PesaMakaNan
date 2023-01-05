package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentAddPurchaseBinding
import com.asmirestoran.pesomakanan.model.*
import com.asmirestoran.pesomakanan.viewmodel.AddPurchaseViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [PurchaseFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PurchaseFragment : BaseFragment() {
    lateinit var binding: FragmentAddPurchaseBinding
    var error: String = ""
    private val addPurchaseViewModel: AddPurchaseViewModel by viewModels()

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_purchase, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addPurchaseViewModel = addPurchaseViewModel
        initViews()
        addPurchaseViewModel.addPurchaseResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), "Purchase added")
                    requireActivity().onBackPressed()
                }
                Status.ERROR -> {
                    showProgress(false)
                }
            }
        }
    }

    private fun initViews() {
        binding.rbPaymentType.setOnCheckedChangeListener { _, id ->
            when (id) {
                R.id.rb_payment_type_net -> {
                    onNetPaymentSelected()
                }
                R.id.rb_payment_type_custom -> {
                    onCustomPaymentSelected()
                }
                R.id.rb_payment_type_emi -> {
                    onEMIPaymentSelected()
                }
            }
        }
        addTextChangeListeners()
        binding.btnAdd.setOnClickListener {
            addPurchaseViewModel.purchaseLiveData.value?.let {
                val error = Purchase.valid(it)
                binding.error = error
                if (error.isEmpty()) {
                    addPurchaseViewModel.addPurchase()
                }
            }

        }
        binding.ivOrderDatePick.setOnClickListener {
            Utils.showDatePicker(requireContext(), binding.etOrderDate)
        }
        binding.ivEmiStartPick.setOnClickListener {
            Utils.showDatePicker(requireContext(), binding.etEmiStartDate)
        }
        binding.ivEmiEndPick.setOnClickListener {
            Utils.showDatePicker(requireContext(), binding.etEmiEndDate)
        }
        binding.ivPendingDatePick.setOnClickListener {
            Utils.showDatePicker(requireContext(), binding.etPendingPaymentDate)
        }
        binding.btnReset.setOnClickListener {
            addPurchaseViewModel.purchaseLiveData.value = Purchase()
            binding.invalidateAll()
        }
    }

    private fun addTextChangeListeners() {
        binding.etAmount.addTextChangedListener(amountChangeListener)
        binding.etTaxPercentage.addTextChangedListener(amountChangeListener)
        binding.tvTotalAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let {
                    if (it.paymentMethod != null) {
                        binding.tvPaidAmount.text = binding.tvTotalAmount.text
                    }
                }
            }
        })
        binding.tvPaidAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let {
                    if (it.paymentMethod != null) {
                        binding.etBalanceAmount.setText((it.totalAmount - it.paidAmount).toString())
                    }
                }
            }

        })
        binding.etEmiDuration.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let { purchase ->
                    if (purchase.paymentMethod == PaymentMethod.EMI) {
                        purchase.emiPayment?.let {
                            val emiAmount = if (it.emiDuration > 0) {
                                purchase.totalAmount / it.emiDuration
                            } else {
                                0f
                            }
                            binding.etEmiAmount.setText(emiAmount.toString())
                        }
                    }
                }
            }
        })
        binding.etEmiAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let { purchase ->
                    if (purchase.paymentMethod == PaymentMethod.EMI) {
                        purchase.emiPayment?.let {
                            purchase.paidAmount = it.emiAmount * it.emiDuration
                            binding.tvPaidAmount.setText(purchase.paidAmount.toString())
                        }
                    }
                }
            }
        })
        binding.tvPaidAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let {
                    if (it.paymentMethod != null) {
                        binding.etBalanceAmount.setText((it.totalAmount - it.paidAmount).toString())
                    }
                }
            }

        })
        binding.etBalanceAmount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                addPurchaseViewModel.purchaseLiveData.value?.let {
                    if (it.paymentMethod == PaymentMethod.CUSTOM) {
                        if (it.paidAmount < it.totalAmount) {
                            binding.rlPendingPayment.visibility = View.VISIBLE
                        } else {
                            binding.rlPendingPayment.visibility = View.GONE
                        }
                    }
                }
            }

        })
    }

    private fun onEMIPaymentSelected() {
        binding.llEmiPayment.visibility = View.VISIBLE
        binding.llFinalPayment.visibility = View.VISIBLE
        binding.tilBalanceAmount.visibility = View.VISIBLE
        addPurchaseViewModel.purchaseLiveData.value?.let {
            it.paymentMethod = PaymentMethod.EMI
            it.emiPayment = EmiPayment()
            it.customPayment = null
            it.netPayment = null
            it.emiPayment?.let { emiPayment ->
                emiPayment.emiDuration = 1
                emiPayment.emiAmount = it.totalAmount.div(emiPayment.emiDuration)
                binding.etEmiAmount.setText(it.totalAmount.toString())
                binding.etEmiDuration.setText(emiPayment.emiDuration.toString())
                binding.tvPaidAmount.setText(it.totalAmount.toString())
            }
        }
        binding.tvPaidAmount.isFocusable = false
    }

    private fun onCustomPaymentSelected() {
        binding.llEmiPayment.visibility = View.GONE
        binding.llFinalPayment.visibility = View.VISIBLE
        binding.tilBalanceAmount.visibility = View.VISIBLE
        addPurchaseViewModel.purchaseLiveData.value?.let {
            it.paymentMethod = PaymentMethod.CUSTOM
            it.customPayment = CustomPayment()
            it.netPayment = null
            it.emiPayment = null
        }
        binding.tvPaidAmount.isFocusableInTouchMode = true
        binding.tvPaidAmount.isFocusable = true
        binding.tvPaidAmount.text = binding.tvTotalAmount.text
    }

    private fun onNetPaymentSelected() {
        binding.llEmiPayment.visibility = View.GONE
        binding.llFinalPayment.visibility = View.VISIBLE
        binding.tilBalanceAmount.visibility = View.GONE
        addPurchaseViewModel.purchaseLiveData.value?.let {
            it.paymentMethod = PaymentMethod.NET
            it.netPayment = NetPayment()
            it.customPayment = null
            it.emiPayment = null
        }
        binding.tvPaidAmount.isFocusable = false
        binding.tvPaidAmount.text = binding.tvTotalAmount.text
    }

    private val amountChangeListener = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            addPurchaseViewModel.purchaseLiveData.value?.let {
                if (it.taxPercentage > 0) {
                    it.taxAmount = it.amount * (it.taxPercentage / 100)
                } else {
                    it.taxAmount = 0f
                }
                binding.etTaxAmount.setText(it.taxAmount.toString())
                it.totalAmount = it.amount + it.taxAmount
                binding.tvTotalAmount.setText(it.totalAmount.toString())

                if (it.paymentMethod == PaymentMethod.EMI) {
                    it.emiPayment?.let { emiPayment ->
                        emiPayment.emiAmount = it.totalAmount.div(emiPayment.emiDuration)
                        binding.etEmiAmount.setText(emiPayment.emiAmount.toString())
                    }
                }
            }
        }
    }
}