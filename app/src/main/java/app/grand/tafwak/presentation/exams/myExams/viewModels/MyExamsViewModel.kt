package app.grand.tafwak.presentation.exams.myExams.viewModels

import app.grand.tafwak.domain.settings.repository.SettingsRepository
import app.grand.tafwak.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyExamsViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel()