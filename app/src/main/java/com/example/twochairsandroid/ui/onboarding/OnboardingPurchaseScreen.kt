package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.PurchaseResult
import kotlinx.coroutines.launch

private data class PurchaseUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val readyToPay: Boolean = false,
)

@Composable
internal fun PurchaseScreen(
    productId: Long,
    productTitle: String,
    priceRub: Int,
    onBack: () -> Unit,
    onRequireRegistration: () -> Unit,
    onPurchased: (PurchaseResult) -> Unit,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val authRepository = remember(context) { context.appContainer.authRepository }
    val storeRepository = remember(context) { context.appContainer.storeRepository }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(PurchaseUiState()) }

    LaunchedEffect(Unit) {
        val accessToken = authRepository.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            onRequireRegistration()
            return@LaunchedEffect
        }
        uiState = uiState.copy(readyToPay = true)
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

            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Оплата",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF07090B),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Заполните немного данных\nдля оплаты",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF294BA2),
                textAlign = TextAlign.Center,
            )

            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = productTitle.ifBlank { "Покупка" },
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 34.sp,
                    lineHeight = 36.sp,
                ),
                color = Color(0xFF294BA2),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${priceRub.coerceAtLeast(0)} ₽",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 30.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF294BA2),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.weight(1f))

            if (!uiState.errorText.isNullOrBlank()) {
                Text(
                    text = uiState.errorText.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFFD23A63),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            GreenCloudButton(
                text = if (uiState.isLoading) "Оплачиваем..." else "Оплатить ${priceRub.coerceAtLeast(0)} ₽",
                onClick = {
                    if (!uiState.readyToPay || uiState.isLoading) return@GreenCloudButton

                    scope.launch {
                        uiState = uiState.copy(isLoading = true, errorText = null)
                        when (val result = storeRepository.purchase(productId)) {
                            is ApiResult.Success -> onPurchased(result.data)
                            is ApiResult.Error -> {
                                if (result.error.httpCode == 401) {
                                    onRequireRegistration()
                                } else {
                                    uiState = uiState.copy(errorText = result.error.message)
                                }
                            }
                        }
                        uiState = uiState.copy(isLoading = false)
                    }
                },
                enabled = uiState.readyToPay && !uiState.isLoading,
                modifier = Modifier.fillMaxWidth(0.9f),
            )
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
