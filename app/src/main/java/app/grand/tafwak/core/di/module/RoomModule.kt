package app.grand.tafwak.core.di.module

import android.content.Context
import androidx.room.Room
import app.grand.tafwak.core.AppDatabase
import app.grand.tafwak.core.MyApplication
import app.grand.tafwak.data.home.data_source.local.HomeLocalRemoteDataSource
import app.grand.tafwak.data.home.repository.local.HomeLocalRepositoryImpl
import app.grand.tafwak.domain.home.repository.local.HomeLocalRepository
import com.structure.base_mvvm.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
  @Provides
  @Singleton
  fun provideMyDB(@ApplicationContext context: Context): AppDatabase {
    return Room.databaseBuilder(
      context,
      AppDatabase::class.java,
      BuildConfig.ROOM_DB
    ).build()
  }

  @Provides
  @Singleton
  fun provideHomeLocalRepository(db: AppDatabase): HomeLocalRemoteDataSource {
    return HomeLocalRemoteDataSource(db.getHomeDao)
  }

}



