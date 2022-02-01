package app.grand.tafwak.data.general.data_source.remote

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class GeneralRemoteDataSource @Inject constructor(private val apiService: GeneralServices) : BaseRemoteDataSource()