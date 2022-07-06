package grand.app.moon.presentation.media.image

import androidx.databinding.ObservableField
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class ZoomViewModel @Inject constructor(
  private val useCase: ExploreUseCase,
) : BaseViewModel() {
  val image = ObservableField("")
  val images = ArrayList<String>()
  private  val TAG = "ZoomViewModel"
  fun setImages(images: List<String>) {
    this.images.addAll(images)
    notifyChange()
  }


}