package com.example.triviaapp.ui.screens.createquiz

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import com.example.triviaapp.ui.models.Category
import com.example.triviaapp.ui.models.CategoryOption
import com.example.triviaapp.ui.models.DifficultyOption

@Composable
fun CreateQuizFirstScreen(
    viewModel: CreateQuizFirstScreenViewModel,
    onNavigateToCreateQuizSecondScreen: (Int, String, Int) -> Unit
) {
    val state by viewModel.stateFlow.collectAsState(CreateQuizFirstScreenState())
    CreateQuizFirstScreen(
        categoriesOptions = state.categoriesOptions,
        difficultiesOptions = DifficultyOption.values().toList(),
        selectedCategoryOption = state.selectedCategoryOption,
        selectedDifficultyOption = state.selectedDifficultyOption,
        isCategoryListExpanded = state.isCategoryListExpanded,
        isDifficultyListExpanded = state.isDifficultiesListExpanded,
        onDismissCategoryList = { viewModel.setCategoryListExpandedState(false) },
        onDismissDifficultyList = { viewModel.setDifficultyListExpandedState(false) },
        onExpandedCategoryList = { viewModel.setCategoryListExpandedState(true) },
        onExpandedDifficultyList = { viewModel.setDifficultyListExpandedState(true) },
        onSelectedCategoryOption = { categoryOption ->
            viewModel.onCategorySelected(categoryOption)
            viewModel.setCategoryListExpandedState(false)
       },
        onSelectedDifficultyOption = { difficultyOption ->
            viewModel.onDifficultySelected(difficultyOption)
            viewModel.setDifficultyListExpandedState(false)
        },
        onNextClick = {
            val categoryId = viewModel.getSelectedCategory()?.id ?: 0
            val difficulty = viewModel.getSelectedDifficulty().name
            val numQuestions = viewModel.getMaxNumOfQuestions()
            onNavigateToCreateQuizSecondScreen(categoryId, difficulty, numQuestions)
        }
    )
}

@Composable
fun CreateQuizFirstScreen(
    categoriesOptions: List<CategoryOption>,
    difficultiesOptions: List<DifficultyOption>,
    selectedCategoryOption: CategoryOption,
    selectedDifficultyOption: DifficultyOption,
    isCategoryListExpanded: Boolean,
    isDifficultyListExpanded: Boolean,
    onDismissCategoryList: () -> Unit,
    onExpandedCategoryList: () -> Unit,
    onDismissDifficultyList: () -> Unit,
    onExpandedDifficultyList: () -> Unit,
    onSelectedCategoryOption: (CategoryOption) -> Unit,
    onSelectedDifficultyOption: (DifficultyOption) -> Unit,
    onNextClick: () -> Unit
) {
    Column {
        CategoryRow(
            categoriesOptions = categoriesOptions,
            expanded = isCategoryListExpanded,
            selectedCategoryOption = selectedCategoryOption,
            onDismissRequest = onDismissCategoryList,
            onExpanded = onExpandedCategoryList,
            onSelect = onSelectedCategoryOption
        )
        DifficultyRow(
            difficultiesOptions = difficultiesOptions,
            expanded = isDifficultyListExpanded,
            selectedDifficultyOption = selectedDifficultyOption,
            onDismissRequest = onDismissDifficultyList,
            onExpanded = onExpandedDifficultyList,
            onSelect = onSelectedDifficultyOption
        )
        Button(
            onClick = onNextClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(text = stringResource(R.string.next))
        }
    }
}

@Composable
fun DifficultyDropDownMenu(
    difficultiesOptions: List<DifficultyOption> = DifficultyOption.values().toList(),
    expanded: Boolean,
    selectedDifficultyOption: DifficultyOption,
    onDismissRequest: () -> Unit,
    onExpanded: () -> Unit,
    onSelect: (DifficultyOption) -> Unit,
    modifier: Modifier
) {
    Box(modifier = Modifier) {
        Row(
            modifier = Modifier
                .clickable { onExpanded() }
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(selectedDifficultyOption.textId),
                style = MaterialTheme.typography.h6
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Difficulty Drop Down Icon"
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
        ) {
            difficultiesOptions.forEach { difficultyOption ->
                DropdownMenuItem(
                    onClick = { onSelect(difficultyOption) }
                ) {
                    Text(text = stringResource(difficultyOption.textId))
                }
            }
        }
    }

}

