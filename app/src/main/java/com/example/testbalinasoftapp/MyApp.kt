package com.example.testbalinasoftapp

import CameraViewModel
import android.app.Application
import com.example.testbalinasoftapp.data.RetrofitClient.provideApiService
import com.example.testbalinasoftapp.data.RetrofitClient.provideRetrofit
import com.example.testbalinasoftapp.data.database.AppDatabase
import com.example.testbalinasoftapp.data.repositories.AuthRepository
import com.example.testbalinasoftapp.data.repositories.GalleryRepository
import com.example.testbalinasoftapp.data.repositories.UserRepository
import com.example.testbalinasoftapp.domain.usecases.GalleryUseCase
import com.example.testbalinasoftapp.domain.usecases.LoginUseCase
import com.example.testbalinasoftapp.domain.usecases.UserUseCase
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.DrawerViewModel
import com.example.testbalinasoftapp.ui.viewmodel.GalleryViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                module {
                    single { provideRetrofit() }
                    single { provideApiService(get()) }

                    single { AppDatabase.getDatabase(get()) }

                    single { LoginUseCase(get()) }
                    single { UserUseCase(get()) }
                    single { GalleryUseCase(get()) }

                    single { UserRepository(get()) }
                    single { AuthRepository(get()) }
                    single { GalleryRepository(get(), get()) }

                    single { HostViewModel() }
                    single { PhotoDetailViewModel() }
                    single { AuthViewModel(get(), get()) }
                    single { DrawerViewModel(get()) }
                    single { GalleryViewModel(get()) }
                    single { CameraViewModel() }
                })
        }
        clearDatabase()
    }

    private fun clearDatabase() {
        val db = AppDatabase.getDatabase(this)
        val userDao = db.userDao()
        val imageDao = db.imageDao()

        CoroutineScope(Dispatchers.IO).launch {
            userDao.clearTable()
            imageDao.clearTable()
        }
    }
}