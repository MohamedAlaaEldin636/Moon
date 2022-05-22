package grand.app.moon.presentation.comment.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.presentation.reviews.viewModels.ItemReviewViewModel
import grand.app.moon.R
import grand.app.moon.databinding.ItemCommentBinding
import grand.app.moon.databinding.ItemReviewBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.presentation.comment.viewmodel.ItemCommentViewModel

class CommentListAdapter(val userLocalUseCase: UserLocalUseCase) : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
  var position = -1
  private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
      return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
      return oldItem == newItem
    }
  }
    val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCommentViewModel(data,position,userLocalUseCase)
    holder.setViewModel(itemViewModel)

  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: Comment) {
    val array = ArrayList<Comment>(differ.currentList)
    val size = array.size
    array.add(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(null)
    differ.submitList(array)
//    notifyItemInserted(differ.currentList.size - 1)
  }

  fun insertData(insertList: List<Comment>) {
    val array = ArrayList<Comment>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }



  override fun getItemCount(): Int {
    return differ.currentList.size
  }

  override fun onViewAttachedToWindow(holder: ViewHolder) {
    super.onViewAttachedToWindow(holder)
    holder.bind()
  }

  override fun onViewDetachedFromWindow(holder: ViewHolder) {
    super.onViewDetachedFromWindow(holder)
    holder.unBind()
  }

    fun add(review: Comment) {
      differ.currentList.add(review)
      val array = ArrayList<Comment>(differ.currentList)
      val size = array.size
      array.add(review)
      Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
      differ.submitList(array)
      notifyDataSetChanged()
    }

  fun deleteItem() {
    if(position != -1 && position < differ.currentList.size){
      val array = ArrayList<Comment>(differ.currentList)
      array.removeAt(position)
      differ.submitList(array)
      notifyItemRemoved(position)
    }
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private lateinit var itemLayoutBinding: ItemCommentBinding

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