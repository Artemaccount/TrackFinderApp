package com.example.myapplication.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.api.TrackRepositoryApi
import com.example.myapplication.presentation.viewmodel.TrackViewModel

class TrackViewModelFactory(

    val repository: TrackRepositoryApi
) : ViewModelProvider.Factory {


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TrackViewModel(repository = repository) as T
    }
}