package app.grand.tafwak.data.home.data_source.local

import app.grand.tafwak.domain.home.models.HomeStudentData
import javax.inject.Inject

class HomeLocalRemoteDataSource @Inject constructor(private val homeDao: HomeDao) {

  fun homeStudentLocal() = homeDao.getNews()
  suspend fun insertHomeStudent(homeStudentData: HomeStudentData)=homeDao.insertHomeData(homeStudentData)
  fun homeStudentDelete() = homeDao.deleteHomeData()

}