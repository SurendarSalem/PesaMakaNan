package com.asmirestoran.pesomakanan.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.asmirestoran.pesomakanan.NavigationUtils
import com.asmirestoran.pesomakanan.viewmodel.LoginViewModel
import com.asmirestoran.pesomakanan.R
import com.asmirestoran.pesomakanan.Status
import com.asmirestoran.pesomakanan.User
import com.asmirestoran.pesomakanan.databinding.FragmentLoginBinding
import com.asmirestoran.pesomakanan.local.PreferenceHelper
import com.asmirestoran.pesomakanan.repository.AuthRepository


/**
 * A simple [Fragment] subclass.
 * Use the [SignupFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()
    var error: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginViewModel = loginViewModel
        binding.btnLogin.setOnClickListener {
            loginViewModel.user.value?.let {
                error = User.validForLogin(it)
                binding.error = error
                loginViewModel.login(it)
            }
        }
        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
        }
        loginViewModel.loginResult.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                when (it.status) {
                    Status.LOADING -> {
                        showProgress(true)
                    }
                    Status.SUCCESS -> {
                        showProgress(false)
                        Utils.showToast(
                            requireActivity(),
                            "Welcome back ${AuthRepository.currentUser?.name}"
                        )
                        requireActivity().finish()
                        NavigationUtils.openActivity(requireActivity(), OrderActivity::class.java)
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
}