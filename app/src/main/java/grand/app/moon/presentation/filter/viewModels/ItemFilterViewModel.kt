package grand.app.moon.presentation.filter.viewModels

import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.filter.FILTER_TYPE


class ItemFilterViewModel  constructor(val model: FilterProperty) : BaseViewModel(){

  fun isEditText(): Boolean{
    return when(model.filterType){
      FILTER_TYPE.PRICE -> true
      else -> false
    }
  }
}