package com.structure.base_mvvm.data.teacher_profile.repository

import com.structure.base_mvvm.data.teacher_profile.data_source.TeacherProfileRemoteDataSource
import com.structure.base_mvvm.domain.home.models.Instructor
import com.structure.base_mvvm.domain.teacher_profile.repository.TeacherProfileRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class TeacherProfileRepositoryImpl @Inject constructor(private val remoteDataSource: TeacherProfileRemoteDataSource) :
  TeacherProfileRepository {
  override suspend fun teacherProfile(instructor_id: Int): Resource<BaseResponse<Instructor>> =
    remoteDataSource.teacherProfile(instructor_id)

}