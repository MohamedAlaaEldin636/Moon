package grand.app.moon.data.general.repository

import grand.app.moon.data.general.data_source.remote.GeneralRemoteDataSource
import grand.app.moon.data.local.preferences.AppPreferences
import grand.app.moon.domain.general.repository.GeneralRepository
import javax.inject.Inject

class GeneralRepositoryImpl @Inject constructor(
  private val remoteDataSource: GeneralRemoteDataSource,
  private val appPreferences: AppPreferences
) : GeneralRepository