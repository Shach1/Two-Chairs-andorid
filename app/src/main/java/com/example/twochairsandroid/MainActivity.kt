package com.example.twochairsandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.twochairsandroid.ui.onboarding.TwoChairsAppRoot
import com.example.twochairsandroid.ui.theme.TwoChairsAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TwoChairsAndroidTheme {
                TwoChairsAppRoot()
            }
        }
    }
}
