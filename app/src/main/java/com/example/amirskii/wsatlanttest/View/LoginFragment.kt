package com.example.amirskii.wsatlanttest.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.amirskii.wsatlanttest.Model.Status
import com.example.amirskii.wsatlanttest.R
import com.example.amirskii.wsatlanttest.ViewModel.LoginViewModel
import com.example.amirskii.wsatlanttest.databinding.FragmentLoginBinding
import com.example.amirskii.wsatlanttest.factory.AppViewModelFactory
import kotlinx.android.synthetic.main.fragment_login.*
import javax.inject.Inject


class LoginFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    lateinit var binding: FragmentLoginBinding

    val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        binding.setLifecycleOwner(this)
        binding.loginViewModel = viewModel

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDefaults()
        btnLogin.setOnClickListener {
            observeViewModel()
        }
    }

    private fun setDefaults() {
        viewModel.emailAddress.value = "hello@karta.com"
        viewModel.password.value = "12345678"
    }

    private fun observeViewModel() {
        viewModel.login().observe(this, Observer { it?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    //hideLoadingLayout()
                    it.data?.token?.let {
                        viewModel.saveToken(it)
                        viewModel.setUser()
                        (activity as MainActivity).navigateMain()
                        // jwt.getClaim("session_id").asString()?.let {
                    }
                }
                Status.ERROR -> {}//showError(it)
                Status.LOADING -> {}// showLoadingLayout()
            }
        } })
    }


}