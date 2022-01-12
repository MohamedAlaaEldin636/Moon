package com.structure.base_mvvm.presentation.auth.countries.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import com.structure.base_mvvm.domain.countries.entity.Country
import com.structure.base_mvvm.domain.countries.use_case.CountriesUseCase
import com.structure.base_mvvm.domain.utils.BaseResponse
import com.structure.base_mvvm.domain.utils.Resource
import com.structure.base_mvvm.presentation.BR
import com.structure.base_mvvm.presentation.auth.countries.adapters.CountriesAdapter
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class CountriesViewModel @Inject constructor(
  private val countriesUseCase: CountriesUseCase
) :
  BaseViewModel() {
  private val _countriesPasswordResponse =
    MutableStateFlow<Resource<BaseResponse<List<Country>>>>(Resource.Default)
  val countriesPasswordResponse = _countriesPasswordResponse
  private val _registerResponse =
    MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val registerResponse = _registerResponse

  @Bindable
  val adapter: CountriesAdapter = CountriesAdapter()

  init {
    getCountries()
  }

  private fun getCountries() {
    countriesUseCase.invoke()
      .onEach { result ->
        _countriesPasswordResponse.value = result
      }
      .launchIn(viewModelScope)
  }

   fun registerStep2() {
    if (adapter.lastSelected != -1) {
      countriesUseCase.registerStep2(adapter.lastSelected)
        .onEach { result ->
          _registerResponse.value = result
        }
        .launchIn(viewModelScope)
    }
  }

  fun updateAdapter(countries: List<Country>) {
    adapter.differ.submitList(countries)
    notifyPropertyChanged(BR.adapter)
  }

}