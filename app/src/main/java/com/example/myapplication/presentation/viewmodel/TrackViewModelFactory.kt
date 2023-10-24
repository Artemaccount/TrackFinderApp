package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.api.TrackRepositoryApi
import javax.inject.Inject

class TrackViewModelFactory @Inject constructor(
    val repository: TrackRepositoryApi
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackViewModel(repository = repository) as T
    }
}
