package grand.app.moon.data.home.data_source.local

import androidx.room.*
import grand.app.moon.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
  @Query("Select * from HomeStudentData")
  fun getNews(): Flow<HomeStudentData>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHomeData(homeStudentData: HomeStudentData)
  @Query("DELETE from HomeStudentData")
  fun deleteHomeData()

<<<<<<< HEAD
  @Query("DELETE from HomeStudentData")
  fun deleteHomeData()
=======
>>>>>>> fe9f79b930d685897dfc332c799fba773b59624a
}