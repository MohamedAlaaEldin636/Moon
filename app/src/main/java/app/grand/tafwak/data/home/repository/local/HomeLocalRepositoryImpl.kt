package app.grand.tafwak.data.home.repository.local

import app.grand.tafwak.data.home.data_source.local.HomeLocalRemoteDataSource
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.home.repository.local.HomeLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeLocalRepositoryImpl @Inject constructor(private val homeLocalRemoteDataSource: HomeLocalRemoteDataSource) :
  HomeLocalRepository {
  override fun studentHomeLocal(): Flow<HomeStudentData> =
    homeLocalRemoteDataSource.homeStudentLocal()

  override suspend fun insertStudentHomeLocal(homeStudentData: HomeStudentData) =
    homeLocalRemoteDataSource.insertHomeStudent(homeStudentData)
  override suspend fun deleteAll()  = homeLocalRemoteDataSource.homeStudentDelete()

}