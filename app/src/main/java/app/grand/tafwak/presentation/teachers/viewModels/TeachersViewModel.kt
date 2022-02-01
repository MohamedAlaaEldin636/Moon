package app.grand.tafwak.presentation.teachers.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.student_teacher.entity.StudentTeacher
import app.grand.tafwak.domain.student_teacher.use_case.StudentTeacherUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.home.adapters.HomeSliderAdapter
import app.grand.tafwak.presentation.home.adapters.TeacherAdapter
import app.grand.tafwak.presentation.teachers.adapters.SubjectsAdapter
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
      adapter.differ.submitList(value.instructors.instructor)
      notifyPropertyChanged(BR.adapter)
      subjectAdapter.differ.submitList(value.subjects)
      notifyPropertyChanged(BR.subjectAdapter)
      homeSliderAdapter.update(value.sliders)
      field = value
    }
}