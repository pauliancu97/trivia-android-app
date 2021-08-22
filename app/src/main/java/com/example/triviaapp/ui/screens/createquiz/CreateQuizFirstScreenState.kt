package com.example.triviaapp.ui.screens.createquiz

import com.example.triviaapp.ui.models.CategoryOption
import com.example.triviaapp.ui.models.DifficultyOption

data class CreateQuizFirstScreenState(
    val categoriesOptions: List<CategoryOption> = listOf(CategoryOption.Any),
    val selectedCategoryOption: CategoryOption = CategoryOption.Any,
    val isCategoryListExpanded: Boolean = false,
    val selectedDifficultyOption: DifficultyOption = DifficultyOption.Any,
    val isDifficultiesListExpanded: Boolean = false
)