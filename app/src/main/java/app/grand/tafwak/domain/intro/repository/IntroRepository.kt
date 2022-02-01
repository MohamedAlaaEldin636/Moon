package app.grand.tafwak.domain.intro.repository

import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource

interface IntroRepository {
  suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>>
}