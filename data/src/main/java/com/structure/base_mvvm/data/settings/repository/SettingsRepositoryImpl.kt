package com.structure.base_mvvm.data.settings.repository

import com.structure.base_mvvm.data.settings.data_source.remote.SettingsRemoteDataSource
import com.structure.base_mvvm.domain.settings.models.ContactUsRequest
import com.structure.base_mvvm.domain.settings.models.SettingsData
import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(private val remoteDataSource: SettingsRemoteDataSource) :
  SettingsRepository {
  override suspend fun settings(
    type: String,
    app_type: String
  ): Resource<BaseResponse<SettingsData>> = remoteDataSource.settings(type, app_type)

  override suspend fun social(
    type: String,
    app_type: String
  ): Resource<BaseResponse<List<SettingsData>>> = remoteDataSource.social(type, app_type)

  override suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>> =
    remoteDataSource.contactApp(contactUsRequest)
}