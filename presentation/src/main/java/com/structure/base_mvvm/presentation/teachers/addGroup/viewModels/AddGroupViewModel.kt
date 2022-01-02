package com.structure.base_mvvm.presentation.teachers.addGroup.viewModels

import com.structure.base_mvvm.domain.search.repository.SearchRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddGroupViewModel @Inject constructor(
  private val searchRepository: SearchRepository
) : BaseViewModel()