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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R

internal const val AuthControlWidthFraction = 0.9f

internal enum class FeatureBadge {
    Lock,
    Check,
}

@Composable
internal fun TwoChairsBackground(content: @Composable BoxScope.() -> Unit) {
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

@Composable
internal fun RuleCard(
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
internal fun PromoFeatureCard(
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
                Spacer(modifier = Modifier.size(8.dp))
                PromoChipCloud(chips = chips)
            }
            if (subtitle.isNotBlank()) {
                Spacer(modifier = Modifier.size(6.dp))
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
internal fun PromoBuyButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1048f / 299f)
            .clickable { onClick() },
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
                text = "599 Р",
                style = MaterialTheme.typography.titleMedium,
                color = Color(0xFFD758A1),
                textDecoration = androidx.compose.ui.text.style.TextDecoration.LineThrough,
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "399 Р",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFFD3268D),
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
internal fun AuthTextField(
    value: String,
    placeholder: String,
    keyboardType: KeyboardType,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done,
        ),
        placeholder = {
            Text(
                text = placeholder,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                ),
                color = Color(0xFFA5A6AF),
            )
        },
        modifier = Modifier
            .fillMaxWidth(AuthControlWidthFraction)
            .clip(RoundedCornerShape(999.dp)),
        shape = RoundedCornerShape(999.dp),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF1E2745),
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = Color(0xFF3A4FA1),
        ),
    )
}

@Composable
internal fun GreenCloudButton(
    text: String,
    onClick: () -> Unit,
    enabled: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .aspectRatio(960f / 299f)
            .clickable(enabled = enabled) { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.button_green),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 18.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color(0xFF4E9C2D),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 24.dp),
        )
    }
}
