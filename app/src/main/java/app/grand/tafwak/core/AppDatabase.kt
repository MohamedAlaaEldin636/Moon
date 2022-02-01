package app.grand.tafwak.core

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import app.grand.tafwak.data.home.data_source.local.HomeDao
import app.grand.tafwak.domain.home.models.HomeStudentData
import app.grand.tafwak.domain.utils.Converters

//@Database(entities = [HomeStudentData::class],
//  version = 1, exportSchema = false)
//@TypeConverters(Converters::class)
abstract class AppDatabase
//  : RoomDatabase()
{
  abstract fun getHomeDao(): HomeDao
//
//  companion object {
//    // For Singleton instantiation
//    @Volatile
//    private var instance: AppDatabase? = null
//
//    fun getInstance(context: Context): AppDatabase {
//      return instance ?: synchronized(this) {
//        instance ?: buildDatabase(context).also { instance = it }
//      }
//    }
//
//    // Create and pre-populate the database. See this article for more details:
//    private fun buildDatabase(context: Context): AppDatabase {
//      return Room.databaseBuilder(context, AppDatabase::class.java, BuildConfig.ROOM_DB)
//        .addCallback(object : RoomDatabase.Callback() {
//        }).build()
//    }
//  }
}