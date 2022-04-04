package grand.app.moon.presentation.store.viewModels

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import app.grand.tafwak.presentation.reviews.adapters.ReviewsAdapter
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.entity.AddFavouriteAdsRequest
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.ads.adapter.PropertyAdapter
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.openBrowser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.lang.Exception
import java.net.URLEncoder
import javax.inject.Inject
import androidx.core.content.ContextCompat.startActivity
import grand.app.moon.presentation.store.StoreDetailsFragmentDirections
import grand.app.moon.presentation.store.adapter.WorkingHoursAdapter


@HiltViewModel
class WorkingHoursViewModel @Inject constructor(
  val userLocalUseCase: UserLocalUseCase,
  private val useCase: AdsUseCase,
  private val storeUseCase: StoreUseCase
) : BaseViewModel() {
  var id: Int = -1


  @Bindable
  val adapter = WorkingHoursAdapter()
}