package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.ProductType

internal data class HomeShowcaseUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val products: List<Product> = emptyList(),
)

internal val ShowcaseGradients = listOf(
    listOf(Color(0xFF37C5FF), Color(0xFF2E8CF6)),
    listOf(Color(0xFFFF9A1D), Color(0xFFFF6D00)),
    listOf(Color(0xFFFFE447), Color(0xFFFFC700)),
    listOf(Color(0xFF3DDF88), Color(0xFF17B962)),
    listOf(Color(0xFFFF7F92), Color(0xFFFF6578)),
    listOf(Color(0xFF9E67FF), Color(0xFF6F1BFF)),
)

@Composable
internal fun HomeTopBar(
    isPremiumUser: Boolean,
    onProfileClick: () -> Unit,
    onStandardClick: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .width(148.dp)
                .height(58.dp)
                .then(
                    if (isPremiumUser) {
                        Modifier
                    } else {
                        Modifier.clickable { onStandardClick() }
                    }
                ),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painterResource(R.drawable.button_pink),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.FillBounds,
            )
            Text(
                text = if (isPremiumUser) "Премиум" else "Стандарт",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 18.sp,
                ),
                color = Color(0xFFD32A8E),
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFF6AA7FF),
                            Color(0xFF2956F4),
                        )
                    )
                )
                .clickable { onProfileClick() },
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(28.dp)) {
                drawCircle(
                    color = Color(0xFF0D45EC),
                    radius = size.minDimension * 0.2f,
                    center = Offset(size.width / 2f, size.height * 0.3f),
                )
                drawRoundRect(
                    color = Color(0xFF0D45EC),
                    topLeft = Offset(size.width * 0.18f, size.height * 0.52f),
                    size = Size(size.width * 0.64f, size.height * 0.38f),
                )
            }
        }
    }
}

@Composable
internal fun HomeQuestionCard(onStartGame: () -> Unit) {
    val shape = RoundedCornerShape(32.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        Color(0xFFD5EAF7),
                        Color(0xFFCFE5F4),
                    )
                )
            )
            .border(
                width = 2.dp,
                color = Color.White.copy(alpha = 0.75f),
                shape = shape,
            )
            .padding(horizontal = 16.dp, vertical = 18.dp),
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Что ты\nвыберешь",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 30.sp,
                        lineHeight = 32.sp,
                    ),
                    color = Color(0xFF07090B),
                    textAlign = TextAlign.Center,
                )
            }
            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Быть высокой\nсобакой с низкими\nпоступками",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF4C67B5),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "или",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                ),
                color = Color(0xFF4C67B5),
                modifier = Modifier.padding(vertical = 2.dp),
            )
            Text(
                text = "Быть низкой\nсобакой с благородными\nпомыслами",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF4C67B5),
                textAlign = TextAlign.Center,
            )

            Image(
                painter = painterResource(R.drawable.screensaver_mascot),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth(0.56f)
                    .offset(y = 6.dp),
                contentScale = ContentScale.FillWidth,
            )

            GreenCloudButton(
                text = "Начать выбирать",
                onClick = onStartGame,
                modifier = Modifier.fillMaxWidth(0.94f),
            )
        }
    }
}

@Composable
internal fun PremiumOpenedLabel() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White.copy(alpha = 0.30f))
            .border(
                width = 1.dp,
                color = Color.White.copy(alpha = 0.7f),
                shape = RoundedCornerShape(20.dp),
            )
            .padding(vertical = 16.dp),
    ) {
        Text(
            text = "Все темы открыты",
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 24.sp,
                lineHeight = 26.sp,
            ),
            color = Color(0xFF3653A8),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
internal fun PaidThemeRow(
    product: Product,
    colors: List<Color>,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(999.dp))
            .background(Brush.horizontalGradient(colors))
            .clickable { onClick() }
            .padding(horizontal = 12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            SmallLockIcon()
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = product.title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF1E2A63),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${product.priceRub} ₽",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 18.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF1E2A63),
            )
        }
    }
}

@Composable
private fun SmallLockIcon() {
    Canvas(modifier = Modifier.size(12.dp)) {
        drawArc(
            color = Color(0xFF101010),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(size.width * 0.2f, size.height * 0.03f),
            size = Size(size.width * 0.6f, size.height * 0.62f),
            style = Stroke(width = 2f, cap = StrokeCap.Round),
        )
        drawRoundRect(
            color = Color(0xFF101010),
            topLeft = Offset(size.width * 0.16f, size.height * 0.42f),
            size = Size(size.width * 0.68f, size.height * 0.5f),
        )
    }
}

internal fun filterPaidDeckProducts(
    products: List<Product>,
    accessibleDecks: List<Deck>,
): List<Product> {
    val accessibleDeckIds = accessibleDecks.map { it.id }.toSet()
    return products.filter { product ->
        product.active &&
            product.type == ProductType.DECK &&
            product.deckId != null &&
            product.deckId !in accessibleDeckIds
    }
}
