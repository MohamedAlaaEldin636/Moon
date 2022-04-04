package grand.app.moon.presentation.store.viewModels

import grand.app.moon.domain.home.models.store.WorkingHours
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemWorkingHoursViewModel  constructor(var model: WorkingHours) : BaseViewModel(){
  val adapter = AdsAdapter()
  init {
  }
}