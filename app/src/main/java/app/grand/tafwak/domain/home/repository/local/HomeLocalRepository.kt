package app.grand.tafwak.domain.home.repository.local

import app.grand.tafwak.domain.home.models.HomeStudentData
import kotlinx.coroutines.flow.Flow


interface HomeLocalRepository {
  fun studentHomeLocal(): Flow<HomeStudentData>
}