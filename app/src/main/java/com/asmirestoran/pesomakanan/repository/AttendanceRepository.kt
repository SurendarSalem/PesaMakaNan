package com.asmirestoran.pesomakanan.repository

import androidx.lifecycle.MutableLiveData
import com.asmirestoran.pesomakanan.AttendanceListener
import com.asmirestoran.pesomakanan.CategoryListener
import com.asmirestoran.pesomakanan.FirebaseHelper
import com.asmirestoran.pesomakanan.Resource
import com.asmirestoran.pesomakanan.model.Attendance
import com.asmirestoran.pesomakanan.model.Category

class AttendanceRepository {
    val addAttendanceResult: MutableLiveData<Resource<Attendance>> =
        MutableLiveData<Resource<Attendance>>()

    val attendanceListResult: MutableLiveData<Resource<List<Attendance>>> =
        MutableLiveData<Resource<List<Attendance>>>()

    var attendances: List<Attendance>? = null

    fun addAttendance(item: Attendance) {
        addAttendanceResult.value = Resource.loading(null)
        FirebaseHelper.addAttendance(
            item, {
                addAttendanceResult.value = Resource.success(item)
            }, { _ex ->
                addAttendanceResult.value = Resource.error(_ex.message, null)
            })
    }

    fun getAllAttendance(fresh: Boolean = false) {
        attendanceListResult.value = Resource.loading(null)
        if (fresh || attendances.isNullOrEmpty()) {
            FirebaseHelper.getAllAttendances(object : AttendanceListener {
                override fun onLoad(attendances: List<Attendance>) {
                    attendanceListResult.value = Resource.success(attendances)
                }

                override fun onError(error: String) {
                    attendanceListResult.value = Resource.error(error, null)
                }
            })
        } else {
            attendanceListResult.value = Resource.success(attendances)
        }
    }
}