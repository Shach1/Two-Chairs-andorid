package com.example.twochairsandroid.ui.onboarding

import androidx.activity.compose.BackHandler
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.twochairsandroid.R
import com.example.twochairsandroid.core.di.appContainer
import com.example.twochairsandroid.core.network.ApiResult
import com.example.twochairsandroid.domain.model.Deck
import com.example.twochairsandroid.domain.model.Question
import kotlinx.coroutines.launch

internal data class MyDeckFeatureUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val canCreate: Boolean = false,
    val unlockPriceRub: Int? = null,
)

private data class MyDecksUiState(
    val isLoading: Boolean = false,
    val errorText: String? = null,
    val decks: List<Deck> = emptyList(),
)

private data class MyDeckEditorUiState(
    val isLoading: Boolean = false,
    val isSavingBack: Boolean = false,
    val isAddingQuestion: Boolean = false,
    val errorText: String? = null,
    val questions: List<Question> = emptyList(),
)

@Composable
internal fun MyDeckFeatureSection(
    uiState: MyDeckFeatureUiState,
    onRetry: () -> Unit,
    onOpenMyDecks: () -> Unit,
    onUnlock: () -> Unit,
) {
    when {
        uiState.isLoading -> {
            Text(
                text = "Загружаем режим своих колод...",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                ),
                color = Color(0xFF213D86),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        !uiState.errorText.isNullOrBlank() -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = uiState.errorText.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFFD23A63),
                    textAlign = TextAlign.Center,
                )
                Spacer(modifier = Modifier.height(8.dp))
                GreenCloudButton(
                    text = "Повторить",
                    onClick = onRetry,
                    modifier = Modifier.fillMaxWidth(0.62f),
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(28.dp))
                    .background(Color(0xFF67BEEB))
                    .padding(top = 14.dp, start = 10.dp, end = 10.dp, bottom = 16.dp),
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Супер\nрежим",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 30.sp,
                                    lineHeight = 32.sp,
                                ),
                                color = Color(0xFF07090B),
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Развлекайтесь\nкомпанией.\nИсследуйте\nсебя с улетом",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = Color(0xFF07101B),
                            )
                        }
                        Image(
                            painter = painterResource(R.drawable.mascot_my_deck),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.5f)
                                .padding(top = 4.dp),
                            contentScale = ContentScale.FillWidth,
                        )
                    }

                    Image(
                        painter = painterResource(R.drawable.green_star),
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(start = 10.dp),
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(28.dp))
                            .background(
                                brush = Brush.verticalGradient(
                                    listOf(
                                        Color(0xFFD5EAF7),
                                        Color(0xFFCCE4F3),
                                    )
                                )
                            )
                            .border(
                                width = 1.dp,
                                color = Color.White.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(28.dp),
                            )
                            .padding(16.dp),
                    ) {
                        Column {
                            if (!uiState.canCreate) {
                                SmallLockMarker()
                            }
                            Text(
                                text = "Своя\nколода",
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    fontSize = 38.sp,
                                    lineHeight = 36.sp,
                                ),
                                color = Color(0xFF07090B),
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "создавайте свои колоды, чтобы\nузнать о своих друзьях всё самое\nсекретное",
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = Color(0xFF0F1D2F),
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(1.dp)) {
                                Image(
                                    painter = painterResource(R.drawable.mascot_card),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .weight(1f),
                                    contentScale = ContentScale.FillHeight,
                                )
                                Image(
                                    painter = painterResource(R.drawable.second_card),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .weight(1f),
                                    contentScale = ContentScale.FillHeight,
                                )
                                Image(
                                    painter = painterResource(R.drawable.third_card),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .height(120.dp)
                                        .weight(1f),
                                    contentScale = ContentScale.FillHeight,
                                )
                            }
                            Spacer(modifier = Modifier.height(14.dp))
                            MyDeckActionButton(
                                text = if (uiState.canCreate) {
                                    "Мои колоды"
                                } else {
                                    "Разблокировать ${uiState.unlockPriceRub ?: "..."} ₽"
                                },
                                onClick = if (uiState.canCreate) onOpenMyDecks else onUnlock,
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SmallLockMarker() {
    Canvas(modifier = Modifier.size(14.dp)) {
        drawArc(
            color = Color(0xFF101010),
            startAngle = 180f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.2f, size.height * 0.03f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.6f, size.height * 0.62f),
            style = Stroke(width = 2f, cap = StrokeCap.Round),
        )
        drawRoundRect(
            color = Color(0xFF101010),
            topLeft = androidx.compose.ui.geometry.Offset(size.width * 0.16f, size.height * 0.42f),
            size = androidx.compose.ui.geometry.Size(size.width * 0.68f, size.height * 0.5f),
        )
    }
}

@Composable
private fun MyDeckActionButton(
    text: String,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp)
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
                fontSize = 22.sp,
                lineHeight = 24.sp,
            ),
            color = Color(0xFFD3268D),
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 14.dp),
        )
    }
}

