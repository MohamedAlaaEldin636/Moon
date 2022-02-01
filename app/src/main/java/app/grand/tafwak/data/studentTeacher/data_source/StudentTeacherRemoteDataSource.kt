package app.grand.tafwak.data.studentTeacher.data_source

import app.grand.tafwak.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class StudentTeacherRemoteDataSource @Inject constructor(private val apiService: StudentTeacherServices) :
  BaseRemoteDataSource() {

  suspend fun studentTeacher() = safeApiCall {
    apiService.studentTeacher()
  }
}