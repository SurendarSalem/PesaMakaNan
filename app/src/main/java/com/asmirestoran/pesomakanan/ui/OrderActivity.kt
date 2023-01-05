package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.ui.AppBarConfiguration
import com.asmirestoran.pesomakanan.NavigationUtils
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.databinding.ActivityOrderBinding
import com.asmirestoran.pesomakanan.local.PreferenceHelper
import com.asmirestoran.pesomakanan.model.Order
import com.asmirestoran.pesomakanan.ui.adapter.AddItemFragment


class OrderActivity : AppCompatActivity() {

    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityOrderBinding
    private val categoryListFragment = CategoryListFragment()
    private val addItemFragment = AddItemFragment()
    private val purchaseListFragment = PurchaseListFragment()
    private val addEmployeeFragment = AddEmployeeFragment()
    private val attendanceListFragment = AttendanceListFragment()
    private val paymentFragment = PaymentFragment()
    private val homeFragment = HomeFragment()
    private val orderListFragment = OrderListFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = homeFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarOrder.toolbar)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, orderListFragment, getString(R.string.orders)).hide(
                orderListFragment
            )
            add(R.id.container, homeFragment, getString(R.string.menu_home)).hide(homeFragment)
            add(R.id.container, categoryListFragment, getString(R.string.categories)).hide(
                categoryListFragment
            )
            add(R.id.container, addItemFragment, getString(R.string.add_category)).hide(
                addItemFragment
            )
            add(R.id.container, purchaseListFragment, getString(R.string.recent_purchases)).hide(
                purchaseListFragment
            )
            add(R.id.container, addEmployeeFragment, getString(R.string.add_employee)).hide(
                addEmployeeFragment
            )
            add(R.id.container, attendanceListFragment, getString(R.string.attendance)).hide(
                attendanceListFragment
            )
            add(R.id.container, paymentFragment, getString(R.string.payment)).hide(
                paymentFragment
            )
        }.commit()


        val drawerLayout: DrawerLayout = binding.drawerLayout
        actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        );
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        initListeners()
        binding.navView.setCheckedItem(R.id.nav_home)
        showFragment(orderListFragment, "Home")
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }

    private fun initListeners() {
        binding.navView.setNavigationItemSelectedListener { menuItem ->
            binding.drawerLayout.closeDrawers()
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    showFragment(orderListFragment, "Home")
                    true
                }

                R.id.nav_category -> {
                    showFragment(categoryListFragment, "Categories")
                    true
                }
                R.id.nav_slideshow -> {
                    showFragment(addItemFragment, "Add Item")
                    true
                }
                R.id.nav_purchase -> {
                    showFragment(purchaseListFragment, "Purchase")
                    true
                }
                R.id.nav_employee -> {
                    showFragment(addEmployeeFragment, "Purchase")
                    true
                }
                R.id.nav_attendance -> {
                    showFragment(attendanceListFragment, "Attendances")
                    true
                }
                R.id.nav_payment -> {
                    showFragment(paymentFragment, "Payment")
                    true
                }
                R.id.nav_logout -> {
                    PreferenceHelper.clearAll()
                    finish()
                    openAuthentication()
                    true
                }
                else -> false
            }
        }
    }

    private fun openAuthentication() {
        NavigationUtils.openActivity(this, AuthenticationActivity::class.java)
    }

    private fun showFragment(fragment: Fragment, label: String) {
        fragmentManager.beginTransaction().hide(activeFragment)
            .show(fragment).commit()
        activeFragment = fragment
        title = label
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.order, menu)
        return true
    }

    fun openAddCategoryScreen() {
        activeFragment = AddCategoryFragment()
        fragmentManager.beginTransaction()
            .add(R.id.container, activeFragment, "AddCategoryFragment")
            .addToBackStack("AddCategoryFragment").commit()
        title = "Add Category"
    }

    fun openCreateOrder(order: Order? = null) {
        activeFragment = OrderFragment()
        order?.let {
            activeFragment.arguments = Bundle().apply {
                putParcelable(KEY_ORDER, order)
            }
        }
        fragmentManager.beginTransaction()
            .add(R.id.container, activeFragment, "OrderFragment")
            .addToBackStack("OrderFragment").commit()
        title = "Create Order"
    }

    fun openAddAttendanceScreen() {
        activeFragment = AttendanceFragment()
        fragmentManager.beginTransaction()
            .add(R.id.container, activeFragment, "AttendanceFragment")
            .addToBackStack("AttendanceFragment").commit()
        title = "Add Attendance"
    }

    fun openAddPurchaseScreen() {
        activeFragment = PurchaseFragment()
        fragmentManager.beginTransaction()
            .add(R.id.container, activeFragment, "PurchaseFragment")
            .addToBackStack("PurchaseFragment").commit()
        title = "Add Purchase"
    }

    fun openCatalogScreen(order: Order?) {
        activeFragment = HomeFragment()
        order?.let {
            activeFragment.arguments = Bundle().apply {
                putParcelable(KEY_ORDER, it)
            }
        }
        fragmentManager.beginTransaction()
            .add(R.id.container, activeFragment, "HomeFragment")
            .addToBackStack("HomeFragment").commit()
        title = "Catalog"
    }


    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.closeDrawers()
        } else {
            if (activeFragment is OrderListFragment) {
                super.onBackPressed()
            } else if (activeFragment is AddCategoryFragment || activeFragment is PurchaseFragment
                || activeFragment is AttendanceFragment || activeFragment is HomeFragment
                || activeFragment is OrderFragment
            ) {
                super.onBackPressed()
                val list = mutableListOf<Fragment>()
                list.addAll(fragmentManager.fragments.asReversed())
                list.forEach {
                    if (it.isVisible) {
                        activeFragment = it
                    }
                }
            } else {
                binding.navView.setCheckedItem(R.id.nav_home)
                showFragment(orderListFragment, "Orders")
            }
        }
    }
}