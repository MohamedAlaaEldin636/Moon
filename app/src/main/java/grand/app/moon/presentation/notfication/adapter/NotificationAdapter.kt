package com.structure.base_mvvm.presentation.notification.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemNotificationBinding
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.notfication.viewmodel.ItemNotificationViewModel

class NotificationAdapter: RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {
  var clickEvent: SingleLiveEvent<NotificationData> = SingleLiveEvent()
  private val differCallback = object : DiffUtil.ItemCallback<NotificationData>() {
    override fun areItemsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: NotificationData, newItem: NotificationData): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_notification, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemNotificationViewModel(data)
    holder.setViewModel(itemViewModel)
  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<NotificationData>) {
    val array = ArrayList<NotificationData>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem(i: Int){
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

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemNotificationBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemNotificationViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }


}