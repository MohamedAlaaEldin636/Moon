package com.structure.base_mvvm.data.studentTeacher.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class StudentTeacherRemoteDataSource @Inject constructor(private val apiService: StudentTeacherServices) :
  BaseRemoteDataSource() {

  suspend fun studentTeacher() = safeApiCall {
    apiService.studentTeacher()
  }
}