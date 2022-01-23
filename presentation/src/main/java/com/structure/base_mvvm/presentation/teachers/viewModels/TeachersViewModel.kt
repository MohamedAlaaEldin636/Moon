package com.structure.base_mvvm.presentation.teachers.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.student_teacher.entity.StudentTeacher
import com.structure.base_mvvm.domain.student_teacher.use_case.StudentTeacherUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.home.adapters.HomeSliderAdapter
import com.structure.base_mvvm.presentation.home.adapters.TeacherAdapter
import com.structure.base_mvvm.presentation.teachers.adapters.SubjectsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class TeachersViewModel @Inject constructor(
  private val studentTeacherUseCase: StudentTeacherUseCase
) : BaseViewModel() {
  @Bindable
  val adapter: TeacherAdapter = TeacherAdapter()
  @Bindable
  val subjectAdapter: SubjectsAdapter = SubjectsAdapter()
  val homeSliderAdapter: HomeSliderAdapter = HomeSliderAdapter()

  private val _teacherResponse =
    MutableStateFlow<Resource<BaseResponse<StudentTeacher>>>(Resource.Default)
  val teacherResponse = _teacherResponse

  init {
    getStudentTeacher()
  }

  private fun getStudentTeacher() {
    studentTeacherUseCase.invoke()
      .onEach { result ->
        println(result.toString())
        _teacherResponse.value = result
      }
      .launchIn(viewModelScope)
  }

  var studentTeacher: StudentTeacher = StudentTeacher()
    set(value) {
      adapter.differ.submitList(value.instructors)
      notifyPropertyChanged(BR.adapter)
      subjectAdapter.differ.submitList(value.subjects)
      notifyPropertyChanged(BR.subjectAdapter)
      homeSliderAdapter.update(value.sliders)
      field = value
    }
}