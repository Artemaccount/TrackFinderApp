package com.example.myapplication.presentation.fragments

import com.github.terrakok.cicerone.androidx.FragmentScreen

object Screens {
    fun trackList() = FragmentScreen { TrackListFragment() }
    fun selectedTrack(id: Int) = FragmentScreen {
        SelectedTrackFragment.getNewInstance(id)
    }
}