package br.com.khomdrake.zenet

import android.app.Application
import br.com.khomdrake.extensions.DateConfig
import br.com.khomdrake.request.RequestHandler
import br.com.khomdrake.zenet.request.DiskData
import br.com.khomdrake.zenet.request.pages.NoCacheViewModel
import br.com.khomdrake.zenet.request.SampleRepository
import br.com.khomdrake.zenet.request.pages.WithCacheDiskViewModel
import br.com.khomdrake.zenet.request.pages.WithCacheMemoryViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module
import timber.log.Timber

class SampleApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        DateConfig.setup(this)
        RequestHandler.init(this, cleanCacheTimeout = false)
        DiskData.init(this)

        startKoin {
            androidContext(this@SampleApplication)
            modules(module {
                single { SampleRepository() }
                viewModel { NoCacheViewModel(get()) }
                viewModel { WithCacheDiskViewModel(get()) }
                viewModel { WithCacheMemoryViewModel(get()) }
            })
        }
        Timber.plant(Timber.DebugTree())
    }

}