package com.example.gallery.app

import android.app.Application
import com.example.gallery.app.di.RoomModule
import com.example.gallery.app.di.RoomModuleImpl
import com.example.gallery.app.di.RetrofitModule
import com.example.gallery.app.di.RetrofitModuleImpl

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        roomModule = RoomModuleImpl(context = this)
        retrofitModule = RetrofitModuleImpl
    }

    companion object {
        lateinit var roomModule: RoomModule
            private set
        lateinit var retrofitModule: RetrofitModule
            private set
    }
}