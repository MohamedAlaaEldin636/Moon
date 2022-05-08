package grand.app.moon.presentation.filter.viewModels

import grand.app.moon.domain.filter.entitiy.Children
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.home.models.store.WorkingHours
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.ads.adapter.AdsAdapter
import grand.app.moon.presentation.base.BaseViewModel

class ItemFilterRateViewModel  constructor(var model: Children, val position: Int = 0, val selected: Boolean) : BaseViewModel(){
}