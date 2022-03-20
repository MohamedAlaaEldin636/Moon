package grand.app.moon.data.general.data_source.remote

import grand.app.moon.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class GeneralRemoteDataSource @Inject constructor(private val apiService: GeneralServices) : BaseRemoteDataSource()