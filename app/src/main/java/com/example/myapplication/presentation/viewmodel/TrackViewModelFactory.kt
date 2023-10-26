package com.example.myapplication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.api.TrackRepository
import com.example.myapplication.data.api.TrackRepositoryImpl
import javax.inject.Inject

class TrackViewModelFactory @Inject constructor(
    val repository: TrackRepositoryImpl
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackViewModel(repository = repository) as T
    }
}
