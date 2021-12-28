package com.structure.base_mvvm.presentation.exams.examFirstStep.viewModels

import com.structure.base_mvvm.domain.search.repository.SearchRepository
import com.structure.base_mvvm.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExamFirstStepViewModel @Inject constructor(
  private val searchRepository: SearchRepository
) : BaseViewModel()