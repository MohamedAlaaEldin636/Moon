package com.structure.base_mvvm.domain.teacher_profile.repository

import com.structure.base_mvvm.domain.teacher_profile.entity.TeacherProfile
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource

interface TeacherProfileRepository {
  suspend fun teacherProfile(instructor_id: Int): Resource<BaseResponse<TeacherProfile>>

}