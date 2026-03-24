package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.ProductType
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@Composable
internal fun DeckMenuScreen(
    onBack: () -> Unit,
    onSelectDeck: (Long) -> Unit = {},
) {
    val context = LocalContext.current
    val deckRepository = remember(context) { context.appContainer.deckRepository }
    val myDeckRepository = remember(context) { context.appContainer.myDeckRepository }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(DeckMenuUiState(isLoading = true)) }

    val reload: () -> Unit = {
        scope.launch {
            uiState = DeckMenuUiState(isLoading = true)
            val accessibleResultDeferred = async { deckRepository.getAccessibleDecks() }
            val myDecksResultDeferred = async { myDeckRepository.getMyDecks() }

            val accessibleResult = accessibleResultDeferred.await()
            val myDecksResult = myDecksResultDeferred.await()

            when (accessibleResult) {
                is ApiResult.Success -> {
                    val myDecks = when (myDecksResult) {
                        is ApiResult.Success -> myDecksResult.data
                        is ApiResult.Error -> emptyList()
                    }

                    uiState = DeckMenuUiState(
                        isLoading = false,
                        decks = mergeGameDecks(
                            accessibleDecks = accessibleResult.data,
                            myDecks = myDecks,
                        ),
                    )
                }

                is ApiResult.Error -> {
                    uiState = DeckMenuUiState(
                        isLoading = false,
                        errorText = accessibleResult.error.message,
                    )
                }
            }
        }
    }

    LaunchedEffect(Unit) {
        reload()
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
                HeaderRow(
                    onBack = onBack,
                    onMenu = {},
                    showMenu = false,
                )
            }

            item {
                Text(
                    text = "Выбор колоды",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 44.sp,
                        lineHeight = 44.sp,
                    ),
                    textAlign = TextAlign.Center,
                    color = Color(0xFF07090B),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        Text(
                            text = "Загружаем колоды...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF213D86),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                !uiState.errorText.isNullOrBlank() -> {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.errorText.orEmpty(),
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
                                onClick = reload,
                                modifier = Modifier.fillMaxWidth(0.62f),
                            )
                        }
                    }
                }

                uiState.decks.isEmpty() -> {
                    item {
                        Text(
                            text = "У вас пока нет доступных колод",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF213D86),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                else -> {
                    itemsIndexed(uiState.decks) { index, deck ->
                        DeckMenuRow(
                            title = deck.title,
                            colors = DeckRowGradients[index % DeckRowGradients.size],
                            onClick = { onSelectDeck(deck.id) },
                        )
                    }
                }
            }
        }
    }
}

@Composable
internal fun ThemeDetailsScreen(
    deckId: Long,
    onBack: () -> Unit,
    onOpenPremiumPromo: () -> Unit,
    onBuy: (Product) -> Unit = {},
) {
    val context = LocalContext.current
    val deckRepository = remember(context) { context.appContainer.deckRepository }
    val storeRepository = remember(context) { context.appContainer.storeRepository }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(ThemeDetailsUiState(isLoading = true)) }

    val reload: () -> Unit = {
        scope.launch {
            uiState = ThemeDetailsUiState(isLoading = true)

            val productsDeferred = async { storeRepository.getProducts() }
            val decksDeferred = async { deckRepository.getStoreDecks() }
            val productsResult = productsDeferred.await()
            val decksResult = decksDeferred.await()

            if (productsResult is ApiResult.Error) {
                uiState = ThemeDetailsUiState(
                    isLoading = false,
                    errorText = productsResult.error.message,
                )
                return@launch
            }
            if (decksResult is ApiResult.Error) {
                uiState = ThemeDetailsUiState(
                    isLoading = false,
                    errorText = decksResult.error.message,
                )
                return@launch
            }

            productsResult as ApiResult.Success
            decksResult as ApiResult.Success

            val product = productsResult.data.firstOrNull {
                it.type == ProductType.DECK && it.deckId == deckId
            }
            val deck = decksResult.data.firstOrNull { it.id == deckId }
            val premiumPrice = productsResult.data.firstOrNull { it.id == 3L }?.priceRub

            uiState = if (deck != null && product != null) {
                ThemeDetailsUiState(
                    isLoading = false,
                    deck = deck,
                    product = product,
                    premiumPriceRub = premiumPrice,
                )
            } else {
                ThemeDetailsUiState(
                    isLoading = false,
                    errorText = "Не удалось загрузить тему",
                )
            }
        }
    }

    LaunchedEffect(deckId) {
        reload()
    }

    TwoChairsBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 18.dp, vertical = 14.dp),
        ) {
            when {
                uiState.isLoading -> {
                    Text(
                        text = "Загружаем тему...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF213D86),
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                !uiState.errorText.isNullOrBlank() -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = uiState.errorText.orEmpty(),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFFD23A63),
                            textAlign = TextAlign.Center,
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        GreenCloudButton(
                            text = "Повторить",
                            onClick = reload,
                            modifier = Modifier.fillMaxWidth(0.62f),
                        )
                    }
                }

                else -> {
                    val deck = uiState.deck ?: return@Box
                    val product = uiState.product ?: return@Box
                    val premiumPrice = uiState.premiumPriceRub

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        HeaderRow(
                            onBack = onBack,
                            onMenu = {},
                            showMenu = false,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = deck.title,
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize = 28.sp,
                                lineHeight = 34.sp,
                            ),
                            color = Color(0xFF07090B),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.86f),
                        )
                        Spacer(modifier = Modifier.height(26.dp))
                        Text(
                            text = deck.description.orEmpty().ifBlank { "Описание темы появится позже" },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 20.sp,
                                lineHeight = 26.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF294BA2),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(0.88f),
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "${deck.ageRating} +",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 28.sp,
                                lineHeight = 30.sp,
                            ),
                            color = Color(0xFF294BA2),
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        ThemeBuyButton(
                            modifier = Modifier.fillMaxWidth(0.86f),
                            text = "Купить ${product.priceRub} ₽",
                            onClick = { onBuy(product) },
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = if (premiumPrice != null) {
                                "Или купите премиум\nоткрывает все режимы\nи темы за $premiumPrice ₽"
                            } else {
                                "Или купите премиум"
                            },
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF294BA2),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(0.88f)
                                .clickable { onOpenPremiumPromo() },
                        )
                        Text(
                            text = ">",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 40.sp,
                                lineHeight = 44.sp,
                            ),
                            color = Color(0xFF294BA2),
                        )

                        Spacer(modifier = Modifier.weight(1f))
                    }

                    Image(
                        painter = painterResource(R.drawable.mascot_deck_details),
                        contentDescription = null,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(0.68f)
                            .offset(y = 18.dp),
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    }
}
