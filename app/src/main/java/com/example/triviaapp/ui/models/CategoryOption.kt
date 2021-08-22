package com.example.triviaapp.ui.models

sealed class CategoryOption {
    object Any : CategoryOption()
    data class ConcreteCategory(val category: Category) : CategoryOption()
}