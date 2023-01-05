package com.asmirestoran.pesomakanan.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asmirestoran.pesomakanan.databinding.LayoutAttendanceItemBinding
import com.asmirestoran.pesomakanan.model.Attendance

class AttendanceListAdapter : RecyclerView.Adapter<AttendanceListAdapter.AttendanceHolder>() {

    var attendances: List<Attendance> = mutableListOf()
        get() = field
        set(value) {
            field = value
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttendanceHolder {
        return AttendanceHolder(
            LayoutAttendanceItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AttendanceHolder, position: Int) {
        holder.bind(attendances[position])
    }

    override fun getItemCount() = attendances.size
    fun updateData(attendances: List<Attendance>) {
        this.attendances = attendances
        notifyItemRangeChanged(0, attendances.size - 1)
    }

    class AttendanceHolder(val binding: LayoutAttendanceItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(attendance: Attendance) {
            binding.attendance = attendance
        }
    }
}