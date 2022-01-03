package com.structure.base_mvvm.data.local.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PrefsStoreImpl @Inject constructor(private val context: Context) {
//  private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = STORE_NAME)
//
//  companion object {
//    val NAME = stringPreferencesKey("NAME")
//  }
//
//  suspend fun saveNameToDataStore(name: String) {
//    context.dataStore.edit {
//      it[NAME] = name
//    }
//  }
//
//  suspend fun getNameFromDataStore() = context.dataStore.data.map {
//    it[NAME] ?: ""
//  }
}

