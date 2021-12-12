package com.structure.base_mvvm.presentation.splash

import com.structure.base_mvvm.domain.general.use_case.GeneralUseCases
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val generalUseCases: GeneralUseCases) :
  BaseViewModel() {
  val image = "https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885__4890.jpg"
  fun isFirstTime() = generalUseCases.checkFirstTimeUseCase()
}