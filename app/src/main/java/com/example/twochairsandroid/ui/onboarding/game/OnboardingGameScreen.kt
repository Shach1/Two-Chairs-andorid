package com.example.twochairsandroid.ui.onboarding

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.twochairsandroid.R
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.AnswerOption
import com.example.twochairsandroid.domain.model.AnswerStats
import com.example.twochairsandroid.domain.model.MyDeckPick
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.ProductType
import com.example.twochairsandroid.domain.model.Question
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
internal fun GameScreen(
    deckId: Long,
    onOpenOtherDecks: () -> Unit,
    onOpenMenu: () -> Unit,
    onUnlockMyDeckFeature: (Product) -> Unit,
) {
    val context = LocalContext.current
    val deckRepository = remember(context) { context.appContainer.deckRepository }
    val gameRepository = remember(context) { context.appContainer.gameRepository }
    val myDeckRepository = remember(context) { context.appContainer.myDeckRepository }
    val storeRepository = remember(context) { context.appContainer.storeRepository }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(GameUiState(isLoadingQuestion = true)) }

    suspend fun loadQuestion(incrementCounter: Boolean) {
        uiState = uiState.copy(
            isLoadingQuestion = true,
            errorText = null,
            selectedAnswer = null,
            answerStats = null,
        )

        when (val result = gameRepository.getNextQuestion(deckId)) {
            is ApiResult.Success -> {
                val question = result.data
                if (question == null) {
                    uiState = uiState.copy(
                        isLoadingQuestion = false,
                        question = null,
                        errorText = "В этой колоде пока нет вопросов",
                    )
                } else {
                    val nextNumber = when {
                        uiState.questionNumber <= 0 -> 1
                        incrementCounter -> uiState.questionNumber + 1
                        else -> uiState.questionNumber
                    }
                    uiState = uiState.copy(
                        isLoadingQuestion = false,
                        question = question,
                        questionNumber = nextNumber,
                    )
                }
            }

            is ApiResult.Error -> {
                uiState = uiState.copy(
                    isLoadingQuestion = false,
                    errorText = result.error.message,
                )
            }
        }
    }

    suspend fun showDeckPicker(forceReload: Boolean = false) {
        if (uiState.pickerItems.isNotEmpty() && !forceReload) {
            uiState = uiState.copy(
                pickerVisible = true,
                pickerErrorText = null,
            )
            return
        }

        uiState = uiState.copy(
            pickerVisible = true,
            pickerLoading = true,
            pickerErrorText = null,
        )
        when (val result = myDeckRepository.getDeckPicker()) {
            is ApiResult.Success -> {
                uiState = uiState.copy(
                    pickerLoading = false,
                    pickerItems = result.data,
                )
            }

            is ApiResult.Error -> {
                uiState = uiState.copy(
                    pickerLoading = false,
                    pickerErrorText = result.error.message,
                )
            }
        }
    }

    LaunchedEffect(deckId) {
        uiState = uiState.copy(isLoadingQuestion = true)
        coroutineScope {
            val decksDeferred = async { deckRepository.getAccessibleDecks() }
            val canCreateDeferred = async { myDeckRepository.canCreateDecks() }
            val productsDeferred = async { storeRepository.getProducts() }

            when (val decksResult = decksDeferred.await()) {
                is ApiResult.Success -> {
                    val title = decksResult.data.firstOrNull { it.id == deckId }?.title
                    if (!title.isNullOrBlank()) {
                        uiState = uiState.copy(deckTitle = title)
                    }
                }

                is ApiResult.Error -> Unit
            }

            when (val canCreateResult = canCreateDeferred.await()) {
                is ApiResult.Success -> uiState = uiState.copy(canManageMyDecks = canCreateResult.data)
                is ApiResult.Error -> Unit
            }

            when (val productsResult = productsDeferred.await()) {
                is ApiResult.Success -> {
                    uiState = uiState.copy(
                        unlockFeatureProduct = productsResult.data.firstOrNull {
                            it.type == ProductType.FEATURE_CREATE_DECKS
                        }
                    )
                }

                is ApiResult.Error -> Unit
            }
        }
        loadQuestion(incrementCounter = false)
    }

    BackHandler(enabled = true, onBack = onOpenOtherDecks)

    val question = uiState.question
    val selectedAnswer = uiState.selectedAnswer
    val answerStats = uiState.answerStats

    val topAlpha = when (selectedAnswer) {
        null, AnswerOption.A -> 1f
        AnswerOption.B -> 0.5f
    }
    val bottomAlpha = when (selectedAnswer) {
        null, AnswerOption.B -> 1f
        AnswerOption.A -> 0.5f
    }
    val topTextColor = if (selectedAnswer == AnswerOption.B) Color(0xFF86A9D8) else Color(0xFF5570B8)
    val bottomTextColor = if (selectedAnswer == AnswerOption.A) Color(0xFF82B779) else Color(0xFF3B8B20)

    val topText = buildString {
        append(question?.optionA.orEmpty())
        if (answerStats != null) {
            append("\n${answerStats.pctA} %")
        }
    }
    val bottomText = buildString {
        append(question?.optionB.orEmpty())
        if (answerStats != null) {
            append("\n${answerStats.pctB} %")
        }
    }

    TwoChairsBackground {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 12.dp),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .width(230.dp)
                            .height(68.dp)
                            .clip(RoundedCornerShape(999.dp))
                            .clickable { onOpenOtherDecks() },
                        contentAlignment = Alignment.Center,
                    ) {
                        androidx.compose.foundation.Image(
                            painter = painterResource(R.drawable.button_pink),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillBounds,
                        )
                        Text(
                            text = "Другие темы",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 24.sp,
                                lineHeight = 26.sp,
                            ),
                            color = Color(0xFFD3268D),
                        )
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    Text(
                        text = "В меню",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color(0xFF294BA2),
                        modifier = Modifier.clickable { onOpenMenu() },
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = uiState.deckTitle,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(460.dp),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    GameOptionCard(
                        imageRes = R.drawable.up_queston_case,
                        text = topText,
                        textColor = topTextColor,
                        alpha = topAlpha,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.62f),
                        onClick = {
                            if (question == null || selectedAnswer != null || uiState.isSubmittingAnswer) {
                                return@GameOptionCard
                            }
                            scope.launch {
                                uiState = uiState.copy(
                                    isSubmittingAnswer = true,
                                    errorText = null,
                                )
                                when (val result = gameRepository.submitAnswer(deckId, question.id, AnswerOption.A)) {
                                    is ApiResult.Success -> {
                                        uiState = uiState.copy(
                                            isSubmittingAnswer = false,
                                            selectedAnswer = AnswerOption.A,
                                            answerStats = result.data,
                                        )
                                    }

                                    is ApiResult.Error -> {
                                        uiState = uiState.copy(
                                            isSubmittingAnswer = false,
                                            errorText = result.error.message,
                                        )
                                    }
                                }
                            }
                        },
                    )

                    GameOptionCard(
                        imageRes = R.drawable.down_queston_case,
                        text = bottomText,
                        textColor = bottomTextColor,
                        alpha = bottomAlpha,
                        modifier = Modifier
                            .fillMaxWidth(0.97f)
                            .aspectRatio(1.52f)
                            .offset(y = 180.dp),
                        onClick = {
                            if (question == null || selectedAnswer != null || uiState.isSubmittingAnswer) {
                                return@GameOptionCard
                            }
                            scope.launch {
                                uiState = uiState.copy(
                                    isSubmittingAnswer = true,
                                    errorText = null,
                                )
                                when (val result = gameRepository.submitAnswer(deckId, question.id, AnswerOption.B)) {
                                    is ApiResult.Success -> {
                                        uiState = uiState.copy(
                                            isSubmittingAnswer = false,
                                            selectedAnswer = AnswerOption.B,
                                            answerStats = result.data,
                                        )
                                    }

                                    is ApiResult.Error -> {
                                        uiState = uiState.copy(
                                            isSubmittingAnswer = false,
                                            errorText = result.error.message,
                                        )
                                    }
                                }
                            }
                        },
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

                    if (uiState.isLoadingQuestion && question == null) {
                        Text(
                            text = "Загружаем вопрос...",
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontSize = 18.sp,
                                lineHeight = 22.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF294BA2),
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                }

                if (!uiState.errorText.isNullOrBlank()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = uiState.errorText.orEmpty(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 14.sp,
                            lineHeight = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color(0xFFD23A63),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    AddToMyDeckAction(
                        canManageMyDecks = uiState.canManageMyDecks,
                        enabled = true,
                        onClick = {
                            if (!uiState.canManageMyDecks) {
                                val unlockProduct = uiState.unlockFeatureProduct
                                if (unlockProduct != null) {
                                    onUnlockMyDeckFeature(unlockProduct)
                                } else {
                                    uiState = uiState.copy(
                                        errorText = "Не удалось загрузить стоимость разблокировки",
                                    )
                                }
                                return@AddToMyDeckAction
                            }
                            if (question == null) return@AddToMyDeckAction
                            scope.launch { showDeckPicker(forceReload = false) }
                        },
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.clickable(enabled = !uiState.isLoadingQuestion) {
                            scope.launch { loadQuestion(incrementCounter = true) }
                        },
                    ) {
                        Text(
                            text = if (selectedAnswer == null) "Пропустить\nвопрос" else "Следующий\nвопрос",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 19.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF294BA2),
                            textAlign = TextAlign.End,
                        )
                        Text(
                            text = " >",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 44.sp,
                                lineHeight = 44.sp,
                            ),
                            color = Color(0xFF294BA2),
                        )
                    }
                }
            }

            if (uiState.pickerVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.34f))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                        ) {
                            uiState = uiState.copy(pickerVisible = false)
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.88f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Color.White)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null,
                            ) {}
                            .padding(horizontal = 18.dp, vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Выберите колоду",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontSize = 22.sp,
                                lineHeight = 24.sp,
                            ),
                            color = Color(0xFF213D86),
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        when {
                            uiState.pickerLoading -> {
                                Text(
                                    text = "Загружаем список...",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color(0xFF213D86),
                                )
                            }

                            !uiState.pickerErrorText.isNullOrBlank() -> {
                                Text(
                                    text = uiState.pickerErrorText.orEmpty(),
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = Color(0xFFD23A63),
                                    textAlign = TextAlign.Center,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Повторить",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 16.sp,
                                        lineHeight = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                    ),
                                    color = Color(0xFF294BA2),
                                    modifier = Modifier.clickable {
                                        scope.launch { showDeckPicker(forceReload = true) }
                                    },
                                )
                            }

                            uiState.pickerItems.isEmpty() -> {
                                Text(
                                    text = "У вас пока нет своих колод",
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontSize = 14.sp,
                                        lineHeight = 18.sp,
                                    ),
                                    color = Color(0xFF213D86),
                                    textAlign = TextAlign.Center,
                                )
                            }

                            else -> {
                                LazyColumn(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(230.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp),
                                ) {
                                    items(uiState.pickerItems) { pick ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clip(RoundedCornerShape(999.dp))
                                                .background(Color(0xFFE8F2FA))
                                                .clickable {
                                                    val activeQuestion = uiState.question ?: return@clickable
                                                    uiState = uiState.copy(pickerVisible = false)
                                                    scope.launch {
                                                        when (val result = myDeckRepository.addExistingQuestion(pick.id, activeQuestion.id)) {
                                                            is ApiResult.Success -> {
                                                                Toast
                                                                    .makeText(context, "Вопрос добавлен в колоду", Toast.LENGTH_SHORT)
                                                                    .show()
                                                            }

                                                            is ApiResult.Error -> {
                                                                uiState = uiState.copy(errorText = result.error.message)
                                                            }
                                                        }
                                                    }
                                                }
                                                .padding(horizontal = 14.dp, vertical = 12.dp),
                                        ) {
                                            Text(
                                                text = pick.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontSize = 16.sp,
                                                    lineHeight = 20.sp,
                                                    fontWeight = FontWeight.Bold,
                                                ),
                                                color = Color(0xFF294BA2),
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Отмена",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 16.sp,
                                lineHeight = 20.sp,
                                fontWeight = FontWeight.Bold,
                            ),
                            color = Color(0xFF294BA2),
                            modifier = Modifier.clickable { uiState = uiState.copy(pickerVisible = false) },
                        )
                    }
                }
            }
        }
    }
}
