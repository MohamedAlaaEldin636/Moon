package com.structure.base_mvvm.data.teacher_profile.data_source

import com.structure.base_mvvm.data.remote.BaseRemoteDataSource
import javax.inject.Inject

class TeacherProfileRemoteDataSource @Inject constructor(private val apiService: TeacherProfileServices) :
  BaseRemoteDataSource() {

  suspend fun teacherProfile(instructor_id: Int) = safeApiCall {
    apiService.teacherProfile(instructor_id)
  }
}