package com.example.twochairsandroid.ui.onboarding

internal object OnboardingRoutes {
    const val DeckIdArg = "deckId"

    const val Splash = "splash"
    const val Rules = "rules"
    const val PremiumPromo = "premium_promo"
    const val PremiumPromoBack = "premium_promo_back"
    const val PurchaseStub = "purchase_stub"
    const val RegisterOffer = "register_offer"
    const val Register = "register"
    const val Login = "login"
    const val RegisterResult = "register_result"
    const val Home = "home"
    const val Profile = "profile"
    const val DeckMenu = "deck_menu"
    const val ThemeDetailsPattern = "theme_details/{$DeckIdArg}"

    fun themeDetails(deckId: Long): String = "theme_details/$deckId"
    fun parseDeckId(raw: String?): Long? = raw?.toLongOrNull()
}
