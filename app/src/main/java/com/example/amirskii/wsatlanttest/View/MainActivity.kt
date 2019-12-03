package com.example.amirskii.wsatlanttest.View

import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import com.example.amirskii.wsatlanttest.R
import com.example.amirskii.wsatlanttest.ViewModel.LoginViewModel
import com.example.amirskii.wsatlanttest.databinding.ActivityMainBinding
import com.example.amirskii.wsatlanttest.factory.AppViewModelFactory
import dagger.android.AndroidInjection
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class MainActivity : DaggerAppCompatActivity() {

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

        if (viewModel.getSession().isNullOrEmpty())
            navigateLogin()
        else {
            navigateMain()
        }
    }

    fun navigateLogin() {
        showFragment(LoginFragment())
    }

    fun navigateMain() {
        showFragment(MainFragment())
    }

    fun showFragment(fragment: Fragment) {
        val ft = supportFragmentManager.beginTransaction()
        ft.replace(R.id.content, fragment)
        ft.commit()
    }

}
