package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R

@Composable
internal fun ProfileScreen(
    onBack: () -> Unit,
    onLogout: () -> Unit,
) {
    TwoChairsBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
            ) {
                Text(
                    text = "Назад",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF3A4FA1),
                    modifier = Modifier.clickable { onBack() },
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
            Text(
                text = "Профиль",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF07090B),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Управление аккаунтом",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 18.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF10243B),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(22.dp))
            GreenCloudButton(
                text = "Выйти",
                onClick = onLogout,
                modifier = Modifier.fillMaxWidth(AuthControlWidthFraction),
            )

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(R.drawable.mascot_registration),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.84f)
                    .offset(y = 30.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}
