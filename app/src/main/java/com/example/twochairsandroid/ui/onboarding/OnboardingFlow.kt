package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.twochairsandroid.R
import kotlinx.coroutines.delay

private object OnboardingRoutes {
    const val Splash = "splash"
    const val Rules = "rules"
    const val PremiumPromo = "premium_promo"
    const val PurchaseStub = "purchase_stub"
}

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
                    }
                )
            }
            composable(OnboardingRoutes.Rules) {
                RulesScreen(
                    onSkip = { navController.navigate(OnboardingRoutes.PremiumPromo) }
                )
            }
            composable(OnboardingRoutes.PremiumPromo) {
                PremiumPromoScreen(
                    onSkip = { /* перейдем к auth flow позже */ },
                    onBuy = { navController.navigate(OnboardingRoutes.PurchaseStub) },
                )
            }
            composable(OnboardingRoutes.PurchaseStub) {
                PurchaseStubScreen(navController)
            }
        }
    }
}

@Composable
private fun ScreensaverScreen(onFinished: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(1800)
        onFinished()
    }

    TwoChairsBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.screensaver_mascot),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.90f)
                    .aspectRatio(1f),
            )

            Spacer(modifier = Modifier.height(34.dp))

            Image(
                painter = painterResource(R.drawable.screensaver_logo),
                contentDescription = null,
                modifier = Modifier.fillMaxWidth(0.78f),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

@Composable
private fun RulesScreen(onSkip: () -> Unit) {
    TwoChairsBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-44).dp, y = 44.dp)
                    .size(160.dp)
                    .background(color = Color(0xFF5A7EFF), shape = CircleShape)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 18.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Простые\nправила",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color(0xFF07090B),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Листай карточки с вопросами,\nделай выбор из двух неоднозначных\nвариантов (влияющий на твою\nжизнь или нет))",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                    ),
                    color = Color(0xFF07101B),
                    textAlign = TextAlign.Center,
                )

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(460.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    RuleCard(
                        imageRes = R.drawable.up_queston_case,
                        text = "Стать\nантилопой\nи бороться\nс лопами",
                        textColor = Color(0xFF5570B8),
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.62f),
                    )

                    RuleCard(
                        imageRes = R.drawable.down_queston_case,
                        text = "Стать лопой\nи бороться\nс антилопами",
                        textColor = Color(0xFF3B8B20),
                        modifier = Modifier
                            .fillMaxWidth(0.97f)
                            .aspectRatio(1.52f)
                            .offset(y = 180.dp)
                    )

                    Box(
                        modifier = Modifier
                            .offset(y = 184.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFD5D5D6))
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) {
                        Text(
                            text = "Или",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 30.sp,
                                lineHeight = 30.sp,
                                fontWeight = FontWeight.Normal,
                            ),
                            color = Color(0xFF969696),
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 12.dp,
                            end = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Скип",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color(0xFF3A4FA1),
                        modifier = Modifier.clickable { onSkip() },
                    )
                    Text(
                        text = " >",
                        style = MaterialTheme.typography.titleLarge,
                        color = Color(0xFF3A4FA1),
                        modifier = Modifier.clickable { onSkip() },
                    )
                }
            }
        }
    }
}

@Composable
private fun PremiumPromoScreen(
    onSkip: () -> Unit,
    onBuy: () -> Unit,
) {
    TwoChairsBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(R.drawable.promo_logo),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(0.45f),
                        contentScale = ContentScale.FillWidth,
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Скип",
                            style = MaterialTheme.typography.titleMedium,
                            color = Color.White,
                            modifier = Modifier.clickable { onSkip() },
                        )
                        Text(
                            text = " >",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.White,
                            modifier = Modifier.clickable { onSkip() },
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Text(
                        text = "Супер\nпремиум\nрежим",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color(0xFF07090B),
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Image(
                        painter = painterResource(R.drawable.promo_heart),
                        contentDescription = null,
                        modifier = Modifier
                            .size(42.dp)
                            .offset(y = 8.dp, x = (-14).dp),
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                PromoFeatureCard(
                    title = "Открывает все все\nвсе колоды",
                    subtitle = "",
                    chips = listOf(
                        "Пикантная",
                        "Франция 1787 г",
                        "Работа и Офисный планктон",
                        "Картофель",
                        "Фантастические твари и где они обитают",
                    ),
                    badge = FeatureBadge.Lock,
                )
                Spacer(modifier = Modifier.height(10.dp))
                PromoFeatureCard(
                    title = "Интерактивный режим",
                    subtitle = "Сумасшедшие вопросы\n+ неожиданные задания",
                    badge = FeatureBadge.Check,
                )
                Spacer(modifier = Modifier.height(10.dp))
                PromoFeatureCard(
                    title = "Дает возможность\nсоздавать свои колоды",
                    subtitle = "",
                    badge = FeatureBadge.Check,
                )

                Spacer(modifier = Modifier.weight(1f))

                PromoBuyButton(onClick = onBuy)

                Spacer(modifier = Modifier.height(8.dp))
            }

            Image(
                painter = painterResource(R.drawable.promo_mascot),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .offset(x = 80.dp, y = 30.dp)
                    .fillMaxWidth(0.58f),
                contentScale = ContentScale.FillWidth,
            )
        }
    }
}

