package com.asmirestoran.pesomakanan.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.asmirestoran.pesomakanan.model.Attendance
import com.asmirestoran.pesomakanan.repository.AttendanceRepository
import com.asmirestoran.pesomakanan.ui.Utils

class AddAttendanceViewModel : ViewModel() {
    val attendanceLiveData = MutableLiveData<Attendance>()

    val error = MutableLiveData<String>()
    val attendanceRepository = AttendanceRepository()
    val addAttendanceResult = attendanceRepository.addAttendanceResult

    init {
        attendanceLiveData.value = Attendance().apply {
            id = Attendance.createId()
            date = Utils.getDate()
        }
    }

    fun addAttendance() {
        attendanceLiveData.value?.let {
            attendanceRepository.addAttendance(it)
        }
    }

    fun reset() {
        attendanceLiveData.value = Attendance()
    }
}