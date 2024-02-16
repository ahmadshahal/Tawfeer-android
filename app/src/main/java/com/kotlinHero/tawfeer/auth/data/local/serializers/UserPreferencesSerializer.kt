package com.kotlinHero.tawfeer.auth.data.local.serializers

import androidx.datastore.core.Serializer
import com.kotlinHero.tawfeer.auth.data.local.preferences.UserPreferences
import com.kotlinHero.tawfeer.common.utils.parsePreference
import com.kotlinHero.tawfeer.common.utils.writePreference
import java.io.InputStream
import java.io.OutputStream

object UserPreferencesSerializer : Serializer<UserPreferences> {
  
    override val defaultValue = UserPreferences()

    override suspend fun readFrom(input: InputStream) =
        input.parsePreference<UserPreferences>()

    override suspend fun writeTo(t: UserPreferences, output: OutputStream) =
        output.writePreference(t)
}