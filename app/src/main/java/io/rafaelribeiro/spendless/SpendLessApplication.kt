package io.rafaelribeiro.spendless

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.work.Configuration
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class SpendLessApplication : Application(), Configuration.Provider {

    private val _sessionTimeout = MutableLiveData<Boolean>()
    val sessionTimeout = _sessionTimeout.map { it }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

    fun sessionTimeout() {
        _sessionTimeout.value = true
    }
}
