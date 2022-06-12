package grand.app.moon.presentation.reviews.viewModels

import grand.app.moon.R
import grand.app.moon.core.MyApplication
import grand.app.moon.domain.home.models.review.Reviews
import grand.app.moon.presentation.base.BaseViewModel

class ItemReviewViewModel  constructor(val reviews: Reviews) : BaseViewModel(){
  fun name() : String{
    return when(reviews.user.name){
      "" , null -> MyApplication.instance.getString(R.string.user)
      else -> reviews.user.name.toString()
    }
  }
}