package app.grand.tafwak.presentation.group_details.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.domain.groups.entity.GroupDetails
import app.grand.tafwak.domain.groups.use_case.GroupDetailsUseCase
import app.grand.tafwak.domain.utils.BaseResponse
import app.grand.tafwak.domain.utils.Resource
import com.structure.base_mvvm.BR
import app.grand.tafwak.presentation.base.BaseViewModel
import app.grand.tafwak.presentation.group_details.adapters.SessionsScheduledAdapter
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GroupDetailsViewModel @Inject constructor(
  private val groupDetailsUseCase: GroupDetailsUseCase,
  savedStateHandle: SavedStateHandle
) : BaseViewModel() {
  @Bindable
  val reviewsAdapter: ReviewsAdapter = ReviewsAdapter()

  @Bindable
  val scheduledAdapter = SessionsScheduledAdapter()

  @Bindable
  var groupDetails: GroupDetails = GroupDetails()
    set(value) {
      reviewsAdapter.differ.submitList(value.instructor.reviews)
      notifyPropertyChanged(BR.reviewsAdapter)
      scheduledAdapter.differ.submitList(value.scheduled)
      notifyPropertyChanged(BR.scheduledAdapter)

      notifyPropertyChanged(BR.groupDetails)
      field = value
    }
  private val _detailsResponse =
    MutableStateFlow<Resource<BaseResponse<GroupDetails>>>(Resource.Default)
  val detailsResponse = _detailsResponse

  init {
    savedStateHandle.get<Int>("group_id")?.let { group_id ->
      getGroupDetails(group_id)
    }
  }

  private fun getGroupDetails(group_id: Int) {
    groupDetailsUseCase.invoke(group_id)
      .onEach { result ->
        println(result.toString())
        _detailsResponse.value = result
      }
      .launchIn(viewModelScope)
  }

}