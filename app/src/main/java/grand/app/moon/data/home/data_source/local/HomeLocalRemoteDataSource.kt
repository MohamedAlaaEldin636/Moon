package grand.app.moon.data.home.data_source.local

import grand.app.moon.domain.home.models.HomeStudentData
import javax.inject.Inject

class HomeLocalRemoteDataSource @Inject constructor(private val homeDao: HomeDao) {

  fun homeStudentLocal() = homeDao.getNews()
  suspend fun insertHomeStudent(homeStudentData: HomeStudentData)=homeDao.insertHomeData(homeStudentData)
  fun homeStudentDelete() = homeDao.deleteHomeData()
<<<<<<< HEAD
=======

>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
}