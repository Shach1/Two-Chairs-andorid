package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.twochairsandroid.R
import kotlinx.coroutines.delay

@Composable
internal fun ScreensaverScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1800)
        onFinished()
    }

    TwoChairsBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.screensaver_mascot),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .aspectRatio(1f),
            )

            Spacer(modifier = Modifier.height(34.dp))

            Image(
                painter = painterResource(R.drawable.screensaver_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.78f),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
