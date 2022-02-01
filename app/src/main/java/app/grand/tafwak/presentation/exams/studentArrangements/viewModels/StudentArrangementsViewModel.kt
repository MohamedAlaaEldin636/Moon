package app.grand.tafwak.presentation.exams.studentArrangements.viewModels

import app.grand.tafwak.domain.settings.repository.SettingsRepository
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentArrangementsViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel()