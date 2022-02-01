package app.grand.tafwak.presentation.shared.web_view

import app.grand.tafwak.domain.general.repository.GeneralRepository
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(private val generalRepository: GeneralRepository) :
  BaseViewModel()