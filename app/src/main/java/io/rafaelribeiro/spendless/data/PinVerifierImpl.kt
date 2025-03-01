package io.rafaelribeiro.spendless.data

import io.rafaelribeiro.spendless.domain.PinVerifier
import javax.inject.Inject

class PinVerifierImpl @Inject constructor() : PinVerifier {

    // TODO: Check if PIN is correct through DataStore

    override suspend fun isPinCorrect(pin: String): Boolean {
        return false // TODO: Check if PIN is correct
    }

}
