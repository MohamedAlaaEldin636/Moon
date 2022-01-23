package com.structure.base_mvvm.presentation.teachers.compensationSessions.viewModels

import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TeacherCompensationSessionsViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel()