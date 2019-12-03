package com.example.umangburman.databindingwithlivedata.View

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.umangburman.databindingwithlivedata.R
import com.example.umangburman.databindingwithlivedata.ViewModel.MainViewModel
import com.example.umangburman.databindingwithlivedata.databinding.FragmentMainBinding
import com.example.umangburman.databindingwithlivedata.factory.AppViewModelFactory
import kotlinx.android.synthetic.main.fragment_main.*
import javax.inject.Inject


class MainFragment: BaseFragment() {

    @Inject
    lateinit var viewModelFactory: AppViewModelFactory

    lateinit var binding: FragmentMainBinding

    val viewModel: MainViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
    }

    private val adapter by lazy { TransactionAdapter() }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        binding.setLifecycleOwner(this)
        binding.viewModel = viewModel

        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        transactionsRv.layoutManager = LinearLayoutManager(context)
        transactionsRv.adapter = adapter

        btnLogout.setOnClickListener {
            logout()
        }

        btnStart.setOnClickListener {
            viewModel.startListening()
        }

        btnStop.setOnClickListener {
            viewModel.stopListening()
        }
        viewModel.getUserInfo()
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
                viewModel.startListening()
            }
        })
        viewModel.transactionData.observe(this, Observer {
            it?.let {
                adapter.add(it)
            }
        })
        viewModel.sumData.observe(this, Observer {
            it?.let { sum ->
                lblSumValue.text = sum.toString()
            }
        })
    }


}