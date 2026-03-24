package com.example.twochairsandroid.ui.onboarding

import com.example.twochairsandroid.domain.model.AnswerOption
import com.example.twochairsandroid.domain.model.AnswerStats
import com.example.twochairsandroid.domain.model.MyDeckPick
import com.example.twochairsandroid.domain.model.Product
import com.example.twochairsandroid.domain.model.Question

internal data class GameUiState(
    val isLoadingQuestion: Boolean = false,
    val isSubmittingAnswer: Boolean = false,
    val errorText: String? = null,
    val deckTitle: String = "Обычная колода",
    val question: Question? = null,
    val questionNumber: Int = 0,
    val selectedAnswer: AnswerOption? = null,
    val answerStats: AnswerStats? = null,
    val canManageMyDecks: Boolean = false,
    val unlockFeatureProduct: Product? = null,
    val pickerVisible: Boolean = false,
    val pickerLoading: Boolean = false,
    val pickerErrorText: String? = null,
    val pickerItems: List<MyDeckPick> = emptyList(),
)
