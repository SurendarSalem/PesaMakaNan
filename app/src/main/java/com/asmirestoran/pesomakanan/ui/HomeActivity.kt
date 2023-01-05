package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.databinding.ActivityHomeBinding
import com.asmirestoran.pesomakanan.ui.adapter.AddItemFragment

class HomeActivity : AppCompatActivity() {

    private val categoryListFragment = CategoryListFragment()
    private val addItemFragment = AddItemFragment()
    private val addCategoryFragment = AddCategoryFragment()
    private val fragmentManager = supportFragmentManager
    private var activeFragment: Fragment = categoryListFragment
    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fragmentManager.beginTransaction().apply {
            add(R.id.container, categoryListFragment, getString(R.string.category_name))
            add(R.id.container, addItemFragment, getString(R.string.add_category)).hide(
                addItemFragment
            )
        }.commit()
        initListeners()
    }

    private fun initListeners() {
        binding.bottomNavigationView.setOnItemSelectedListener { menuItem ->
            binding.container1.visibility = GONE
            when (menuItem.itemId) {
                R.id.navigation_category_list -> {
                    fragmentManager.beginTransaction().hide(activeFragment)
                        .show(categoryListFragment).commit()
                    activeFragment = categoryListFragment
                    true
                }

                R.id.navigation_add_category -> {
                    fragmentManager.beginTransaction().hide(activeFragment).show(addItemFragment)
                        .commit()
                    activeFragment = addItemFragment
                    true
                }
                else -> false
            }
        }
    }

    fun openFragment() {
        binding.bottomNavigationView.visibility = GONE
        binding.container1.visibility = VISIBLE
        fragmentManager.beginTransaction()
            .add(R.id.container1, AddCategoryFragment(), "AddCategoryFragment")
            .addToBackStack("AddCategoryFragment").commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        binding.bottomNavigationView.visibility = VISIBLE
        binding.container1.visibility = GONE
        fragmentManager.popBackStack()
    }
}