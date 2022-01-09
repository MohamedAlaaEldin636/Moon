package com.structure.base_mvvm.data.intro.data_source

import com.structure.base_mvvm.domain.intro.entity.AppTutorial
import com.structure.base_mvvm.domain.utils.BaseResponse
import retrofit2.http.GET

interface IntroServices {

  @GET("app/app-explanation/0/0")
  suspend fun intro(): BaseResponse<List<AppTutorial>>

}