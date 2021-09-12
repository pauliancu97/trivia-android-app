package com.example.triviaapp.ui.animations

import androidx.compose.animation.*
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun EnterFromRightAnimation(
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    val visibleState = remember { MutableTransitionState(false) }
    visibleState.targetState = true
    AnimatedVisibility(
        visibleState = visibleState,
        content = content,
        enter = slideInHorizontally(
            initialOffsetX = { it }
        ),
        exit = slideOutHorizontally(
            targetOffsetX = { -it }
        )
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun <T> AnimateQuestionContent(
    state: T,
    content: @Composable () -> Unit
) {
    AnimatedContent(
        targetState = state,
        transitionSpec = {
            fadeIn(animationSpec = tween(1000)) with fadeOut(animationSpec = tween(1000))
        }
    ) {
        content()
    }
}