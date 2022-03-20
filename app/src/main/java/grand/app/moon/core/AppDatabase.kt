package grand.app.moon.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import grand.app.moon.data.home.data_source.local.HomeDao
import grand.app.moon.domain.home.models.HomeStudentData
import grand.app.moon.domain.utils.Converters

@Database(
  entities = [HomeStudentData::class],
  version = 1, exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
  abstract val getHomeDao: HomeDao
}