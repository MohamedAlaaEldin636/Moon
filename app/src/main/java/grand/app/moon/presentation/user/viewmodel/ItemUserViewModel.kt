package grand.app.moon.presentation.user.viewmodel

import android.view.View
import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.presentation.base.BaseViewModel

class ItemUserViewModel  constructor(val model: User) : BaseViewModel(){
  fun name() : String{
    return when(model.name){
      "" , null -> MyApplication.instance.getString(R.string.user)
      else -> model.name
    }
  }
}