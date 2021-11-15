package com.structure.base_mvvm.presentation.navigationDrawer

import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.home.models.HomeData
import com.structure.base_mvvm.domain.home.models.HomePaginateData
import com.structure.base_mvvm.domain.home.use_case.HomeUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.presentation.home.adapters.HomeAdapter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class NavigationViewModel : BaseViewModel() {
  val showPrettyPopUp = SingleLiveEvent<Void>()
  val adapter: HomeAdapter = HomeAdapter()
}