package grand.app.moon.data.home.repository.local

import grand.app.moon.data.home.data_source.local.HomeLocalRemoteDataSource
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.home.repository.local.HomeLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeLocalRepositoryImpl @Inject constructor(private val homeLocalRemoteDataSource: HomeLocalRemoteDataSource) :
  HomeLocalRepository {
  override fun studentHomeLocal(): Flow<HomeStudentData> =
    homeLocalRemoteDataSource.homeStudentLocal()

  override suspend fun insertStudentHomeLocal(homeStudentData: HomeStudentData) =
    homeLocalRemoteDataSource.insertHomeStudent(homeStudentData)
  override suspend fun deleteAll()  = homeLocalRemoteDataSource.homeStudentDelete()

  override suspend fun deleteAll()  = homeLocalRemoteDataSource.homeStudentDelete()

}