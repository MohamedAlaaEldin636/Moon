package com.structure.base_mvvm.presentation.teachers.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.teacher_profile.entity.TeacherProfile
import com.structure.base_mvvm.domain.teacher_profile.use_case.TeacherProfileUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TeacherProfileViewModel @Inject constructor(
  private val teacherProfileUseCase: TeacherProfileUseCase,
  savedStateHandle: SavedStateHandle
) : BaseViewModel() {

  @Bindable
  var teacherProfile: TeacherProfile = TeacherProfile()

  private val _profileResponse =
    MutableStateFlow<Resource<BaseResponse<TeacherProfile>>>(Resource.Default)
  val profileResponse = _profileResponse

  init {
    savedStateHandle.get<Int>("instructor_id")?.let { instructor_id ->
      getTeacherProfile(instructor_id)
    }
  }

  private fun getTeacherProfile(instructor_id: Int) {
    teacherProfileUseCase.invoke(instructor_id)
      .onEach { result ->
        println(result.toString())
        _profileResponse.value = result
      }
      .launchIn(viewModelScope)
  }

}