package io.rafaelribeiro.spendless.domain

interface PinVerifier {
    suspend fun isPinCorrect(pin: String): Boolean
}
