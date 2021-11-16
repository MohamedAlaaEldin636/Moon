package com.structure.base_mvvm.presentation.navigationDrawer

import com.structure.base_mvvm.presentation.base.BaseViewModel
import com.structure.base_mvvm.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.presentation.home.adapters.TeacherAdapter

class NavigationViewModel : BaseViewModel() {
  val showPrettyPopUp = SingleLiveEvent<Void>()
  val adapter: TeacherAdapter = TeacherAdapter()
}