package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import kotlinx.coroutines.launch

@Composable
fun TwoChairsAppRoot() {
    val context = LocalContext.current
    val authRepository = remember(context) { context.appContainer.authRepository }
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    var isPremiumUser by remember { mutableStateOf(false) }
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
                    onBuy = { navController.navigate(OnboardingRoutes.PurchaseStub) },
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
                    onOpenPaidTheme = { navController.navigate(OnboardingRoutes.PurchaseStub) },
                    onOpenProfile = { navController.navigate(OnboardingRoutes.Profile) },
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

            composable(OnboardingRoutes.PurchaseStub) {
                PurchaseStubScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
