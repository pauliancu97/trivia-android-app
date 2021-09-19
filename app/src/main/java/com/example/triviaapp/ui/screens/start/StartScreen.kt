package com.example.triviaapp.ui.screens.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R
import kotlinx.coroutines.launch


@Composable
fun StartScreen(
    viewModel: StartScreenViewModel,
    navigateToCreateQuizFirstPage: () -> Unit,
    navigateToQuizTemplatesPage: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val isFetching by viewModel.isFetchingLiveData().observeAsState(false)
    StartScreen(
        onPlayClick = {
            coroutineScope.launch {
                viewModel.loadCategories()
                navigateToCreateQuizFirstPage()
            }
        },
        onQuizTemplatesClick = { navigateToQuizTemplatesPage() },
        isFetching = isFetching
    )
}

@Composable
fun StartScreen(
    onPlayClick: () -> Unit,
    onQuizTemplatesClick: () -> Unit,
    isFetching: Boolean = false
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Text(
            text = stringResource(R.string.app_title),
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(
                    top = dimensionResource(R.dimen.app_title_padding_top),
                    bottom = dimensionResource(R.dimen.app_title_padding_bottom)
                ),
            style = MaterialTheme.typography.h2
        )
        if (!isFetching) {
            Button(
                onClick = onPlayClick,
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                Text(text = stringResource(R.string.play))
            }
        } else {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            )
        }
        Button(
            onClick = onQuizTemplatesClick,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(stringResource(R.string.quiz_templates))
        }
    }
}

@Composable
@Preview
fun PreviewStartScreen() {
    StartScreen(
        onPlayClick = {},
        onQuizTemplatesClick = {}
    )
}