package com.example.myapplication.presentation.activity

import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.application.TrackFinderApplication
import com.example.myapplication.presentation.animation.AnimationAppNavigator
import com.example.myapplication.presentation.fragments.Screens
import com.example.myapplication.presentation.fragments.TrackListFragment
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import javax.inject.Inject


class TrackAppActivity : AppCompatActivity(R.layout.app_activity) {


    private val navigator: Navigator = AnimationAppNavigator(this, R.id.fragment_container)

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: Router


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (applicationContext as TrackFinderApplication).appComponent.inject(this)
        if (supportFragmentManager.findFragmentById(R.id.fragment_container)?.isAdded != true) {
            router.navigateTo(Screens.trackList())
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