package com.example.triviaapp.ui.models

data class Category(
    val id: Int,
    val name: String,
    val numOfQuestions: Int,
    val numOfEasyQuestions: Int,
    val numOfMediumQuestions: Int,
    val numOfHardQuestions: Int
)