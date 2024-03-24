package br.com.khomdrake.zenet

import android.app.Application
import br.com.khomdrake.extensions.DateConfig

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DateConfig.setup(this)
    }

}