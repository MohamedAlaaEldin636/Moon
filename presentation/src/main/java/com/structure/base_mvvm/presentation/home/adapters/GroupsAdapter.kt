package com.structure.base_mvvm.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.structure.base_mvvm.domain.home.models.Classes
import com.structure.base_mvvm.presentation.R
import com.structure.base_mvvm.presentation.databinding.ItemGroupsBinding
import com.structure.base_mvvm.presentation.home.viewModels.ItemGroupsViewModel

class GroupsAdapter : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
  private val differCallback = object : DiffUtil.ItemCallback<Classes>() {
    override fun areItemsTheSame(oldItem: Classes, newItem: Classes): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Classes, newItem: Classes): Boolean {
      return oldItem == newItem
    }
  }
    val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_groups, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemGroupsViewModel(data)
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
    private lateinit var itemLayoutBinding: ItemGroupsBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemGroupsViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}