package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TwoChairsAppRoot() {
    val navController = rememberNavController()

    Surface(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = OnboardingRoutes.Splash,
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
                    onRegistrationDone = { navController.navigate(OnboardingRoutes.RegisterResult) },
                )
            }

            composable(OnboardingRoutes.Login) {
                LoginScreen(
                    onBack = { navController.popBackStack() },
                    onNeedRegistration = { navController.navigate(OnboardingRoutes.Register) },
                    onLoginDone = { navController.navigate(OnboardingRoutes.PurchaseStub) },
                )
            }

            composable(OnboardingRoutes.RegisterResult) {
                RegisterResultScreen(onContinue = { navController.navigate(OnboardingRoutes.PurchaseStub) })
            }

            composable(OnboardingRoutes.PurchaseStub) {
                PurchaseStubScreen(onBack = { navController.popBackStack() })
            }
        }
    }
}
