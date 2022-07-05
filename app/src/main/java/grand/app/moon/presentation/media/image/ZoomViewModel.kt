package grand.app.moon.presentation.media.image

import android.os.Bundle
import android.util.Log
import androidx.annotation.NonNull
import androidx.databinding.ObservableField
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.media.image.pager.TabModel
import java.util.ArrayList
import javax.inject.Inject

@HiltViewModel
class ZoomViewModel @Inject constructor(
  private val useCase: ExploreUseCase,
) : BaseViewModel() {
  val image = ObservableField("")
  var tabModels: ArrayList<TabModel> = ArrayList<TabModel>()
  private  val TAG = "ZoomViewModel"
  fun setImages(images: List<String>) {
    Log.d(TAG, "setImages: " + images.size)
    tabModels.clear()
    for (image in images) {
      val zoomFragment = ZoomFragment()
      val bundle = Bundle()
      bundle.putString(Constants.IMAGE, image)
      zoomFragment.arguments = bundle
      tabModels.add(TabModel("", zoomFragment))
    }
  }


}