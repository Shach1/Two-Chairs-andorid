package com.example.twochairsandroid.core.di

import android.content.Context
import com.example.twochairsandroid.TwoChairsApp

val Context.appContainer: AppContainer
    get() = (applicationContext as TwoChairsApp).appContainer
