package com.example.umangburman.databindingwithlivedata.di

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.umangburman.databindingwithlivedata.ViewModel.LoginViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kz.kaspibusiness.di.ViewModelKey
import kz.kaspibusiness.factory.AppViewModelFactory

@Module
internal abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    internal abstract fun bindLoginViewModel(vm: LoginViewModel): ViewModel


    @Binds
    internal abstract fun bindViewModelFactory(appViewModelFactory: AppViewModelFactory): ViewModelProvider.Factory
}
