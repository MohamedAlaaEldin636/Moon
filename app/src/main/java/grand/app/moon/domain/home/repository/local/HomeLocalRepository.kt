package grand.app.moon.domain.home.repository.local

import grand.app.moon.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow


interface HomeLocalRepository {
  fun studentHomeLocal(): Flow<HomeStudentData>
  suspend fun insertStudentHomeLocal(homeStudentData: HomeStudentData)
  suspend fun deleteAll()
<<<<<<< HEAD
=======

>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
}