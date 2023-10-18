package com.example.myapplication.di

import android.content.Context
import androidx.room.Room
import com.example.myapplication.presentation.fragments.TrackFragment
import com.example.myapplication.data.api.ItunesApi
import com.example.myapplication.data.api.TrackRepositoryApi
import com.example.myapplication.data.db.AppDb
import com.example.myapplication.data.db.TrackDao
import dagger.Component
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Component
    (modules = [AppModule::class, DbModule::class, NetworkModule::class])
interface AppComponent {
    fun inject(appActivity: TrackFragment)
}

@Module
class DbModule {
    @Provides
    fun provideTrackDao(context: Context): TrackDao {
        return Room.databaseBuilder(
            context,
            AppDb::class.java,
            "app.db"
        ).build().trackDAO
    }
}

@Module
class NetworkModule {
    companion object {
        const val BASE_URL = "https://itunes.apple.com/"
    }

    @Provides
    fun providesRetrofitClient(): ItunesApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApi::class.java)
    }
}

@Module
class AppModule(private val context: Context) {

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideTrackViewModelFactory(trackRepositoryApi: TrackRepositoryApi): TrackViewModelFactory {
        return TrackViewModelFactory(trackRepositoryApi)
    }

    @Provides
    fun provideTrackRepositoryApi(trackDao: TrackDao, apiService: ItunesApi): TrackRepositoryApi {
        return TrackRepositoryApi(trackDao, apiService)
    }

}