package com.example.umangburman.databindingwithlivedata.di

import com.example.umangburman.databindingwithlivedata.View.LoginFragment
import com.example.umangburman.databindingwithlivedata.View.MainActivity
import com.example.umangburman.databindingwithlivedata.View.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector


@Module
abstract class ActivityModule {
    @ContributesAndroidInjector
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    internal abstract fun contributeLoginFragment(): LoginFragment

    @ContributesAndroidInjector
    internal abstract fun contributeMainFragment(): MainFragment
}
