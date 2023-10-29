package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.example.myapplication.data.api.ItunesApi
import com.example.myapplication.data.db.AppDb
import com.example.myapplication.data.db.TrackDao
import com.example.myapplication.presentation.activity.TrackAppActivity
import com.example.myapplication.presentation.fragments.SelectedTrackFragment
import com.example.myapplication.presentation.fragments.TrackListFragment
import com.example.myapplication.presentation.viewmodel.TrackViewModelFactory
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Component
    (
    modules = [
        DbModule::class,
        NetworkModule::class,
        CiceroneModule::class
    ]
)
@Singleton
interface AppComponent {
    fun inject(fragment: TrackListFragment)
    fun inject(fragment: SelectedTrackFragment)
    fun inject(activity: TrackAppActivity)
    fun viewModelFactory(): TrackViewModelFactory
}


@Module
class CiceroneModule {
    private val cicerone: Cicerone<Router> = Cicerone.create()

    @Provides
    @Singleton
    fun provideRouter(): Router {
        return cicerone.router
    }

    @Provides
    @Singleton
    fun provideNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }
}

@Module
class DbModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideTrackDao(context: Context): TrackDao {
        return Room.databaseBuilder(
            context,
            AppDb::class.java,
            "app.db"
        ).build().trackDAO
    }

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }
}

@Module
class NetworkModule {
    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    @Provides
    @Singleton
    fun providesRetrofitClient(): ItunesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }


    @Provides
    @Singleton
    fun provideGlide(context: Context): RequestManager {
        return Glide.with(context)
    }

}