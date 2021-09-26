package com.example.triviaapp.ui.drawer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.triviaapp.R
import com.example.triviaapp.ui.navigation.NavigationDestinations

@Composable
fun Drawer(
    selectedDrawerDestination: DrawerDestination? = null,
    onDrawerItemClicked: (DrawerDestination) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        for (drawerDestination in DrawerDestination.values()) {
            DrawerDestinationItem(
                drawerDestination,
                selectedDrawerDestination == drawerDestination,
                onClick = { onDrawerItemClicked(drawerDestination) }
            )
        }
    }
}

@Composable
fun DrawerDestinationItem(
    drawerDestination: DrawerDestination,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) MaterialTheme.colors.primary else MaterialTheme.colors.surface
            )
            .clickable { onClick() }
    ) {
        Icon(
            drawerDestination.icon,
            contentDescription = stringResource(drawerDestination.descriptionId),
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(start = 5.dp, end = 5.dp)
        )
        Text(
            text = stringResource(drawerDestination.descriptionId),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
    }
}

@Composable
@Preview
fun PreviewDrawer() {
    Drawer(
        selectedDrawerDestination = DrawerDestination.Settings,
        onDrawerItemClicked = {}
    )
}

@Composable
@Preview
fun PreviewDrawerDestinationItem() {
    DrawerDestinationItem(
        drawerDestination = DrawerDestination.Home,
        isSelected = false,
        onClick = {}
    )
}