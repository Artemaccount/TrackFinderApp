package com.example.myapplication.presentation.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.presentation.fragments.TrackListFragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.androidx.AppNavigator
import javax.inject.Inject

class TrackAppActivity : AppCompatActivity(R.layout.app_activity) {

    private val navigator: Navigator = AppNavigator(
        this,
        R.id.fragment_container
    )

    @Inject
    lateinit var navigatorHolder: NavigatorHolder


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as TrackFinderApplication).appComponent.inject(this)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container)?.isAdded != true) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, TrackListFragment())
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        super.onPause()
        navigatorHolder.removeNavigator()
    }
}