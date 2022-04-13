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
import grand.app.moon.databinding.ItemUserListBinding
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.comment.entity.Comment
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.comment.viewmodel.ItemCommentViewModel
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel
import grand.app.moon.presentation.user.viewmodel.ItemUserViewModel

class UserListAdapter : RecyclerView.Adapter<UserListAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<String> = MutableLiveData()
  var position = -1
  private val differCallback = object : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_user_list, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemUserViewModel(data)


//    holder.itemLayoutBinding.lifecycleOwner?.let {
//      itemViewModel.submitEvent.observe(holder.itemLayoutBinding.root, {
//        this.position = position
//        clickEvent.value = it
//      })
//    }
    holder.setViewModel(itemViewModel)

  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<User>) {
    val array = ArrayList<User>(differ.currentList)
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

  fun add(comment: User) {
    this.differ.currentList.add(comment)
    notifyItemInserted(this.differ.currentList.size-1)
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemUserListBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemUserViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }


}