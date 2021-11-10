package com.example.triviaapp.ui.screens.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
import com.example.triviaapp.ui.models.Difficulty
import com.example.triviaapp.ui.models.QuestionType
import com.example.triviaapp.ui.repositories.QuestionNotificationPeriodOption
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@Composable
fun QuestionNotificationSettingsScreen(
    viewModel: QuestionNotificationSettingsViewModel
) {
    val settingsState by viewModel.getQuestionNotificationSettings().collectAsState(
        initial = QuestionNotificationSettingsState()
    )
    val categoriesMenuExpanded by viewModel.categoriesMenuExpandedFlow.collectAsState(initial = false)
    val difficultyMenuExpanded by viewModel.difficultyMenuExpandedFlow.collectAsState(initial = false)
    val questionTypeMenuExpanded by viewModel.questionTypeMenuExpandedFlow.collectAsState(initial = false)
    val periodMenuExpanded by viewModel.periodMenuExpandedFlow.collectAsState(initial = false)
    QuestionNotificationSettingsScreen(
        isNotificationEnabled = settingsState.isNotificationEnabled,
        onNotificationEnabledChanged = { viewModel.setIsNotificationEnabled(it) },
        category = settingsState.category,
        onCategoryChanged = { viewModel.setCategory(it) },
        categories = settingsState.categories,
        isCategoriesMenuExpanded = categoriesMenuExpanded,
        onDismissCategoriesMenu = { viewModel.setCategoriesMenuExpandedState(false) },
        onExpandCategoriesMenu = { viewModel.setCategoriesMenuExpandedState(true) },
        difficulty = settingsState.difficulty,
        onDifficultyChanged = { viewModel.setDifficulty(it) },
        isDifficultyMenuExpanded = difficultyMenuExpanded,
        onExpandDifficultyMenu = { viewModel.setDifficultyMenuExpandedState(true) },
        onDismissDifficultyMenu = { viewModel.setDifficultyMenuExpandedState(false) },
        questionType = settingsState.questionType,
        onQuestionTypeChanged = { viewModel.setQuestionType(it) },
        isQuestionTypeMenuExpanded = questionTypeMenuExpanded,
        onExpandQuestionTypeMenu = { viewModel.setQuestionTypeMenuExpandedState(true) },
        onDismissQuestionTypeMenu = { viewModel.setQuestionTypeMenuExpandedState(false) },
        questionNotificationPeriodOption = settingsState.periodOption,
        onPeriodOptionChanged = { viewModel.setQuestionPeriod(it) },
        isPeriodOptionMenuExpanded = periodMenuExpanded,
        onExpandPeriodOptionMenu = { viewModel.setPeriodOptionMenuExpandedState(true) },
        onDismissPeriodOptionMenu = { viewModel.setPeriodOptionMenuExpandedState(false) }
    )
}

@Composable
fun QuestionNotificationSettingsScreen(
    isNotificationEnabled: Boolean,
    onNotificationEnabledChanged: (Boolean) -> Unit,
    category: Category?,
    onCategoryChanged: (Category?) -> Unit,
    categories: List<Category>,
    isCategoriesMenuExpanded: Boolean,
    onDismissCategoriesMenu: () -> Unit,
    onExpandCategoriesMenu: () -> Unit,
    difficulty: Difficulty?,
    onDifficultyChanged: (Difficulty?) -> Unit,
    isDifficultyMenuExpanded: Boolean,
    onExpandDifficultyMenu: () -> Unit,
    onDismissDifficultyMenu: () -> Unit,
    questionType: QuestionType?,
    onQuestionTypeChanged: (QuestionType?) -> Unit,
    isQuestionTypeMenuExpanded: Boolean,
    onExpandQuestionTypeMenu: () -> Unit,
    onDismissQuestionTypeMenu: () -> Unit,
    questionNotificationPeriodOption: QuestionNotificationPeriodOption,
    onPeriodOptionChanged: (QuestionNotificationPeriodOption) -> Unit,
    isPeriodOptionMenuExpanded: Boolean,
    onExpandPeriodOptionMenu: () -> Unit,
    onDismissPeriodOptionMenu: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(5.dp)
    ) {
        QuestionNotificationEnabledSetting(
            isNotificationEnabled = isNotificationEnabled,
            onNotificationEnabledChanged = onNotificationEnabledChanged
        )
        QuestionNotificationCategoryOption(
            category, onCategoryChanged, categories, isCategoriesMenuExpanded, onDismissCategoriesMenu, onExpandCategoriesMenu
        )
        QuestionNotificationSettingsDifficultyOption(
            difficulty = difficulty,
            onDifficultyChanged = onDifficultyChanged,
            isDifficultyMenuExpanded = isDifficultyMenuExpanded,
            onExpandDifficultyMenu = onExpandDifficultyMenu,
            onDismissDifficultyMenu = onDismissDifficultyMenu
        )
        QuestionNotificationTypeOption(
            questionType = questionType,
            onQuestionTypeChanged = onQuestionTypeChanged,
            isQuestionTypeMenuExpanded = isQuestionTypeMenuExpanded,
            onExpandQuestionTypeMenu = onExpandQuestionTypeMenu,
            onDismissQuestionTypeMenu = onDismissQuestionTypeMenu
        )
        QuestionNotificationPeriodOptionSection(
            questionNotificationPeriodOption = questionNotificationPeriodOption,
            onPeriodOptionChanged = onPeriodOptionChanged,
            isPeriodOptionMenuExpanded = isPeriodOptionMenuExpanded,
            onExpandPeriodOptionMenu = onExpandPeriodOptionMenu,
            onDismissPeriodOptionMenu = onDismissPeriodOptionMenu
        )
    }
}

