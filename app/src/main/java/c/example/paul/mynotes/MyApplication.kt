package c.example.paul.mynotes

import android.app.Application

import c.example.paul.mynotes.di.appModule
import org.koin.android.ext.android.startKoin

class MyApplication:Application(){
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }

}