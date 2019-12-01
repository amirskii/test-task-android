package com.example.umangburman.databindingwithlivedata.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umangburman.databindingwithlivedata.R
import com.example.umangburman.databindingwithlivedata.ViewModel.LoginViewModel
import com.example.umangburman.databindingwithlivedata.databinding.FragmentMainBinding
import com.example.umangburman.databindingwithlivedata.factory.AppViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    lateinit var binding: FragmentMainBinding

    val viewModel: LoginViewModel by lazy {
        ViewModelProviders.of(activity!!, viewModelFactory).get(LoginViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.setLifecycleOwner(this)
        binding.loginViewModel = viewModel

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogout.setOnClickListener {
            logout()
        }

        observeViewModel()
    }

    private fun logout() {
        viewModel.logout()
        (activity as MainActivity).navigateLogin()
    }

    private fun observeViewModel() {
        viewModel.userLiveData.observe(this, Observer {
            it?.let { loginUser ->
                binding.lblEmailAnswer.text = loginUser.strEmailAddress
                binding.lblPasswordAnswer.text = loginUser.strPassword
            }
        })

//        viewModel.login("hello@karta.com", "12345678").observe(this, Observer { it?.let {
//            when(it.status) {
//                Status.SUCCESS -> {
//                    //hideLoadingLayout()
//                    it.data?.token?.let {
//                        viewModel.saveToken(it)
//                        // jwt.getClaim("session_id").asString()?.let {
//                    }
//                }
//                Status.ERROR -> {}//showError(it)
//                Status.LOADING -> {}// showLoadingLayout()
//            }
//        } })
    }


}