@Composable
fun QuestionNotificationEnabledSetting(
    isNotificationEnabled: Boolean,
    onNotificationEnabledChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        val statusText = stringResource(
            if (isNotificationEnabled) R.string.enabled_label else R.string.disabled_label
        )
        val text = stringResource(
            R.string.question_notifications,
            statusText
        )
        Text(
            text = text,
            modifier = Modifier
                .weight(4f)
        )
        Switch(
            checked = isNotificationEnabled,
            onCheckedChange = onNotificationEnabledChanged,
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun QuestionNotificationCategoryOption(
    category: Category?,
    onCategoryChanged: (Category?) -> Unit,
    categories: List<Category>,
    isCategoriesMenuExpanded: Boolean,
    onDismissCategoriesMenu: () -> Unit,
    onExpandCategoriesMenu: () -> Unit
) {
    val categoryText = category?.name ?: stringResource(R.string.any)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(R.string.question_notification_category),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 3.dp)
        )
        Row(
            modifier = Modifier
                .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                .padding(2.dp)
                .align(Alignment.CenterVertically)
                .clickable { onExpandCategoriesMenu() }
        ) {
            Text(
                text = categoryText,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .padding(horizontal = 5.dp)
                    .weight(4f)
            )
            Icon(
                Icons.Default.ArrowDropDown,
                contentDescription = "Category Drop Down Icon",
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .weight(1f)
            )
        }
        DropdownMenu(
            expanded = isCategoriesMenuExpanded,
            onDismissRequest = onDismissCategoriesMenu
        ) {
            val categoriesOptions = listOf(CategoryOption.Any) +
                    categories.map { CategoryOption.ConcreteCategory(it) }
            categoriesOptions.forEach { categoryOption ->
                DropdownMenuItem(
                    onClick = {
                        onCategoryChanged(
                            when (categoryOption) {
                                CategoryOption.Any -> null
                                is CategoryOption.ConcreteCategory -> categoryOption.category
                            }
                        )
                        onDismissCategoriesMenu()
                    }
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
fun QuestionNotificationSettingsDifficultyOption(
    difficulty: Difficulty?,
    onDifficultyChanged: (Difficulty?) -> Unit,
    isDifficultyMenuExpanded: Boolean,
    onExpandDifficultyMenu: () -> Unit,
    onDismissDifficultyMenu: () -> Unit
) {
    val difficultyText = stringResource(difficulty?.textId ?: R.string.any)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(R.string.question_notification_difficulty),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 3.dp)
        )
        Column {
            Row(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                    .padding(2.dp)
                    .clickable { onExpandDifficultyMenu() }
            ) {
                Text(
                    text = difficultyText,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .weight(4f)
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Difficulty Drop Down Icon",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
            }
            DropdownMenu(
                expanded = isDifficultyMenuExpanded,
                onDismissRequest = { onDismissDifficultyMenu() }
            ) {
                listOf(null, *Difficulty.values()).forEach {
                    val difficultyOptionText = stringResource(it?.textId ?: R.string.any)
                    DropdownMenuItem(
                        onClick = {
                            onDifficultyChanged(it)
                            onDismissDifficultyMenu()
                        }
                    ) {
                        Text(difficultyOptionText)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionNotificationTypeOption(
    questionType: QuestionType?,
    onQuestionTypeChanged: (QuestionType?) -> Unit,
    isQuestionTypeMenuExpanded: Boolean,
    onExpandQuestionTypeMenu: () -> Unit,
    onDismissQuestionTypeMenu: () -> Unit
) {
    val questionTypeText = stringResource(questionType?.textId ?: R.string.any)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(R.string.question_notification_type),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 3.dp)
        )
        Column {
            Row(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                    .padding(2.dp)
                    .clickable { onExpandQuestionTypeMenu() }
            ) {
                Text(
                    text = questionTypeText,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .weight(4f)
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Question Type Drop Down Icon",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
            }
            DropdownMenu(
                expanded = isQuestionTypeMenuExpanded,
                onDismissRequest = { onDismissQuestionTypeMenu() }
            ) {
                listOf(null, *QuestionType.values()).forEach {
                    val questionTypeOptionText = stringResource(it?.textId ?: R.string.any)
                    DropdownMenuItem(
                        onClick = {
                            onQuestionTypeChanged(it)
                            onDismissQuestionTypeMenu()
                        }
                    ) {
                        Text(questionTypeOptionText)
                    }
                }
            }
        }
    }
}

@Composable
fun QuestionNotificationPeriodOptionSection(
    questionNotificationPeriodOption: QuestionNotificationPeriodOption,
    onPeriodOptionChanged: (QuestionNotificationPeriodOption) -> Unit,
    isPeriodOptionMenuExpanded: Boolean,
    onExpandPeriodOptionMenu: () -> Unit,
    onDismissPeriodOptionMenu: () -> Unit
) {
    val questionPeriodText = stringResource(questionNotificationPeriodOption.textId)
    Row(
        modifier = Modifier
            .background(color = MaterialTheme.colors.background)
    ) {
        Text(
            text = stringResource(R.string.question_notification_period),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(horizontal = 3.dp)
        )
        Column {
            Row(
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colors.primaryVariant, RoundedCornerShape(5.dp))
                    .padding(2.dp)
                    .clickable { onExpandPeriodOptionMenu() }
            ) {
                Text(
                    text = questionPeriodText,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 5.dp)
                        .weight(4f)
                )
                Icon(
                    Icons.Default.ArrowDropDown,
                    contentDescription = "Question Period Drop Down Icon",
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                )
            }
            DropdownMenu(
                expanded = isPeriodOptionMenuExpanded,
                onDismissRequest = { onDismissPeriodOptionMenu() }
            ) {
                QuestionNotificationPeriodOption.values().forEach {
                    val periodText = stringResource(it.textId)
                    DropdownMenuItem(
                        onClick = {
                            onPeriodOptionChanged(it)
                            onDismissPeriodOptionMenu()
                        }
                    ) {
                        Text(periodText)
                    }
                }
            }
        }
    }
}

@Composable
@Preview
fun PreviewQuestionNotificationEnabledSetting() {
    QuestionNotificationEnabledSetting(
        isNotificationEnabled = true,
        onNotificationEnabledChanged = {}
    )
}

@Composable
@Preview
fun PreviewQuestionNotificationCategorySetting() {
    QuestionNotificationCategoryOption(
        category = null,
        onCategoryChanged = {},
        categories = emptyList(),
        isCategoriesMenuExpanded = false,
        onDismissCategoriesMenu = {},
        onExpandCategoriesMenu = {}
    )
}

@Composable
@Preview
fun PreviewQuestionNotificationDifficultyOption() {
    QuestionNotificationSettingsDifficultyOption(
        difficulty = null,
        onDifficultyChanged = {},
        isDifficultyMenuExpanded = false,
        onExpandDifficultyMenu = {},
        onDismissDifficultyMenu = {}
    )
}

@Composable
@Preview
fun PreviewQuestionNotificationSettingsScreen() {
    QuestionNotificationSettingsScreen(
        isNotificationEnabled = true,
        onNotificationEnabledChanged = {},
        category = null,
        onCategoryChanged = {},
        categories = emptyList(),
        isCategoriesMenuExpanded = false,
        onDismissCategoriesMenu = {},
        onExpandCategoriesMenu = {},
        difficulty = null,
        onDifficultyChanged = {},
        isDifficultyMenuExpanded = false,
        onExpandDifficultyMenu = {},
        onDismissDifficultyMenu = {},
        questionType = null,
        onQuestionTypeChanged = {},
        isQuestionTypeMenuExpanded = false,
        onDismissQuestionTypeMenu = {},
        onExpandQuestionTypeMenu = {},
        questionNotificationPeriodOption = QuestionNotificationPeriodOption.FifteenMinutes,
        onPeriodOptionChanged = {},
        isPeriodOptionMenuExpanded = false,
        onExpandPeriodOptionMenu = {},
        onDismissPeriodOptionMenu = {}
    )
}

