package grand.app.moon.data.intro.repository

import grand.app.moon.data.intro.data_source.IntroRemoteDataSource
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.intro.repository.IntroRepository
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
  private val remoteDataSource: IntroRemoteDataSource
) : IntroRepository {
  override suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>> =
    remoteDataSource.intro()

}