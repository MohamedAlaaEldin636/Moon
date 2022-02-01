package app.grand.tafwak.presentation.teachers.acceptanceTest.viewModels

import app.grand.tafwak.domain.settings.repository.SettingsRepository
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AcceptanceTestViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel()