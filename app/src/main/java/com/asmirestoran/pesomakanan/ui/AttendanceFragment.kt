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
import com.asmirestoran.pesomakanan.databinding.FragmentAddAttendanceBinding
import com.asmirestoran.pesomakanan.model.Attendance
import com.asmirestoran.pesomakanan.viewmodel.AddAttendanceViewModel
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [AttendanceFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AttendanceFragment : BaseFragment() {
    lateinit var binding: FragmentAddAttendanceBinding
    var error: String = ""
    private val addAttendanceViewModel: AddAttendanceViewModel by viewModels()

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_add_attendance, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.addAttendanceViewModel = addAttendanceViewModel
        initViews()
        addAttendanceViewModel.addAttendanceResult.observe(viewLifecycleOwner) { resource ->
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
            addAttendanceViewModel.attendanceLiveData.value?.let {
                val error = Attendance.valid(it)
                binding.error = error
                if (error.isEmpty()) {
                    addAttendanceViewModel.addAttendance()
                }
            }
        }
        binding.ivDatePick.setOnClickListener {
            Utils.showTimePicker(requireContext(), binding.etDate)
        }
        binding.ivInTimePick.setOnClickListener {
            Utils.showTimePicker(requireContext(), binding.etInTime)
        }
        binding.ivOutTimePick.setOnClickListener {
            Utils.showTimePicker(requireContext(), binding.etOutTime)
        }
        binding.btnReset.setOnClickListener {
            addAttendanceViewModel.reset()
            binding.invalidateAll()
        }
    }
}