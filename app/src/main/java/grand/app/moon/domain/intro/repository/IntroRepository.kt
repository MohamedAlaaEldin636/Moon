package grand.app.moon.domain.intro.repository

import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource

interface IntroRepository {
  suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>>
}