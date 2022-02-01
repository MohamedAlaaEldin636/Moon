package app.grand.tafwak.data.home.data_source.local.repository

import app.grand.tafwak.data.home.data_source.local.HomeDao
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.home.repository.local.HomeLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeLocalRepositoryImpl @Inject constructor(private val homeDao: HomeDao) :
  HomeLocalRepository {
  override fun studentHomeLocal(): Flow<HomeStudentData> = homeDao.getNews()


}