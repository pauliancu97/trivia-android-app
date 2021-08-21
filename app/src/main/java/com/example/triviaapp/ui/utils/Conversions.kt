package com.example.triviaapp.ui.utils

import com.example.triviaapp.ui.database.entities.CategoryEntity
import com.example.triviaapp.ui.models.Category

fun CategoryEntity.toModel() = Category(
    id = this.id,
    name = this.name,
    numOfQuestions = this.numOfQuestions,
    numOfEasyQuestions = this.numOfEasyQuestions,
    numOfMediumQuestions = this.numOfMediumQuestions,
    numOfHardQuestions = this.numOfHardQuestions
)