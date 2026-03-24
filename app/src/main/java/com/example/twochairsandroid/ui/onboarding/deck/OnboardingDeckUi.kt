package com.example.twochairsandroid.ui.onboarding

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

internal data class DeckMenuUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val decks: List<Deck> = emptyList(),
)

internal data class ThemeDetailsUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val deck: Deck? = null,
    val product: Product? = null,
    val premiumPriceRub: Int? = null,
)

internal val DeckRowGradients = listOf(
    listOf(Color(0xFF37C5FF), Color(0xFF2E8CF6)),
    listOf(Color(0xFFFF9A1D), Color(0xFFFF6D00)),
    listOf(Color(0xFFFFE447), Color(0xFFFFC700)),
    listOf(Color(0xFF3DDF88), Color(0xFF17B962)),
    listOf(Color(0xFFFF7F92), Color(0xFFFF6578)),
    listOf(Color(0xFF9E67FF), Color(0xFF6F1BFF)),
)

internal fun mergeGameDecks(
    accessibleDecks: List<Deck>,
    myDecks: List<Deck>,
): List<Deck> {
    val knownIds = accessibleDecks.map { it.id }.toMutableSet()
    val extraMyDecks = myDecks.filter { knownIds.add(it.id) }
    return accessibleDecks + extraMyDecks
}

@Composable
internal fun HeaderRow(
    onBack: () -> Unit,
    onMenu: () -> Unit,
    showMenu: Boolean = true,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = "Назад",
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = Color(0xFF294BA2),
            modifier = Modifier.clickable { onBack() },
        )
        if (showMenu) {
            Text(
                text = "В меню",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF294BA2),
                modifier = Modifier.clickable { onMenu() },
            )
        } else {
            Spacer(modifier = Modifier.width(54.dp))
        }
    }
}

@Composable
internal fun DeckMenuRow(
    title: String,
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
            .padding(horizontal = 14.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF1E2A63),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.88f),
            )
        }
    }
}

@Composable
internal fun ThemeBuyButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(88.dp)
            .clip(RoundedCornerShape(999.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        Image(
            painter = painterResource(R.drawable.button_pink),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds,
        )
        Text(
            text = text,
            style = MaterialTheme.typography.titleMedium.copy(
                fontSize = 30.sp,
                lineHeight = 34.sp,
            ),
            color = Color(0xFFD3268D),
        )
    }
}
