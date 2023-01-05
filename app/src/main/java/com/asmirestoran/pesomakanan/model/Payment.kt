package com.asmirestoran.pesomakanan.model

class Payment {
    var paymentId: String = ""
    var date: String = ""
    var month: String = ""
    var employeeId: String = ""
    var employeeName: String = ""
    var salary: Int = 0
    var salaryPaidDays: Int = 0
    var advanceAmount: Int = 0
    var type: Type? = null

    companion object {
        fun createId(): String {
            return "PY${System.currentTimeMillis()}"
        }

        fun valid(payment: Payment, employee: Employee): String {
            if (payment.paymentId.isEmpty()) {
                return "Please reopen the screen to generate ID"
            }
            if (payment.employeeId.isEmpty()) {
                return "Please enter employee ID"
            }
            if (payment.employeeName.isEmpty()) {
                return "Please enter employee name"
            }
            if (payment.date.isEmpty()) {
                return "Please enter date"
            }
            if (payment.type == null) {
                return "Please select payment type"
            }
            if (payment.type == Type.ADVANCE) {
                if (payment.advanceAmount <= 0) {
                    return "Please enter advance amount"
                } else if (payment.advanceAmount >= employee.salaryCutOff) {
                    return "Advance is greater than Cut-off!"
                }
            } else if (payment.type == Type.SALARY) {
                if (payment.salary <= 0) {
                    return "Please enter salary amount"
                }
            }
            if (payment.salaryPaidDays <= 0) {
                return "Please select salary paid days"
            }

            return ""
        }
    }
}

enum class Type(val type: String) {
    SALARY("Salary"),
    ADVANCE("Advance")
}