package com.example.myapplication.presentation.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.myapplication.R
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.presentation.fragments.TrackListFragment
import com.example.myapplication.presentation.fragments.getAppComponent
import com.example.myapplication.presentation.viewmodel.TrackViewModel

class TrackAppActivity : AppCompatActivity(R.layout.app_activity) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container)?.isAdded != true) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, TrackListFragment())
                .commit()
        }
    }
}