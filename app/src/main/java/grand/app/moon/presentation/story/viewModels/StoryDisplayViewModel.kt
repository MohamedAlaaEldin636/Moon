package grand.app.moon.presentation.story.viewModels

import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.base.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class StoryDisplayViewModel @Inject constructor(
) : BaseViewModel() {

  var progress = ObservableField(true)

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun startLoading() {
    progress.set(true)
  }
  fun stopLoading() {
    progress.set(false)
  }
}