package app.grand.tafwak.data.studentTeacher.repository

import app.grand.tafwak.data.studentTeacher.data_source.StudentTeacherRemoteDataSource
import app.grand.tafwak.domain.student_teacher.entity.StudentTeacher
import app.grand.tafwak.domain.student_teacher.repository.StudentTeacherRepository
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import javax.inject.Inject

class StudentTeacherRepositoryImpl @Inject constructor(private val remoteDataSource: StudentTeacherRemoteDataSource) :
  StudentTeacherRepository {
  override suspend fun studentTeacher(): Resource<BaseResponse<StudentTeacher>> =
    remoteDataSource.studentTeacher()

}