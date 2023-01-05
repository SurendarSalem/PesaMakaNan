package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.repository.AttendanceRepository

class AttendanceListViewModel : ViewModel() {
    val error = MutableLiveData<String>()
    private val attendanceRepository = AttendanceRepository()
    val attendanceListResult = attendanceRepository.attendanceListResult

    fun getAllAttendance() {
        attendanceRepository.getAllAttendance()
    }
}