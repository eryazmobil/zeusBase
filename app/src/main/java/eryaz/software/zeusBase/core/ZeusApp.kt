package eryaz.software.zeusBase.core

import android.content.Context
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import eryaz.software.zeusBase.data.di.appModuleApis
import eryaz.software.zeusBase.data.di.appModuleRepos
import eryaz.software.zeusBase.data.persistence.SessionManager
import eryaz.software.zeusBase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class ZeusApp : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()

        initKoin()
        init()
    }

    private fun init() {
        SessionManager.init(applicationContext)

            Timber.plant(Timber.DebugTree())
    }

    private fun initKoin() {
        startKoin {
            androidLogger(if (BuildConfig.DEBUG) Level.ERROR else Level.NONE)
            androidContext(this@ZeusApp)
            androidFileProperties()
            modules(listOf(appModule, appModuleApis, appModuleRepos))
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}