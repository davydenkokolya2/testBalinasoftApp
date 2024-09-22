package com.example.testbalinasoftapp

import android.app.Application
import com.example.testbalinasoftapp.ui.viewmodel.HostViewModel
import com.example.testbalinasoftapp.ui.viewmodel.PhotoDetailViewModel
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
                    single { HostViewModel() }
                    single { PhotoDetailViewModel() }
                })
        }
    }
}