package com.structure.base_mvvm.presentation.notification.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCommentBinding
import grand.app.moon.databinding.ItemExploreListBinding
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.comment.viewmodel.ItemCommentViewModel
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel

class CommentListAdapter : RecyclerView.Adapter<CommentListAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  var position = -1
  private val differCallback = object : DiffUtil.ItemCallback<Comment>() {
    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCommentViewModel(data)
    holder.itemLayoutBinding.appCompatImageView.setOnClickListener {
//      clickEvent.value = Constants.DELETE
      clickEvent.value = data.id
      val list = ArrayList(differ.currentList)
      list.removeAt(position)
      differ.submitList(list)
      notifyItemRemoved(position)
    }


//    holder.itemLayoutBinding.lifecycleOwner?.let {
//      itemViewModel.submitEvent.observe(holder.itemLayoutBinding.root, {
//        this.position = position
//        clickEvent.value = it
//      })
//    }
    holder.setViewModel(itemViewModel)

  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<Comment>) {
    val array = ArrayList<Comment>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: " + size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem(i: Int) {
    val array = ArrayList(differ.currentList)
    array.remove(array[i])
    differ.submitList(array)
    notifyItemRemoved(i)
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

  fun add(comment: Comment) {
//    differ.submitList()
    val array = ArrayList(differ.currentList)
    array.add(comment)
    differ.submitList(array)
//    notifyItemInserted(this.differ.currentList.size-1)
    notifyDataSetChanged()
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