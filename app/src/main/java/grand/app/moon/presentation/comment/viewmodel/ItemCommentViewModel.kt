package grand.app.moon.presentation.comment.viewmodel

import android.util.Log
import android.view.View
import androidx.fragment.app.findFragment
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.comment.CommentsListFragment
import grand.app.moon.presentation.comment.CommentsListPaginateFragment

class ItemCommentViewModel(val model: Comment,val position:Int ,val userLocalUseCase: UserLocalUseCase) : BaseViewModel(){
//
//  @Inject
//  lateinit var userLocalUseCase: UserLocalUseCase

  private  val TAG = "ItemCommentViewModel"
  fun delete(v: View){
    v.findFragment<CommentsListFragment>().viewModel.deleteComment(position,model.id)
  }



  fun isOwnComment(): Boolean{
    return userLocalUseCase.invoke().id == model.user.id
//    if(model.)
  }
}