private enum class FeatureBadge {
    Lock,
    Check,
}

@Composable
private fun PromoFeatureCard(
    title: String,
    subtitle: String,
    chips: List<String> = emptyList(),
    badge: FeatureBadge,
) {
    Box {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFD8EBF8),
                            Color(0xFFCCE3F2),
                        )
                    )
                )
                .padding(start = 26.dp, end = 14.dp, top = 14.dp, bottom = 14.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF4A63AE),
            )
            if (chips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                PromoChipCloud(chips = chips)
            }
            if (subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 14.sp,
                        lineHeight = 18.sp,
                    ),
                    color = Color(0xFF4A63AE),
                )
            }
        }

        FeatureBadgeIcon(
            badge = badge,
            modifier = Modifier
                .size(26.dp)
                .offset(x = (-4).dp, y = (-2).dp),
        )
    }
}

@Composable
private fun PromoChipCloud(chips: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        chips.chunked(2).forEach { rowChips ->
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                rowChips.forEach { chip ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(999.dp))
                            .background(Color.White.copy(alpha = 0.3f))
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RoundedCornerShape(999.dp),
                            )
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = chip,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 12.sp,
                                lineHeight = 14.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF4A63AE),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureBadgeIcon(
    badge: FeatureBadge,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(Color(0xFF10D373)),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(16.dp)) {
            when (badge) {
                FeatureBadge.Check -> {
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.2f, size.height * 0.55f),
                        end = Offset(size.width * 0.44f, size.height * 0.78f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = Color.White,
                        start = Offset(size.width * 0.44f, size.height * 0.78f),
                        end = Offset(size.width * 0.82f, size.height * 0.28f),
                        strokeWidth = 3f,
                        cap = StrokeCap.Round,
                    )
                }

                FeatureBadge.Lock -> {
                    drawRoundRect(
                        color = Color.White,
                        topLeft = Offset(size.width * 0.2f, size.height * 0.46f),
                        size = Size(size.width * 0.6f, size.height * 0.42f),
                    )
                    drawArc(
                        color = Color.White,
                        startAngle = 180f,
                        sweepAngle = 180f,
                        useCenter = false,
                        topLeft = Offset(size.width * 0.27f, size.height * 0.13f),
                        size = Size(size.width * 0.46f, size.height * 0.52f),
                        style = Stroke(width = 3f),
                    )
                }
            }
        }
    }
}

@Composable
private fun PromoBuyButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1048f / 299f)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(R.drawable.button_pink),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )

        Row(
            modifier = Modifier.align(Alignment.Center),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Купить",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFD3268D),
            )
            Spacer(modifier = Modifier.size(12.dp))
            Text(
                text = "6704 Р",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFD758A1),
                textDecoration = TextDecoration.LineThrough,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "1990 Р",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFD3268D),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun RuleCard(
    imageRes: Int,
    text: String,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 18.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier.align(Alignment.Center),
        )
    }
}

@Composable
private fun PurchaseStubScreen(navController: NavHostController) {
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
                modifier = Modifier.clickable { navController.popBackStack() },
            )
        }
    }
}

@Composable
private fun TwoChairsBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF60BCEB),
                        Color(0xFF69C0EC),
                    )
                )
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            repeat(16) { index ->
                val x = (size.width / 15f) * (index % 6) + (index * 13f)
                val y = (size.height / 17f) * (index % 8) + (index * 20f)
                drawCircle(
                    color = Color(0x66A7D9F3),
                    radius = 26f + (index % 3) * 16f,
                    center = Offset(x, y),
                    style = Stroke(width = 4f),
                )
            }
        }

        content()
    }
}
