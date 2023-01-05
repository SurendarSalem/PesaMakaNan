package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.databinding.FragmentAttendanceListBinding
import com.asmirestoran.pesomakanan.ui.adapter.AttendanceListAdapter
import com.asmirestoran.pesomakanan.viewmodel.AttendanceListViewModel

class AttendanceListFragment : BaseFragment() {
    private lateinit var binding: FragmentAttendanceListBinding
    private val attendanceListViewModel: AttendanceListViewModel by viewModels()
    private lateinit var attendanceListAdapter: AttendanceListAdapter

    override fun getProgressBar(): View {
        return binding.pb.root
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_attendance_list, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observe()
    }

    private fun initViews() {
        binding.rvAttendance.layoutManager = LinearLayoutManager(requireContext())
        attendanceListAdapter = AttendanceListAdapter()
        binding.rvAttendance.adapter = attendanceListAdapter
        binding.btnAddAttendance.setOnClickListener {
            (requireActivity() as OrderActivity).openAddAttendanceScreen()
        }
    }

    private fun observe() {
        attendanceListViewModel.getAllAttendance()
        attendanceListViewModel.attendanceListResult.observe(viewLifecycleOwner) { resource ->
            when (resource.status) {
                Status.LOADING -> {
                    showProgress(true)
                }
                Status.SUCCESS -> {
                    showProgress(false)
                    resource.data?.let { categories ->
                        attendanceListAdapter.updateData(categories)
                    }
                }
                Status.ERROR -> {
                    showProgress(false)
                    Utils.showSnack(requireActivity(), resource.message)
                }
            }
        }
    }
}