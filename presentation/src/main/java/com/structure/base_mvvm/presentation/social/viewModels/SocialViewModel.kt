package com.structure.base_mvvm.presentation.social.viewModels

import com.structure.base_mvvm.domain.settings.repository.SettingsRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SocialViewModel @Inject constructor(
  private val settingsRepository: SettingsRepository
) : BaseViewModel()