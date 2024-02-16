package com.kotlinHero.tawfeer.products.data.local.serializers

import androidx.datastore.core.Serializer
import com.kotlinHero.tawfeer.common.utils.parsePreference
import com.kotlinHero.tawfeer.common.utils.writePreference
import com.kotlinHero.tawfeer.products.data.local.preferences.CartPreferences
import java.io.InputStream
import java.io.OutputStream

object CartPreferencesSerializer : Serializer<CartPreferences> {
  
    override val defaultValue = CartPreferences()

    override suspend fun readFrom(input: InputStream) =
        input.parsePreference<CartPreferences>()

    override suspend fun writeTo(t: CartPreferences, output: OutputStream) =
        output.writePreference(t)
}