package com.structure.base_mvvm.domain.settings.repository

import com.structure.base_mvvm.domain.settings.models.ContactUsRequest
import com.structure.base_mvvm.domain.settings.models.SettingsData
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface SettingsRepository {
  suspend fun settings(type: String, app_type: String): Resource<BaseResponse<SettingsData>>
  suspend fun contactApp(contactUsRequest: ContactUsRequest): Resource<BaseResponse<*>>

}