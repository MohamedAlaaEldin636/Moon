package app.grand.tafwak.core.di.module

import android.content.Context
import androidx.room.Room
import app.grand.tafwak.core.AppDatabase
import app.grand.tafwak.data.home.data_source.local.HomeDao
import com.structure.base_mvvm.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

//@InstallIn(SingletonComponent::class)
//@Module
object RoomModule {
//  @Singleton
//  @Provides
//  fun provideMyDB(@ApplicationContext context: Context): AppDatabase {
//    return Room.databaseBuilder(
//      context,
//      AppDatabase::class.java,
//      BuildConfig.ROOM_DB
//    ).fallbackToDestructiveMigration().build()
//  }
}

//@Singleton
//@Provides
//fun provideMyDao(myDB: AppDatabase): HomeDao {
//  return myDB.getHomeDao()
//}
