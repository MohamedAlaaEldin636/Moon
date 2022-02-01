package app.grand.tafwak.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.grand.tafwak.data.home.data_source.local.HomeDao
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.utils.Converters

@Database(
  entities = [HomeStudentData::class],
  version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract val getHomeDao: HomeDao
}