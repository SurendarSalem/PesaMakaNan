package com.asmirestoran.pesomakanan.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.User
import com.asmirestoran.pesomakanan.databinding.FragmentSignupBinding
import com.asmirestoran.pesomakanan.ui.adapter.RoleAdapter
import com.asmirestoran.pesomakanan.viewmodel.SignupViewModel


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignupFragment : Fragment() {

    lateinit var binding: FragmentSignupBinding
    private val signupViewModel: SignupViewModel by viewModels()
    var error: String = ""
    lateinit var rolesAdapter: RoleAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_signup, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signupViewModel = signupViewModel
        observeForData()
        binding.btnLogin.setOnClickListener {
            validateAndSignup()
        }
    }

    private fun observeForData() {
        signupViewModel.rolesData.observe(viewLifecycleOwner) {
            if (it.isNotEmpty()) {
                rolesAdapter = RoleAdapter(requireContext(), it)
                binding.spRoles.adapter = rolesAdapter
                binding.spRoles.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            adapterView: AdapterView<*>?,
                            view: View?,
                            position: Int,
                            p3: Long
                        ) {
                            signupViewModel.user.value?.role = it[position]
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {

                        }
                    }
            }
        }
        signupViewModel.result.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.LOADING -> {
                        showProgress(true)
                    }
                    Status.SUCCESS -> {
                        showProgress(false)
                        Utils.showSnack(requireActivity(), "Registration done successfully!")
                        findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
                    }
                    Status.ERROR -> {
                        showProgress(false)
                        Utils.showSnack(requireActivity(), it.message)
                    }
                }
            }
        }
    }

    private fun showProgress(show: Boolean) {
        (requireActivity() as AuthenticationActivity).showProgress(show)
    }

    private fun validateAndSignup() {
        signupViewModel.user.value?.let {
            error = User.validForSignup(it)
            binding.error = error
            if (error.isEmpty()) {
                signupViewModel.signup(it)
            }
        }
    }
}