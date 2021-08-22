package com.example.triviaapp.ui.screens.createquiz

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.triviaapp.ui.models.CategoryOption
import com.example.triviaapp.ui.models.DifficultyOption
import com.example.triviaapp.ui.repositories.TriviaRepository
import com.example.triviaapp.ui.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateQuizFirstScreenViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
): ViewModel() {

    private val mutableStateFlow: MutableStateFlow<CreateQuizFirstScreenState> =
        MutableStateFlow(CreateQuizFirstScreenState())

    val stateFlow: Flow<CreateQuizFirstScreenState> = mutableStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            triviaRepository.getCategoriesFlow()
                .collect { categories ->
                    val categoriesOptions =
                        listOf(CategoryOption.Any) +
                                categories.map { CategoryOption.ConcreteCategory(it) }
                    mutableStateFlow.update { copy(categoriesOptions = categoriesOptions) }
                }
        }
    }

    fun onCategorySelected(categoryOption: CategoryOption) {
        mutableStateFlow.update { copy(selectedCategoryOption = categoryOption) }
    }

    fun onDifficultySelected(difficultyOption: DifficultyOption) {
        mutableStateFlow.update { copy(selectedDifficultyOption = difficultyOption) }
    }

    fun setCategoryListExpandedState(expanded: Boolean) {
        mutableStateFlow.update { copy(isCategoryListExpanded = expanded) }
    }

    fun setDifficultyListExpandedState(expanded: Boolean) {
        mutableStateFlow.update { copy(isDifficultiesListExpanded = expanded) }
    }
}