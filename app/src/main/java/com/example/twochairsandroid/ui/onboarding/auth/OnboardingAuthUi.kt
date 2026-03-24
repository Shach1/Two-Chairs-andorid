package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.AuthSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

internal enum class SmsMode {
    Register,
    Login,
}

private enum class SmsStep {
    EnterPhone,
    EnterCode,
}

@Composable
internal fun SmsAuthScreen(
    mode: SmsMode,
    title: String,
    description: String,
    mascotRes: Int,
    onBack: () -> Unit,
    secondaryActionText: String,
    onSecondaryAction: () -> Unit,
    onSuccess: (AuthSession) -> Unit,
) {
    val context = LocalContext.current
    val authRepository = remember(context) { context.appContainer.authRepository }
    val scope = rememberCoroutineScope()

    var step by rememberSaveable { mutableStateOf(SmsStep.EnterPhone) }
    var phone by rememberSaveable { mutableStateOf("+7") }
    var code by rememberSaveable { mutableStateOf("") }
    var expiresInSeconds by rememberSaveable { mutableStateOf<Int?>(null) }
    var isLoading by rememberSaveable { mutableStateOf(false) }
    var errorText by rememberSaveable { mutableStateOf<String?>(null) }

    val buttonText = when (step) {
        SmsStep.EnterPhone -> "Прислать мне СМС"
        SmsStep.EnterCode -> if (mode == SmsMode.Register) "Регистрация" else "Войти"
    }

    LaunchedEffect(step, expiresInSeconds) {
        if (step != SmsStep.EnterCode) return@LaunchedEffect
        val remaining = expiresInSeconds ?: return@LaunchedEffect
        if (remaining > 0) {
            delay(1000)
            expiresInSeconds = (expiresInSeconds ?: 1) - 1
        }
    }

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

            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF07090B),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                ),
                color = Color(0xFF10243B),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.height(18.dp))

            AuthTextField(
                value = if (step == SmsStep.EnterPhone) phone else code,
                placeholder = if (step == SmsStep.EnterPhone) "+7" else if (mode == SmsMode.Register) "Регистрация" else "Вход",
                keyboardType = if (step == SmsStep.EnterPhone) KeyboardType.Phone else KeyboardType.Number,
                onValueChange = { newValue ->
                    if (step == SmsStep.EnterPhone) {
                        phone = newValue
                    } else {
                        code = newValue
                    }
                },
            )

            if (step == SmsStep.EnterCode) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Введите код из СМС" + (expiresInSeconds?.let { " (${it}с)" } ?: ""),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                    ),
                    color = Color(0xFF3A4FA1),
                )
            }

            if (!errorText.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorText ?: "",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFFD23A63),
                    textAlign = TextAlign.Center,
                )
            }

            Spacer(modifier = Modifier.height(14.dp))
            GreenCloudButton(
                text = if (isLoading) "Отправка..." else buttonText,
                onClick = {
                    if (isLoading) return@GreenCloudButton
                    scope.launch {
                        errorText = null
                        isLoading = true

                        when (step) {
                            SmsStep.EnterPhone -> {
                                val currentPhone = phone.trim()
                                if (currentPhone.length < 4) {
                                    errorText = "Введите корректный номер телефона"
                                } else {
                                    when (val result = authRepository.sendCode(currentPhone)) {
                                        is ApiResult.Success -> {
                                            step = SmsStep.EnterCode
                                            code = ""
                                            expiresInSeconds = result.data.expiresInSeconds
                                        }

                                        is ApiResult.Error -> {
                                            errorText = result.error.message
                                        }
                                    }
                                }
                            }

                            SmsStep.EnterCode -> {
                                val currentCode = code.trim()
                                if (currentCode.isBlank()) {
                                    errorText = "Введите код из СМС"
                                } else {
                                    when (val result = authRepository.verifyCode(phone.trim(), currentCode)) {
                                        is ApiResult.Success -> onSuccess(result.data)
                                        is ApiResult.Error -> errorText = result.error.message
                                    }
                                }
                            }
                        }

                        isLoading = false
                    }
                },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(AuthControlWidthFraction),
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = secondaryActionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF3A4FA1),
                modifier = Modifier.clickable { onSecondaryAction() },
            )

            Spacer(modifier = Modifier.weight(1f))
            Image(
                painter = painterResource(mascotRes),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.86f)
                    .offset(y = 24.dp),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

@Composable
internal fun RegisterResultScreen(onContinue: () -> Unit) {
    TwoChairsBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(R.drawable.mascot_registration),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.9f)
                    .offset(y = 30.dp),
                contentScale = ContentScale.FillWidth,
                alpha = 0.9f,
            )

            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(0.86f)
                    .clip(RoundedCornerShape(30.dp))
                    .background(Color.White)
                    .padding(horizontal = 24.dp, vertical = 22.dp),
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Регистрация\nпрошла успешно",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 22.sp,
                            lineHeight = 26.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color(0xFF334D9D),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Мы рады вам!",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                        ),
                        color = Color(0xFF334D9D),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    GreenCloudButton(
                        text = "Начать отрываться",
                        onClick = onContinue,
                        modifier = Modifier.fillMaxWidth(AuthControlWidthFraction),
                    )
                }
            }
        }
    }
}

@Composable
internal fun PurchaseStubScreen(onBack: () -> Unit) {
    TwoChairsBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(
                text = "Экран оплаты\nсделаем следующим шагом",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center,
                color = Color(0xFF102030),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Назад к промо",
                color = Color(0xFF364EAD),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.clickable { onBack() },
            )
        }
    }
}
