package app.grand.tafwak.data.local.preferences

import kotlinx.coroutines.flow.Flow

interface PrefsStore {
  fun isNightMode(): Flow<Boolean>

  suspend fun saveUserName(name: String)
}