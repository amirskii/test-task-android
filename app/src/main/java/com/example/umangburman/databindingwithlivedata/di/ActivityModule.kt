package com.example.umangburman.databindingwithlivedata.di

import com.example.umangburman.databindingwithlivedata.View.LoginFragment
import com.example.umangburman.databindingwithlivedata.View.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector(modules = [MainModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeLoginFragment(): LoginFragment
}
