package com.asmirestoran.pesomakanan

import android.graphics.Bitmap
import com.asmirestoran.pesomakanan.model.*
import com.asmirestoran.pesomakanan.ui.FireBaseUploader
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*


object FirebaseHelper {
    private var firebaseDatabase: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()

    private const val USER_REFERENCE = "users"
    private const val CATEGORY_REFERENCE = "categories"
    private const val ITEM_REFERENCE = "item"
    private const val PURCHASE_REFERENCE = "purchases"
    private const val EMPLOYEE_REFERENCE = "employee"
    private const val ATTENDANCE_REFERENCE = "attendance"
    private const val PAYMENT_REFERENCE = "payments"
    private const val CART_REFERENCE = "carts"
    private const val ORDER_REFERENCE = "orders"

    fun addUser(
        user: User,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(USER_REFERENCE).child(user.firebaseId).setValue(user)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    private fun getDataBaseReference(path: String): DatabaseReference {
        return firebaseDatabase.getReference(path)
    }

    fun createUser(user: User, completeListener: OnCompleteListener<AuthResult>) {
        firebaseAuth.createUserWithEmailAndPassword(user.emailId.trim(), user.password.trim())
            .addOnCompleteListener(completeListener)
    }

    fun addCategory(
        category: Category,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(CATEGORY_REFERENCE).child(category.code).setValue(category)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun addItem(
        item: Item,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(ITEM_REFERENCE).child(item.code).setValue(item)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun login(user: User, onCompleteListener: OnCompleteListener<AuthResult>) {
        firebaseAuth.signInWithEmailAndPassword(user.emailId.trim(), user.password.trim())
            .addOnCompleteListener(onCompleteListener)
    }


    fun getUserDetail(user: User, userDetailsCallback: UserDetailsCallback) {
        val databaseReference = getDataBaseReference(USER_REFERENCE)
        databaseReference.child(user.firebaseId)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val userDetail = dataSnapshot.getValue(User::class.java)
                    userDetail?.let {
                        userDetailsCallback.onUserDetailsLoaded(it)
                    } ?: run {
                        userDetailsCallback.onUserDetailsFailed()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    userDetailsCallback.onUserDetailsFailed()
                }
            })
    }

    fun getAllCategories(categoryListener: CategoryListener) {
        getDataBaseReference(CATEGORY_REFERENCE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val categories = mutableListOf<Category>()
                for (postSnapshot in dataSnapshot.children) {
                    val student: Category? = postSnapshot.getValue(Category::class.java)
                    student?.let {
                        categories.add(it)
                    }
                }
                categoryListener.onLoad(categories)
            }

            override fun onCancelled(error: DatabaseError) {
                categoryListener.onError(error.message)
            }
        })
    }


    fun getAllAttendances(attendanceListener: AttendanceListener) {
        getDataBaseReference(ATTENDANCE_REFERENCE).addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val attendances = mutableListOf<Attendance>()
                for (postSnapshot in dataSnapshot.children) {
                    val attendance: Attendance? = postSnapshot.getValue(Attendance::class.java)
                    attendance?.let {
                        attendances.add(it)
                    }
                }
                attendanceListener.onLoad(attendances)
            }

            override fun onCancelled(error: DatabaseError) {
                attendanceListener.onError(error.message)
            }
        })
    }

    fun getAllPurchases(purchaseListListener: PurchaseListListener) {
        getDataBaseReference(PURCHASE_REFERENCE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val purchases = mutableListOf<Purchase>()
                for (postSnapshot in dataSnapshot.children) {
                    val purchase: Purchase? = postSnapshot.getValue(Purchase::class.java)
                    purchase?.let {
                        purchases.add(it)
                    }
                }
                purchaseListListener.onLoad(purchases)
            }

            override fun onCancelled(error: DatabaseError) {
                purchaseListListener.onError(error.message)
            }
        })
    }

    fun getOrders(orderListener: OrderListener) {
        getDataBaseReference(ORDER_REFERENCE).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val orders = mutableListOf<Order>()
                for (postSnapshot in dataSnapshot.children) {
                    val order: Order? = postSnapshot.getValue(Order::class.java)
                    order?.let {
                        orders.add(it)
                    }
                }
                orderListener.onLoad(orders)
            }

            override fun onCancelled(error: DatabaseError) {
                orderListener.onError(error.message)
            }
        })
    }

    fun getItems(category: Category, itemListListener: ItemListListener) {
        getDataBaseReference(ITEM_REFERENCE).orderByChild("category/code").equalTo(category.code)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val categories = mutableListOf<Item>()
                    for (postSnapshot in dataSnapshot.children) {
                        val student: Item? = postSnapshot.getValue(Item::class.java)
                        student?.let {
                            categories.add(it)
                        }
                    }
                    itemListListener.onLoad(categories)
                }

                override fun onCancelled(error: DatabaseError) {
                    itemListListener.onError(error.message)
                }
            })
    }

    fun uploadImage(
        bitmap: Bitmap,
        successListener: OnSuccessListener<String>,
        failureListener: OnFailureListener,
        item: Item
    ) {
        FireBaseUploader(bitmap, successListener, failureListener, item).uploadImageToFirebase(item)
    }

    fun logout() {
        firebaseAuth.signOut()
    }

    fun addPurchase(
        purchase: Purchase,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(PURCHASE_REFERENCE).child(purchase.purchaseId).setValue(purchase)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun addOrUpdateEmployee(
        employee: Employee,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(EMPLOYEE_REFERENCE).child(employee.employeeId).setValue(employee)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun addAttendance(
        attendance: Attendance,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(ATTENDANCE_REFERENCE).child(attendance.id).setValue(attendance)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun addPayment(
        payment: Payment,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(PAYMENT_REFERENCE).child(payment.employeeId).setValue(payment)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun getEmployee(id: String, employeeDetailsCallback: EmployeeDetailsCallback) {
        val databaseReference = getDataBaseReference(EMPLOYEE_REFERENCE)
        databaseReference.child(id)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val employee = dataSnapshot.getValue(Employee::class.java)
                    employee?.let {
                        employeeDetailsCallback.onDetailsLoaded(it)
                    } ?: run {
                        employeeDetailsCallback.onLoadFailed("No employee found with this id")
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    employeeDetailsCallback.onLoadFailed("No employee found with this id")
                }
            })
    }

    fun addCartItem(
        cartItem: CartItem,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(CART_REFERENCE).child(cartItem.cartId).setValue(cartItem)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }

    fun addOrder(
        order: Order,
        successListener: OnSuccessListener<Void>,
        failureListener: OnFailureListener
    ) {
        getDataBaseReference(ORDER_REFERENCE).child(order.orderId).setValue(order)
            .addOnSuccessListener(successListener).addOnFailureListener(failureListener)
    }
}

