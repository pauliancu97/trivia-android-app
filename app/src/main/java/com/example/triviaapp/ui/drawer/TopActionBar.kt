package com.example.triviaapp.ui.drawer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.triviaapp.R

@Composable
fun TopMenuAppBar(
    openDrawer: () -> Unit
) {
    TopAppBar {
        Row {
            Icon(
                Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .clickable { openDrawer() }
            )
            Text(text = stringResource(R.string.app_title))
        }
    }
}