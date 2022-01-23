package com.structure.base_mvvm.data.studentTeacher.repository

import com.structure.base_mvvm.data.studentTeacher.data_source.StudentTeacherRemoteDataSource
import com.structure.base_mvvm.domain.student_teacher.entity.StudentTeacher
import com.structure.base_mvvm.domain.student_teacher.repository.StudentTeacherRepository
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import javax.inject.Inject

class StudentTeacherRepositoryImpl @Inject constructor(private val remoteDataSource: StudentTeacherRemoteDataSource) :
  StudentTeacherRepository {
  override suspend fun studentTeacher(): Resource<BaseResponse<StudentTeacher>> =
    remoteDataSource.studentTeacher()

}