package app.grand.tafwak.data.intro.repository

import app.grand.tafwak.data.intro.data_source.IntroRemoteDataSource
import app.grand.tafwak.domain.intro.entity.AppTutorial
import app.grand.tafwak.domain.intro.repository.IntroRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class IntroRepositoryImpl @Inject constructor(
  private val remoteDataSource: IntroRemoteDataSource
) : IntroRepository {
  override suspend fun intro(): Resource<BaseResponse<List<AppTutorial>>> =
    remoteDataSource.intro()

}