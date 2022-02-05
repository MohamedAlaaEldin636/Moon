package app.grand.tafwak.data.home.data_source.local

import androidx.room.*
import app.grand.tafwak.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
  @Query("Select * from HomeStudentData")
  fun getNews(): Flow<HomeStudentData>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHomeData(homeStudentData: HomeStudentData)
  @Query("DELETE from HomeStudentData")
  fun deleteHomeData()

}