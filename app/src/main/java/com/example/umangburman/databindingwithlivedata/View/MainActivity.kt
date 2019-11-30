package com.example.umangburman.databindingwithlivedata.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import com.example.umangburman.databindingwithlivedata.Model.Status
import com.example.umangburman.databindingwithlivedata.R
import com.example.umangburman.databindingwithlivedata.ViewModel.LoginViewModel
import com.example.umangburman.databindingwithlivedata.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import kz.kaspibusiness.factory.AppViewModelFactory
import java.util.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)
    }

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)

        binding.setLifecycleOwner(this)

        binding.loginViewModel = viewModel

        btnLogin.setOnClickListener {
            observeViewModel()
        }

        viewModel.userLiveData.observe(this, Observer {
            it?.let { loginUser ->
                if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).strEmailAddress)) {
                    binding.txtEmailAddress.error = "Enter an E-Mail Address"
                    binding.txtEmailAddress.requestFocus()
                } else if (!loginUser!!.isEmailValid) {
                    binding.txtEmailAddress.error = "Enter a Valid E-mail Address"
                    binding.txtEmailAddress.requestFocus()
                } else if (TextUtils.isEmpty(Objects.requireNonNull(loginUser).strPassword)) {
                    binding.txtPassword.error = "Enter a Password"
                    binding.txtPassword.requestFocus()
                } else if (!loginUser.isPasswordLengthGreaterThan5) {
                    binding.txtPassword.error = "Enter at least 6 Digit password"
                    binding.txtPassword.requestFocus()
                } else {
                    binding.lblEmailAnswer.text = loginUser.strEmailAddress
                    binding.lblPasswordAnswer.text = loginUser.strPassword
                }
            }
        })

    }

    private fun observeViewModel() {
        viewModel.login("hello@karta.com", "12345678").observe(this, Observer { it?.let {
            when(it.status) {
                Status.SUCCESS -> {
                    //hideLoadingLayout()
                    if (it.data?.statusCode == 0) {
                    } else {
                        //textInputPassword.error = it.data?.message
                    }
                }
                Status.ERROR -> {}//showError(it)
                Status.LOADING -> {}// showLoadingLayout()
            }
        } })
    }
}
