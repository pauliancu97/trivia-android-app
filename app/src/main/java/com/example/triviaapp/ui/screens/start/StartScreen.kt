package com.example.triviaapp.ui.screens.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.triviaapp.R


@Composable
fun StartScreen(
    onPlayClick: () -> Unit
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
        Button(
            onClick = onPlayClick,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        ) {
            Text(text = stringResource(R.string.play))
        }
    }
}

@Composable
@Preview
fun PreviewStartScreen() {
    StartScreen(onPlayClick = {})
}