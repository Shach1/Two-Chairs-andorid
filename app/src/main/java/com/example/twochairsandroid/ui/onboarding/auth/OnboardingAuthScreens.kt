package com.example.twochairsandroid.ui.onboarding

import androidx.compose.runtime.Composable
import com.example.twochairsandroid.R
import com.example.twochairsandroid.domain.model.AuthSession

@Composable
internal fun RegisterScreen(
    onBack: () -> Unit,
    onAlreadyHaveAccount: () -> Unit,
    onRegistrationDone: (AuthSession) -> Unit,
) {
    SmsAuthScreen(
        mode = SmsMode.Register,
        title = "Регистрация",
        description = "Введите телефон и вам придет\nСМС для регистрации",
        mascotRes = R.drawable.mascot_registration,
        onBack = onBack,
        secondaryActionText = "У меня уже есть аккаунт",
        onSecondaryAction = onAlreadyHaveAccount,
        onSuccess = onRegistrationDone,
    )
}

@Composable
internal fun LoginScreen(
    onBack: () -> Unit,
    onNeedRegistration: () -> Unit,
    onLoginDone: (AuthSession) -> Unit,
) {
    SmsAuthScreen(
        mode = SmsMode.Login,
        title = "Вход",
        description = "Введите телефон и вам придет\nСМС для входа",
        mascotRes = R.drawable.mascot_login,
        onBack = onBack,
        secondaryActionText = "Регистрация",
        onSecondaryAction = onNeedRegistration,
        onSuccess = onLoginDone,
    )
}