@Composable
internal fun MyDecksScreen(
    onBack: () -> Unit,
    onOpenDeckEditor: (Long) -> Unit,
    onCreateDeck: () -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val myDeckRepository = remember(context) { context.appContainer.myDeckRepository }
    val scope = rememberCoroutineScope()
    var uiState by remember { mutableStateOf(MyDecksUiState(isLoading = true)) }

    val reload: () -> Unit = {
        scope.launch {
            uiState = MyDecksUiState(isLoading = true)
            when (val result = myDeckRepository.getMyDecks()) {
                is ApiResult.Success -> uiState = MyDecksUiState(
                    isLoading = false,
                    decks = result.data,
                )

                is ApiResult.Error -> uiState = MyDecksUiState(
                    isLoading = false,
                    errorText = result.error.message,
                )
            }
        }
    }

    LaunchedEffect(Unit) { reload() }
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                reload()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    TwoChairsBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
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
            }
            item {
                Text(
                    text = "Мои колоды",
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 48.sp,
                        lineHeight = 46.sp,
                    ),
                    color = Color(0xFF07090B),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                )
            }

            when {
                uiState.isLoading -> {
                    item {
                        Text(
                            text = "Загружаем колоды...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF213D86),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }

                !uiState.errorText.isNullOrBlank() -> {
                    item {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = uiState.errorText.orEmpty(),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = Color(0xFFD23A63),
                                textAlign = TextAlign.Center,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            GreenCloudButton(
                                text = "Повторить",
                                onClick = reload,
                                modifier = Modifier.fillMaxWidth(0.62f),
                            )
                        }
                    }
                }

                else -> {
                    items(uiState.decks) { deck ->
                        MyDeckRow(
                            deck = deck,
                            onEdit = { onOpenDeckEditor(deck.id) },
                        )
                    }
                    item {
                        NewDeckRow(onClick = onCreateDeck)
                    }
                }
            }
        }
    }
}

@Composable
private fun MyDeckRow(
    deck: Deck,
    onEdit: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFF0F7FC), Color(0xFFE4F0F8))
                )
            ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = deck.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                ),
                color = Color(0xFF294BA2),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Image(
                painter = painterResource(R.drawable.edit),
                contentDescription = null,
                modifier = Modifier
                    .size(28.dp)
                    .clickable { onEdit() },
            )
        }
    }
}

@Composable
private fun NewDeckRow(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(92.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(Color(0xFFB9DDF2))
            .clickable { onClick() },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Новая колода",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    lineHeight = 24.sp,
                ),
                color = Color(0xFF4168B8),
                modifier = Modifier.weight(1f),
            )
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC8E3F3)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 38.sp,
                        lineHeight = 38.sp,
                    ),
                    color = Color(0xFF4168B8),
                )
            }
        }
    }
}

