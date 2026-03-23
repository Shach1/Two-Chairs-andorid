package com.example.twochairsandroid.ui.onboarding

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.ProductType
import kotlinx.coroutines.launch

@Composable
fun TwoChairsAppRoot() {
    val context = LocalContext.current
    val authRepository = remember(context) { context.appContainer.authRepository }
    val storeRepository = remember(context) { context.appContainer.storeRepository }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var isPremiumUser by remember { mutableStateOf(false) }
    var homeRefreshSignal by remember { mutableIntStateOf(0) }
    var startDestination by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(authRepository) {
        val accessToken = authRepository.getAccessToken()
        if (accessToken.isNullOrBlank()) {
            isPremiumUser = false
            startDestination = OnboardingRoutes.Splash
        } else {
            isPremiumUser = authRepository.getIsPremium()
            startDestination = OnboardingRoutes.Home
        }
    }

    val destination = startDestination
    if (destination == null) {
        Surface(modifier = Modifier.fillMaxSize()) {}
        return
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = destination,
        ) {
            composable(OnboardingRoutes.Splash) {
                ScreensaverScreen(
                    onFinished = {
                        navController.navigate(OnboardingRoutes.Rules) {
                            popUpTo(OnboardingRoutes.Splash) { inclusive = true }
                        }
                    },
                )
            }

            composable(OnboardingRoutes.Rules) {
                RulesScreen(onSkip = { navController.navigate(OnboardingRoutes.PremiumPromo) })
            }

            composable(OnboardingRoutes.PremiumPromo) {
                PremiumPromoScreen(
                    onSkip = { navController.navigate(OnboardingRoutes.RegisterOffer) },
                    onBuy = { navController.navigate(OnboardingRoutes.Register) },
                )
            }

            composable(OnboardingRoutes.PremiumPromoBack) {
                PremiumPromoScreen(
                    onSkip = { navController.popBackStack() },
                    onBuy = {
                        scope.launch {
                            val premiumProduct = when (val result = storeRepository.getProducts()) {
                                is ApiResult.Success -> result.data.firstOrNull {
                                    it.type == ProductType.PREMIUM || it.id == 3L
                                }

                                is ApiResult.Error -> null
                            }

                            navController.navigate(
                                OnboardingRoutes.purchase(
                                    productId = premiumProduct?.id ?: 3L,
                                    title = premiumProduct?.title ?: "Премиум",
                                    priceRub = premiumProduct?.priceRub ?: 1990,
                                )
                            )
                        }
                    },
                )
            }

            composable(OnboardingRoutes.RegisterOffer) {
                RegisterOfferScreen(onRegister = { navController.navigate(OnboardingRoutes.Register) })
            }

            composable(OnboardingRoutes.Register) {
                RegisterScreen(
                    onBack = { navController.popBackStack() },
                    onAlreadyHaveAccount = { navController.navigate(OnboardingRoutes.Login) },
                    onRegistrationDone = { session ->
                        isPremiumUser = session.user.isPremium
                        navController.navigate(OnboardingRoutes.RegisterResult)
                    },
                )
            }

            composable(OnboardingRoutes.Login) {
                LoginScreen(
                    onBack = { navController.popBackStack() },
                    onNeedRegistration = { navController.navigate(OnboardingRoutes.Register) },
                    onLoginDone = { session ->
                        isPremiumUser = session.user.isPremium
                        navController.navigate(OnboardingRoutes.Home)
                    },
                )
            }

            composable(OnboardingRoutes.RegisterResult) {
                RegisterResultScreen(onContinue = { navController.navigate(OnboardingRoutes.Home) })
            }

            composable(OnboardingRoutes.Home) {
                HomeScreen(
                    isPremiumUser = isPremiumUser,
                    refreshSignal = homeRefreshSignal,
                    onOpenPaidTheme = { product ->
                        val deckId = product.deckId
                        if (deckId != null) {
                            navController.navigate(OnboardingRoutes.themeDetails(deckId))
                        }
                    },
                    onOpenProfile = { navController.navigate(OnboardingRoutes.Profile) },
                    onOpenPremiumPromo = { navController.navigate(OnboardingRoutes.PremiumPromoBack) },
                    onOpenMyDecks = { navController.navigate(OnboardingRoutes.MyDecks) },
                    onUnlockMyDeckFeature = { product ->
                        navController.navigate(
                            OnboardingRoutes.purchase(
                                productId = product.id,
                                title = product.title,
                                priceRub = product.priceRub,
                            )
                        )
                    },
                    onStartGame = { navController.navigate(OnboardingRoutes.DeckMenu) },
                )
            }

            composable(OnboardingRoutes.Profile) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onLogout = {
                        scope.launch {
                            authRepository.clearSession()
                            isPremiumUser = false
                            navController.navigate(OnboardingRoutes.Register) {
                                popUpTo(OnboardingRoutes.Home) { inclusive = true }
                            }
                        }
                    },
                )
            }

            composable(OnboardingRoutes.MyDecks) {
                MyDecksScreen(
                    onBack = { navController.popBackStack() },
                    onOpenDeckEditor = { deckId ->
                        navController.navigate(OnboardingRoutes.myDeckEditor(deckId))
                    },
                    onCreateDeck = {
                        navController.navigate(OnboardingRoutes.myDeckEditor(deckId = null))
                    },
                )
            }

            composable(OnboardingRoutes.MyDeckEditorPattern) { backStackEntry ->
                val myDeckId = OnboardingRoutes.parseMyDeckId(
                    backStackEntry.arguments?.getString(OnboardingRoutes.MyDeckIdArg),
                )
                MyDeckEditorScreen(
                    deckId = myDeckId,
                    onBack = { navController.popBackStack() },
                )
            }

            composable(OnboardingRoutes.DeckMenu) {
                DeckMenuScreen(
                    onBack = { navController.popBackStack() },
                )
            }

            composable(OnboardingRoutes.ThemeDetailsPattern) { backStackEntry ->
                val deckId = OnboardingRoutes.parseDeckId(
                    backStackEntry.arguments?.getString(OnboardingRoutes.DeckIdArg),
                )
                if (deckId != null) {
                    ThemeDetailsScreen(
                        deckId = deckId,
                        onBack = { navController.popBackStack() },
                        onOpenPremiumPromo = { navController.navigate(OnboardingRoutes.PremiumPromoBack) },
                        onBuy = { product ->
                            navController.navigate(
                                OnboardingRoutes.purchase(
                                    productId = product.id,
                                    title = product.title,
                                    priceRub = product.priceRub,
                                )
                            )
                        },
                    )
                }
            }

            composable(OnboardingRoutes.PurchasePattern) { backStackEntry ->
                val productId = OnboardingRoutes.parsePurchaseProductId(
                    backStackEntry.arguments?.getString(OnboardingRoutes.PurchaseProductIdArg),
                )
                val productTitle = OnboardingRoutes.parsePurchaseTitle(
                    backStackEntry.arguments?.getString(OnboardingRoutes.PurchaseTitleArg),
                )
                val productPrice = OnboardingRoutes.parsePurchasePrice(
                    backStackEntry.arguments?.getString(OnboardingRoutes.PurchasePriceArg),
                )

                if (productId == null || productPrice == null) {
                    LaunchedEffect(Unit) {
                        navController.popBackStack()
                    }
                } else {
                    PurchaseScreen(
                        productId = productId,
                        productTitle = productTitle.ifBlank { "Покупка" },
                        priceRub = productPrice,
                        onBack = { navController.popBackStack() },
                        onRequireRegistration = {
                            navController.navigate(OnboardingRoutes.Register)
                        },
                        onPurchased = { purchaseResult ->
                            scope.launch {
                                if (purchaseResult.productType == ProductType.PREMIUM) {
                                    isPremiumUser = true
                                    authRepository.setIsPremium(true)
                                }
                                homeRefreshSignal += 1
                                Toast.makeText(
                                    context,
                                    "Поздравляем! Покупка прошла успешно",
                                    Toast.LENGTH_SHORT,
                                ).show()
                                navController.navigate(OnboardingRoutes.Home) {
                                    popUpTo(OnboardingRoutes.Home) { inclusive = false }
                                    launchSingleTop = true
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}
