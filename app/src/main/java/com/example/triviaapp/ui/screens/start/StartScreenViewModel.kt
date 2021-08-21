package com.example.triviaapp.ui.screens.start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.triviaapp.ui.repositories.TriviaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class StartScreenViewModel @Inject constructor(
    private val triviaRepository: TriviaRepository
): ViewModel() {

    private val isFetchingMutableLiveData: MutableLiveData<Boolean> = MutableLiveData(false)

    suspend fun loadCategories() {
        isFetchingMutableLiveData.value = true
        withContext(Dispatchers.IO) {
            triviaRepository.fetchCategories()
        }
        isFetchingMutableLiveData.value = false
    }

    fun isFetchingLiveData(): LiveData<Boolean> = isFetchingMutableLiveData
}