@Composable
fun DifficultyRow(
    difficultiesOptions: List<DifficultyOption> = DifficultyOption.values().toList(),
    expanded: Boolean,
    selectedDifficultyOption: DifficultyOption,
    onDismissRequest: () -> Unit,
    onExpanded: () -> Unit,
    onSelect: (DifficultyOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.padding(5.dp)
    ) {
       Text(
           text = stringResource(R.string.difficulty),
           style = MaterialTheme.typography.h6,
           modifier = Modifier
               .padding(10.dp)
               .align(alignment = Alignment.CenterVertically)
       )
        DifficultyDropDownMenu(
            difficultiesOptions,
            expanded,
            selectedDifficultyOption,
            onDismissRequest,
            onExpanded,
            onSelect,
            modifier
        )
    }
}

@Composable
fun CategoryDropDownMenu(
    categoriesOptions: List<CategoryOption>,
    expanded: Boolean,
    selectedCategoryOption: CategoryOption,
    onDismissRequest: () -> Unit,
    onExpanded: () -> Unit,
    onSelect: (CategoryOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = Modifier) {
        Row(
            modifier = Modifier
                .clickable { onExpanded() }
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                .padding(10.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = when (selectedCategoryOption) {
                    CategoryOption.Any -> stringResource(R.string.any)
                    is CategoryOption.ConcreteCategory -> selectedCategoryOption.category.name
                },
                style = MaterialTheme.typography.h6,
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Drop Down Category Icon"
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = onDismissRequest,
            modifier = modifier
        ) {
            categoriesOptions.forEach { categoryOption ->
                DropdownMenuItem(
                    onClick = { onSelect(categoryOption) }
                ) {
                    val text = when (categoryOption) {
                        CategoryOption.Any -> stringResource(R.string.any)
                        is CategoryOption.ConcreteCategory -> categoryOption.category.name
                    }
                    Text(text = text)
                }
            }
        }
    }
}

@Composable
fun CategoryRow(
    categoriesOptions: List<CategoryOption>,
    expanded: Boolean,
    selectedCategoryOption: CategoryOption,
    onDismissRequest: () -> Unit,
    onExpanded: () -> Unit,
    onSelect: (CategoryOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier.padding(5.dp)
    ) {
       Text(
           text = stringResource(R.string.category),
           style = MaterialTheme.typography.h6,
           modifier = Modifier
               .padding(10.dp)
               .align(alignment = Alignment.CenterVertically)
       )
        CategoryDropDownMenu(
            categoriesOptions,
            expanded,
            selectedCategoryOption,
            onDismissRequest,
            onExpanded,
            onSelect,
            modifier
        )
    }
}

@Preview
@Composable
fun CreateQuizFirstScreenPreview() {
    CreateQuizFirstScreen(
        categoriesOptions = listOf(
            CategoryOption.Any,
            CategoryOption.ConcreteCategory(
                Category(
                    id = 1,
                    name = "Science",
                    numOfQuestions = 100,
                    numOfEasyQuestions = 42,
                    numOfMediumQuestions = 28,
                    numOfHardQuestions = 30
                )
            ),
            CategoryOption.ConcreteCategory(
                Category(
                    id = 2,
                    name = "Movies",
                    numOfQuestions = 100,
                    numOfEasyQuestions = 42,
                    numOfMediumQuestions = 28,
                    numOfHardQuestions = 30
                )
            )
        ),
        difficultiesOptions = DifficultyOption.values().toList(),
        selectedCategoryOption = CategoryOption.Any,
        selectedDifficultyOption = DifficultyOption.Any,
        isCategoryListExpanded = false,
        isDifficultyListExpanded = false,
        onDismissCategoryList = {},
        onDismissDifficultyList = {},
        onExpandedCategoryList = {},
        onExpandedDifficultyList = {},
        onSelectedCategoryOption = {},
        onSelectedDifficultyOption = {},
        onNextClick = {}
    )
}