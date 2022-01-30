package com.structure.base_mvvm.data.home.data_source.local

import androidx.room.*
import com.structure.base_mvvm.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow

@Dao
interface HomeDao {
  @Query("Select * from HomeStudentData")
  fun getNews(): Flow<HomeStudentData>

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insertHomeData(homeStudentData: HomeStudentData)

  @Delete
  suspend fun deleteHomeData()
}