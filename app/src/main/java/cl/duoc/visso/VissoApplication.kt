package cl.duoc.visso

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class VissoApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: VissoApplication
            private set
    }
}