package app.grand.tafwak.data.general.repository

import app.grand.tafwak.data.general.data_source.remote.GeneralRemoteDataSource
import app.grand.tafwak.data.local.preferences.AppPreferences
import app.grand.tafwak.domain.general.repository.GeneralRepository
import javax.inject.Inject

class GeneralRepositoryImpl @Inject constructor(
  private val remoteDataSource: GeneralRemoteDataSource,
  private val appPreferences: AppPreferences
) : GeneralRepository