package com.structure.base_mvvm.domain.home.repository.local

import com.structure.base_mvvm.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow


interface HomeLocalRepository {
  fun studentHomeLocal(): Flow<HomeStudentData>
}