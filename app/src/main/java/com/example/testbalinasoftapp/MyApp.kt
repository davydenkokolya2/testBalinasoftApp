package com.example.testbalinasoftapp

import android.app.Application
import com.example.testbalinasoftapp.data.RetrofitClient.provideApiService
import com.example.testbalinasoftapp.data.RetrofitClient.provideRetrofit
import com.example.testbalinasoftapp.data.database.AppDatabase
import com.example.testbalinasoftapp.data.repositories.AuthRepository
import com.example.testbalinasoftapp.data.repositories.UserRepository
import com.example.testbalinasoftapp.domain.usecases.LoginUseCase
import com.example.testbalinasoftapp.domain.usecases.UserUseCase
import com.example.testbalinasoftapp.ui.viewmodel.AuthViewModel
import com.example.testbalinasoftapp.ui.viewmodel.DrawerViewModel
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import kotlin.math.sin

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@MyApp)
            modules(
                module {
                    /*single { HostViewModel() }
                    single { PhotoDetailViewModel() }
                    single { AppDatabase.getDatabase(get()) }
                    single { get<AppDatabase>().userDao() }
                    single { AuthRepository(get(), get()) }
                    single { LoginUseCase(get()) }
                    viewModel { AuthViewModel(get()) }*/
                    single { provideRetrofit() }
                    single { provideApiService(get()) }
                    single { AppDatabase.getDatabase(get()) }
                    single { UserRepository(get()) }
                    // Регистрируем DAO
                    single { get<AppDatabase>().userDao() }

                    // Регистрируем репозиторий
                    single { AuthRepository(get(), get()) }

                    // Регистрируем use case
                    single { LoginUseCase(get()) }
                    single { UserUseCase(get()) }

                    // Регистрация ViewModel через специальный метод viewModel
                    single { HostViewModel() }
                    single { PhotoDetailViewModel() }
                    single { AuthViewModel(get(), get()) }
                    single { DrawerViewModel(get()) }
                })
        }
    }
}