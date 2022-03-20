package grand.app.moon.presentation.shared.web_view

import grand.app.moon.domain.general.repository.GeneralRepository
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(private val generalRepository: GeneralRepository) :
  BaseViewModel()