@Composable
internal fun MyDeckEditorScreen(
    deckId: Long?,
    onBack: () -> Unit,
) {
    val context = LocalContext.current
    val myDeckRepository = remember(context) { context.appContainer.myDeckRepository }
    val scope = rememberCoroutineScope()

    var uiState by remember { mutableStateOf(MyDeckEditorUiState(isLoading = deckId != null)) }
    var currentDeckId by rememberSaveable { mutableStateOf(deckId) }
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var questionOptionA by rememberSaveable { mutableStateOf("") }
    var questionOptionB by rememberSaveable { mutableStateOf("") }

    suspend fun loadQuestions(targetDeckId: Long) {
        when (val result = myDeckRepository.getDeckQuestions(targetDeckId)) {
            is ApiResult.Success -> {
                uiState = uiState.copy(
                    isLoading = false,
                    questions = result.data,
                )
            }

            is ApiResult.Error -> {
                uiState = uiState.copy(
                    isLoading = false,
                    errorText = result.error.message,
                )
            }
        }
    }

    suspend fun ensureDeckExists(): Long? {
        currentDeckId?.let { return it }
        val createResult = myDeckRepository.createDeck(
            title = title.trim().ifBlank { "Новая колода" },
            description = description.trim().ifBlank { null },
            ageRating = 18,
        )
        return when (createResult) {
            is ApiResult.Success -> {
                currentDeckId = createResult.data.id
                currentDeckId
            }

            is ApiResult.Error -> {
                uiState = uiState.copy(errorText = createResult.error.message)
                null
            }
        }
    }

    val saveAndGoBack: () -> Unit = {
        scope.launch {
            if (uiState.isLoading) {
                onBack()
                return@launch
            }
            uiState = uiState.copy(isSavingBack = true, errorText = null)

            val normalizedTitle = title.trim()
            val normalizedDescription = description.trim().ifBlank { null }
            val existingDeckId = currentDeckId

            val saveResult: ApiResult<*> = if (existingDeckId != null) {
                myDeckRepository.updateDeck(
                    deckId = existingDeckId,
                    title = normalizedTitle.ifBlank { "Новая колода" },
                    description = normalizedDescription,
                    ageRating = null,
                )
            } else {
                if (normalizedTitle.isBlank() && normalizedDescription.isNullOrBlank() && uiState.questions.isEmpty()) {
                    uiState = uiState.copy(isSavingBack = false)
                    onBack()
                    return@launch
                }
                myDeckRepository.createDeck(
                    title = normalizedTitle.ifBlank { "Новая колода" },
                    description = normalizedDescription,
                    ageRating = 18,
                )
            }

            when (saveResult) {
                is ApiResult.Success -> {
                    uiState = uiState.copy(isSavingBack = false)
                    onBack()
                }

                is ApiResult.Error -> {
                    uiState = uiState.copy(
                        isSavingBack = false,
                        errorText = saveResult.error.message,
                    )
                }
            }
        }
    }

    LaunchedEffect(deckId) {
        if (deckId == null) {
            uiState = uiState.copy(isLoading = false, questions = emptyList())
            return@LaunchedEffect
        }

        uiState = uiState.copy(isLoading = true, errorText = null)
        when (val deckResult = myDeckRepository.getMyDecks()) {
            is ApiResult.Success -> {
                val deck = deckResult.data.firstOrNull { it.id == deckId }
                if (deck == null) {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorText = "Колода не найдена",
                    )
                    return@LaunchedEffect
                }
                title = deck.title
                description = deck.description.orEmpty()
                loadQuestions(deckId)
            }

            is ApiResult.Error -> {
                uiState = uiState.copy(
                    isLoading = false,
                    errorText = deckResult.error.message,
                )
            }
        }
    }

    BackHandler(enabled = true) {
        if (!uiState.isSavingBack) {
            saveAndGoBack()
        }
    }

    TwoChairsBackground {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            item {
                Text(
                    text = "Назад",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                    modifier = Modifier.clickable {
                        if (!uiState.isSavingBack) {
                            saveAndGoBack()
                        }
                    },
                )
            }

            item {
                Text(
                    text = title.ifBlank { "Новая колода" },
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontSize = 38.sp,
                        lineHeight = 40.sp,
                    ),
                    color = Color(0xFF07090B),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Все изменения сохраняются\nавтоматически",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (!uiState.errorText.isNullOrBlank()) {
                item {
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
            }

            item {
                Text(
                    text = "Название",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                )
                Spacer(modifier = Modifier.height(6.dp))
                DeckEditorInputField(
                    value = title,
                    placeholder = "Название колоды",
                    onValueChange = { title = it },
                )
            }

            item {
                Text(
                    text = "Описание",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                )
                Spacer(modifier = Modifier.height(6.dp))
                DeckEditorInputField(
                    value = description,
                    placeholder = "Описание колоды",
                    onValueChange = { description = it },
                )
            }

            item {
                Text(
                    text = "Что ты выберешь...",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                )
                Spacer(modifier = Modifier.height(6.dp))
                DeckEditorInputField(
                    value = questionOptionA,
                    placeholder = "Ввести вопрос 1",
                    onValueChange = { questionOptionA = it },
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "или",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                )
                Spacer(modifier = Modifier.height(6.dp))
                DeckEditorInputField(
                    value = questionOptionB,
                    placeholder = "Ввести вопрос 2",
                    onValueChange = { questionOptionB = it },
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center,
                ) {
                    GreenCloudButton(
                        text = if (uiState.isAddingQuestion) "Добавляем..." else "Добавить",
                        onClick = {
                            if (uiState.isAddingQuestion) return@GreenCloudButton
                            val optionA = questionOptionA.trim()
                            val optionB = questionOptionB.trim()
                            if (optionA.isBlank() || optionB.isBlank()) {
                                uiState = uiState.copy(errorText = "Заполните оба варианта вопроса")
                                return@GreenCloudButton
                            }

                            scope.launch {
                                uiState = uiState.copy(isAddingQuestion = true, errorText = null)
                                val targetDeckId = ensureDeckExists()
                                if (targetDeckId == null) {
                                    uiState = uiState.copy(isAddingQuestion = false)
                                    return@launch
                                }

                                when (val addResult = myDeckRepository.addQuestion(targetDeckId, optionA, optionB)) {
                                    is ApiResult.Success -> {
                                        questionOptionA = ""
                                        questionOptionB = ""
                                        loadQuestions(targetDeckId)
                                    }

                                    is ApiResult.Error -> {
                                        uiState = uiState.copy(errorText = addResult.error.message)
                                    }
                                }
                                uiState = uiState.copy(isAddingQuestion = false)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(0.62f),
                    )
                }
            }

            item {
                Text(
                    text = "Вопросы в колоде",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 16.sp,
                        lineHeight = 20.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    color = Color(0xFF294BA2),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            if (uiState.isLoading) {
                item {
                    Text(
                        text = "Загружаем вопросы...",
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                        color = Color(0xFF294BA2),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            } else {
                items(uiState.questions, key = { it.id }) { question ->
                    QuestionEditorRow(
                        question = question,
                        onRemove = {
                            val targetDeckId = currentDeckId ?: return@QuestionEditorRow
                            val previousQuestions = uiState.questions
                            uiState = uiState.copy(
                                questions = previousQuestions.filterNot { it.id == question.id },
                                errorText = null,
                            )
                            scope.launch {
                                when (val removeResult = myDeckRepository.removeQuestion(targetDeckId, question.id)) {
                                    is ApiResult.Success -> Unit
                                    is ApiResult.Error -> {
                                        uiState = uiState.copy(
                                            questions = previousQuestions,
                                            errorText = removeResult.error.message,
                                        )
                                    }
                                }
                            }
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun DeckEditorInputField(
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit,
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
        ),
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodyMedium.copy(
            fontSize = 18.sp,
            lineHeight = 22.sp,
            color = Color(0xFF1E2745),
            fontWeight = FontWeight.Bold,
        ),
        cursorBrush = SolidColor(Color(0xFF3A4FA1)),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(78.dp)
                    .clip(RoundedCornerShape(999.dp))
                    .background(Color.White),
                contentAlignment = Alignment.CenterStart,
            ) {
                if (value.isBlank()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 16.sp,
                            lineHeight = 20.sp,
                        ),
                        color = Color(0xFFA5A6AF),
                        modifier = Modifier.padding(start = 22.dp, end = 18.dp),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 22.dp, end = 18.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    innerTextField()
                }
            }
        },
    )
}

@Composable
private fun QuestionEditorRow(
    question: Question,
    onRemove: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(30.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(Color(0xFFF0F7FC), Color(0xFFE4F0F8))
                )
            )
            .padding(horizontal = 18.dp, vertical = 16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "${question.optionA} или ${question.optionB}",
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = Color(0xFF294BA2),
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = "×",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 30.sp,
                    lineHeight = 30.sp,
                ),
                color = Color(0xFF294BA2),
                modifier = Modifier.clickable { onRemove() },
            )
        }
    }
}
