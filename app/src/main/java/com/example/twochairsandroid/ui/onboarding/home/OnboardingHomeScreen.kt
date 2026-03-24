package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.ProductType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
internal fun HomeScreen(
    isPremiumUser: Boolean,
    refreshSignal: Int,
    onOpenPaidTheme: (Product) -> Unit,
    onOpenProfile: () -> Unit,
    onOpenPremiumPromo: () -> Unit,
    onOpenMyDecks: () -> Unit,
    onUnlockMyDeckFeature: (Product) -> Unit,
    onStartGame: () -> Unit = {},
) {
    val context = LocalContext.current
    val deckRepository = remember(context) { context.appContainer.deckRepository }
    val myDeckRepository = remember(context) { context.appContainer.myDeckRepository }
    val storeRepository = remember(context) { context.appContainer.storeRepository }
    val scope = rememberCoroutineScope()

    var showcaseState by remember { mutableStateOf(HomeShowcaseUiState()) }
    var myDeckFeatureState by remember { mutableStateOf(MyDeckFeatureUiState(isLoading = true)) }

    val reloadShowcase: () -> Unit = {
        scope.launch {
            showcaseState = HomeShowcaseUiState(isLoading = true)

            val productsDeferred = async { storeRepository.getProducts() }
            val decksDeferred = async { deckRepository.getAccessibleDecks() }

            val productsResult = productsDeferred.await()
            val decksResult = decksDeferred.await()

            if (productsResult is ApiResult.Error) {
                showcaseState = HomeShowcaseUiState(
                    isLoading = false,
                    errorText = productsResult.error.message,
                )
                return@launch
            }

            if (decksResult is ApiResult.Error) {
                showcaseState = HomeShowcaseUiState(
                    isLoading = false,
                    errorText = decksResult.error.message,
                )
                return@launch
            }

            productsResult as ApiResult.Success
            decksResult as ApiResult.Success

            showcaseState = HomeShowcaseUiState(
                isLoading = false,
                products = filterPaidDeckProducts(
                    products = productsResult.data,
                    accessibleDecks = decksResult.data,
                ),
            )
        }
    }

    val reloadMyDeckFeature: () -> Unit = {
        scope.launch {
            myDeckFeatureState = MyDeckFeatureUiState(isLoading = true)

            val canCreateResult = myDeckRepository.canCreateDecks()
            val productsResult = storeRepository.getProducts()

            when (canCreateResult) {
                is ApiResult.Success -> {
                    myDeckFeatureState = MyDeckFeatureUiState(
                        isLoading = false,
                        canCreate = canCreateResult.data,
                        unlockProduct = (productsResult as? ApiResult.Success)?.data?.firstOrNull {
                            it.type == ProductType.FEATURE_CREATE_DECKS
                        },
                    )
                }

                is ApiResult.Error -> {
                    myDeckFeatureState = MyDeckFeatureUiState(
                        isLoading = false,
                        errorText = canCreateResult.error.message,
                    )
                }
            }
        }
    }

    LaunchedEffect(isPremiumUser, refreshSignal) {
        if (isPremiumUser) {
            showcaseState = HomeShowcaseUiState(isLoading = false)
        } else {
            reloadShowcase()
        }
        reloadMyDeckFeature()
    }

    TwoChairsBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                HomeTopBar(
                    isPremiumUser = isPremiumUser,
                    onProfileClick = onOpenProfile,
                    onStandardClick = onOpenPremiumPromo,
                )
            }

            item {
                HomeQuestionCard(onStartGame = onStartGame)
            }

            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Супер темы",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        lineHeight = 32.sp,
                    ),
                    color = Color(0xFF07090B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "открой еще более сумасшедшие\nвопросы",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF07101B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            when {
                isPremiumUser -> {
                    item {
                        PremiumOpenedLabel()
                    }
                }

                showcaseState.isLoading -> {
                    item {
                        Text(
                            text = "Загружаем темы...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF213D86),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                !showcaseState.errorText.isNullOrBlank() -> {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = showcaseState.errorText.orEmpty(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = Color(0xFFD23A63),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            GreenCloudButton(
                                text = "Повторить",
                                onClick = reloadShowcase,
                                modifier = Modifier.fillMaxWidth(0.62f),
                            )
                        }
                    }
                }

                showcaseState.products.isEmpty() -> {
                    item {
                        Text(
                            text = "Новых платных тем пока нет",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF213D86),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                else -> {
                    itemsIndexed(showcaseState.products) { index, product ->
                        PaidThemeRow(
                            product = product,
                            colors = ShowcaseGradients[index % ShowcaseGradients.size],
                            onClick = { onOpenPaidTheme(product) },
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Вы никогда не видели такие\nвопросы!",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 28.sp,
                    ),
                    color = Color(0xFF07101B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            item {
                MyDeckFeatureSection(
                    uiState = myDeckFeatureState,
                    onRetry = reloadMyDeckFeature,
                    onOpenMyDecks = onOpenMyDecks,
                    onUnlock = onUnlockMyDeckFeature,
                )
            }
        }
    }
}
