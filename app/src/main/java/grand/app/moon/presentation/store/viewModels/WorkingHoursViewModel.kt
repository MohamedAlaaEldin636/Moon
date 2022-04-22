package grand.app.moon.presentation.store.viewModels

import androidx.databinding.Bindable
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.ads.use_case.AdsUseCase
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.adapter.WorkingHoursAdapter
import javax.inject.Inject


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