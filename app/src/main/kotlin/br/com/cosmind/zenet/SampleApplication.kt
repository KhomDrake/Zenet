package br.com.cosmind.zenet

import android.app.Application
import br.com.cosmind.extensions.DateConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DateConfig.setup(this)
    }

}