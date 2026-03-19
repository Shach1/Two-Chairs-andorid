package com.example.twochairsandroid

import android.app.Application
import com.example.twochairsandroid.core.di.AppContainer

class TwoChairsApp : Application() {
    val appContainer: AppContainer by lazy { AppContainer(this) }
}
