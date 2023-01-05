package com.asmirestoran.pesomakanan.model

class Employee {
    var employeeId: String = ""
    var name: String = ""
    var mobileNo: String = ""
    var description: String = ""
    var salary: Int = 0
    var salaryCutOff: Int = 0
    var attendanceInTime: String = ""
    var attendanceOutTime: String = ""
    var advanceAmount: Int = 0

    companion object {
        fun createId(): String {
            return "EID${System.currentTimeMillis()}"
        }

        fun valid(employee: Employee): String {
            if (employee.employeeId.isEmpty()) {
                return "Please reopen the screen to generate ID"
            }
            if (employee.name.isEmpty()) {
                return "Please enter name"
            }
            if (employee.mobileNo.isEmpty()) {
                return "Please enter mobile number"
            }
            if (employee.description.isEmpty()) {
                return "Please enter description"
            }
            if (employee.salary <= 0) {
                return "Please enter salary"
            }
            if (employee.attendanceInTime.isEmpty()) {
                return "Please enter in-time"
            }
            if (employee.attendanceOutTime.isEmpty()) {
                return "Please enter out-time"
            }
            return ""
        }
    }

}

data class Advance(val amount: Int = 0, val date: String = "")
