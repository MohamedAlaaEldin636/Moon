package grand.app.moon.presentation.ads.viewModels

import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.HomeResponse
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.ads.adapter.PropertyAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class AdsDetailsViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase
) : BaseViewModel() {
  @Bindable
  var propertiesAdapter = PropertyAdapter()
  @Bindable
  var reviewsAdapter = ReviewsAdapter()

  @Bindable
  val adsAdapter = AdsAdapter()

  private val _adsDetailsResponse =
    MutableStateFlow<Resource<BaseResponse<Advertisement>>>(Resource.Default)
  val adsDetailsResponse = _adsDetailsResponse

  val advertisement = ObservableField<Advertisement>()

  fun follow(v: View){

  }
}