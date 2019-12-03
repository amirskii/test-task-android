package com.example.amirskii.wsatlanttest.di

import com.example.amirskii.wsatlanttest.View.LoginFragment
import com.example.amirskii.wsatlanttest.View.MainActivity
import com.example.amirskii.wsatlanttest.View.MainFragment
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
