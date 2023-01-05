package com.asmirestoran.pesomakanan.model

class Attendance {
    var id: String = ""
    var date: String = ""
    var employeeId: String = ""
    var employeeName: String = ""
    var checkInTime: String = ""
    var checkOutTime: String = ""

    companion object {
        fun createId(): String {
            return "AT${System.currentTimeMillis()}"
        }

        fun valid(employee: Attendance): String {
            if (employee.id.isEmpty()) {
                return "Please reopen the screen to generate ID"
            }
            if (employee.date.isEmpty()) {
                return "Please enter date"
            }
            if (employee.employeeId.isEmpty()) {
                return "Please enter employee ID"
            }
            if (employee.employeeName.isEmpty()) {
                return "Please enter employee name"
            }
            if (employee.checkInTime.isEmpty()) {
                return "Please enter check-in time"
            }
            if (employee.checkOutTime.isEmpty()) {
                return "Please enter check-out time"
            }
            return ""
        }
    }
}