package com.structure.base_mvvm.presentation.group_details.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.structure.base_mvvm.domain.groups.entity.Scheduled
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.databinding.ItemSessionBinding
import com.structure.base_mvvm.presentation.group_details.viewModels.ItemSessionScheduledViewModel

class SessionsScheduledAdapter : RecyclerView.Adapter<SessionsScheduledAdapter.ViewHolder>() {
  private val differCallback = object : DiffUtil.ItemCallback<Scheduled>() {
    override fun areItemsTheSame(oldItem: Scheduled, newItem: Scheduled): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Scheduled, newItem: Scheduled): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_session, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemSessionScheduledViewModel(data)
    holder.setViewModel(itemViewModel)

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
    private lateinit var itemLayoutBinding: ItemSessionBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemSessionScheduledViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}