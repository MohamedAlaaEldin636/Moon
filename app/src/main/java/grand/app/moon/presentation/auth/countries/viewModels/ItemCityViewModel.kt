package grand.app.moon.presentation.auth.countries.viewModels

import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.presentation.base.BaseViewModel

class ItemCityViewModel  constructor(val country: Country,var selected : Boolean) : BaseViewModel()