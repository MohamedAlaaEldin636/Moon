package grand.app.moon.presentation.filter.viewModels

import grand.app.moon.domain.home.models.store.WorkingHours
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemFilterRateViewModel  constructor(var model: AppTutorial, val selected: Boolean) : BaseViewModel(){
}