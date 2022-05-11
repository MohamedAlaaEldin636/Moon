package grand.app.moon.presentation.comment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCommentBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.comment.viewmodel.ItemCommentViewModel
import javax.inject.Inject

class CommentListUpdateAdapter @Inject constructor(val userLocalUseCase: UserLocalUseCase) : PagingDataAdapter<Comment, CommentListUpdateAdapter.ViewHolder>(COMPARATOR) {

  companion object {

    val COMPARATOR = object : DiffUtil.ItemCallback<Comment>() {
      override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean =
        oldItem.id == newItem.id

      override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean =
        oldItem == newItem

    }
  }

  override fun getItemCount(): Int {
    return super.getItemCount().also {
      Log.d(TAG, "getItemCount: $it")
    }
  }


  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    return ViewHolder(view)
  }
  private  val TAG = "CommentListUpdateAdapte"

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = getItem(position)
    Log.d(TAG, "onBindViewHolder: HERE ${data?.comment}")
    val itemViewModel = data?.let { ItemCommentViewModel(it,userLocalUseCase) }
    holder.itemLayoutBinding.appCompatImageView.setOnClickListener {
//      clickEvent.value = Constants.DELETE

//      oldListItem.removeAt(position)
//      differ.submitList(list)
//      notifyItemRemoved(position)
    }
    itemViewModel?.let { holder.setViewModel(it) }
  }


  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemCommentBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemCommentViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }


}