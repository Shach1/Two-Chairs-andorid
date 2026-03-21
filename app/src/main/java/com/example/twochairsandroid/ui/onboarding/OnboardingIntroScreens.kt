package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R
import kotlinx.coroutines.delay

@Composable
internal fun ScreensaverScreen(onFinished: () -> Unit) {
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
                    .fillMaxWidth(0.9f)
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
internal fun RulesScreen(onSkip: () -> Unit) {
    TwoChairsBackground {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .offset(x = (-44).dp, y = 44.dp)
                    .size(160.dp)
                    .background(color = Color(0xFF5A7EFF), shape = CircleShape),
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
                            .offset(y = 180.dp),
                    )

                    Box(
                        modifier = Modifier
                            .offset(y = 184.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFFD5D5D6))
                            .padding(horizontal = 24.dp, vertical = 10.dp),
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
                        .padding(bottom = 12.dp, end = 8.dp),
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
internal fun PremiumPromoScreen(
    onSkip: () -> Unit,
    onBuy: () -> Unit,
) {
    TwoChairsBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 14.dp, vertical = 12.dp),
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

@Composable
internal fun RegisterOfferScreen(onRegister: () -> Unit) {
    TwoChairsBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "Хочешь\nзарегистрироваться?",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF07090B),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(14.dp))
            Text(
                text = "вы сможете легче открыть прям\nвееееесь контент, который есть в игре",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
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
                    text = "Даааа\nтем более\nэто не сложно",
                    textColor = Color(0xFF111111),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.62f)
                        .clickable { onRegister() },
                )

                RuleCard(
                    imageRes = R.drawable.down_queston_case,
                    text = "Нет\nя не хочу",
                    textColor = Color(0xFF111111),
                    modifier = Modifier
                        .fillMaxWidth(0.97f)
                        .aspectRatio(1.52f)
                        .offset(y = 180.dp),
                )

                Box(
                    modifier = Modifier
                        .offset(y = 184.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFFD5D5D6))
                        .padding(horizontal = 24.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = "Или",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            lineHeight = 24.sp,
                            fontWeight = FontWeight.Normal,
                        ),
                        color = Color(0xFF969696),
                    )
                }

                Image(
                    painter = painterResource(R.drawable.mascot_offer_registration),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth(0.30f)
                        .align(Alignment.TopEnd)
                        .offset(x = (-12).dp, y = 56.dp)
                        .rotate(-8f),
                    contentScale = ContentScale.FillWidth,
                )
            }

            Spacer(modifier = Modifier.weight(1f))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp, end = 4.dp)
                    .clickable { onRegister() },
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Теперь уж точно\nначать отрываться",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 22.sp,
                        lineHeight = 26.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF3A4FA1),
                    textAlign = TextAlign.End,
                )
                Text(
                    text = " >",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF3A4FA1),
                )
            }
        }
    }
}
