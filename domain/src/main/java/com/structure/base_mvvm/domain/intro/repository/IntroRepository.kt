package com.structure.base_mvvm.domain.intro.repository

import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface IntroRepository {
  suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>>
}