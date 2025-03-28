package io.rafaelribeiro.spendless.domain

interface PinLockTickerRepository {
    fun startSession(remainingSeconds: Int)
    fun cancelWorker()
}