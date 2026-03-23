package com.example.twochairsandroid.ui.onboarding

import android.net.Uri

internal object OnboardingRoutes {
    const val DeckIdArg = "deckId"
    const val MyDeckIdArg = "myDeckId"
    const val PurchaseProductIdArg = "purchaseProductId"
    const val PurchaseTitleArg = "purchaseTitle"
    const val PurchasePriceArg = "purchasePrice"

    const val Splash = "splash"
    const val Rules = "rules"
    const val PremiumPromo = "premium_promo"
    const val PremiumPromoBack = "premium_promo_back"
    const val PurchasePattern =
        "purchase?${PurchaseProductIdArg}={${PurchaseProductIdArg}}&${PurchaseTitleArg}={${PurchaseTitleArg}}&${PurchasePriceArg}={${PurchasePriceArg}}"
    const val RegisterOffer = "register_offer"
    const val Register = "register"
    const val Login = "login"
    const val RegisterResult = "register_result"
    const val Home = "home"
    const val Profile = "profile"
    const val MyDecks = "my_decks"
    const val MyDeckEditorPattern = "my_deck_editor/{$MyDeckIdArg}"
    const val DeckMenu = "deck_menu"
    const val ThemeDetailsPattern = "theme_details/{$DeckIdArg}"

    fun themeDetails(deckId: Long): String = "theme_details/$deckId"
    fun parseDeckId(raw: String?): Long? = raw?.toLongOrNull()
    fun myDeckEditor(deckId: Long?): String = "my_deck_editor/${deckId ?: "new"}"
    fun parseMyDeckId(raw: String?): Long? = raw?.toLongOrNull()

    fun purchase(
        productId: Long,
        title: String,
        priceRub: Int,
    ): String = "purchase?${PurchaseProductIdArg}=$productId&${PurchaseTitleArg}=${Uri.encode(title)}&${PurchasePriceArg}=$priceRub"

    fun parsePurchaseProductId(raw: String?): Long? = raw?.toLongOrNull()
    fun parsePurchaseTitle(raw: String?): String = Uri.decode(raw.orEmpty())
    fun parsePurchasePrice(raw: String?): Int? = raw?.toIntOrNull()
}
