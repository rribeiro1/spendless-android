package io.rafaelribeiro.spendless.data.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import io.rafaelribeiro.spendless.data.crypto.EncryptedDouble
import io.rafaelribeiro.spendless.data.crypto.EncryptedString
import javax.inject.Inject

@ProvidedTypeConverter
class CryptoTypeConverter @Inject constructor() {

    @TypeConverter
    fun encryptedStringFromByteArray(value: ByteArray?): EncryptedString? =
        value?.let { EncryptedString(it) }

    @TypeConverter
    fun encryptedStringToByteArray(encryptedString: EncryptedString?): ByteArray? =
        encryptedString?.value

    @TypeConverter
    fun encryptedDoubleFromByteArray(value: ByteArray?): EncryptedDouble? =
        value?.let { EncryptedDouble(it) }

    @TypeConverter
    fun encryptedDoubleToByteArray(encryptedDouble: EncryptedDouble?): ByteArray? =
        encryptedDouble?.value
}