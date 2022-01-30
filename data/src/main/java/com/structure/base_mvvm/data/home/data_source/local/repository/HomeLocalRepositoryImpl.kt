package com.structure.base_mvvm.data.home.data_source.local.repository

import com.structure.base_mvvm.data.home.data_source.local.HomeDao
import com.structure.base_mvvm.domain.home.models.HomeStudentData
import com.structure.base_mvvm.domain.home.repository.local.HomeLocalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeLocalRepositoryImpl @Inject constructor(private val homeDao: HomeDao) :
  HomeLocalRepository {
  override fun studentHomeLocal(): Flow<HomeStudentData> = homeDao.getNews()


}