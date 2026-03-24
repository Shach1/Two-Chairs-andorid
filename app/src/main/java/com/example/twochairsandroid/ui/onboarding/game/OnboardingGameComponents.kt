package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
internal fun GameOptionCard(
    imageRes: Int,
    text: String,
    textColor: Color,
    alpha: Float,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .alpha(alpha)
            .clickable { onClick() },
    ) {
        androidx.compose.foundation.Image(
            painter = painterResource(imageRes),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 18.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Bold,
            ),
            textAlign = TextAlign.Center,
            color = textColor,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp),
        )
    }
}

@Composable
internal fun AddToMyDeckAction(
    canManageMyDecks: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .clickable(enabled = enabled) { onClick() }
            .padding(end = 8.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(62.dp)
                    .clip(CircleShape)
                    .background(Color(0xF7FFFFFF)),
                contentAlignment = Alignment.Center,
            ) {
                Canvas(modifier = Modifier.size(30.dp)) {
                    drawLine(
                        color = Color(0xFF3A4FA1),
                        start = androidx.compose.ui.geometry.Offset(size.width / 2f, 0f),
                        end = androidx.compose.ui.geometry.Offset(size.width / 2f, size.height),
                        strokeWidth = 3.5f,
                        cap = StrokeCap.Round,
                    )
                    drawLine(
                        color = Color(0xFF3A4FA1),
                        start = androidx.compose.ui.geometry.Offset(0f, size.height / 2f),
                        end = androidx.compose.ui.geometry.Offset(size.width, size.height / 2f),
                        strokeWidth = 3.5f,
                        cap = StrokeCap.Round,
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "В свою\nколоду",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 19.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF294BA2),
            )
        }

        if (!canManageMyDecks) {
            MiniLockIcon(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .offset(x = 2.dp, y = (-2).dp),
            )
        }
    }
}

@Composable
internal fun MiniLockIcon(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(16.dp)) {
        drawArc(
            color = Color(0xFF101010),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.03f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.6f, size.height * 0.62f),
            style = Stroke(width = 2.5f, cap = StrokeCap.Round),
        )
        drawRoundRect(
            color = Color(0xFF101010),
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.16f, size.height * 0.42f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.68f, size.height * 0.5f),
        )
    }
}
