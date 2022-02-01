package app.grand.tafwak.presentation.teachers.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.home.models.Instructor
import app.grand.tafwak.domain.teacher_profile.use_case.TeacherProfileUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.home.adapters.GroupsAdapter
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
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
  val groupsAdapter: GroupsAdapter = GroupsAdapter()

  @Bindable
  val reviewsAdapter: ReviewsAdapter = ReviewsAdapter()

  @Bindable
  var teacherProfile: Instructor = Instructor()
    set(value) {
      groupsAdapter.differ.submitList(value.classes)
      notifyPropertyChanged(BR.groupsAdapter)
      reviewsAdapter.differ.submitList(value.reviews)
      notifyPropertyChanged(BR.reviewsAdapter)

      notifyPropertyChanged(BR.teacherProfile)
      field = value
    }


  private val _profileResponse =
    MutableStateFlow<Resource<BaseResponse<Instructor>>>(Resource.Default)
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