package app.grand.tafwak.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.domain.home.models.Classes
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.databinding.ItemGroupsBinding
import app.grand.tafwak.presentation.home.viewModels.ItemGroupsViewModel

class GroupsAdapter : RecyclerView.Adapter<GroupsAdapter.ViewHolder>() {
  var clickEvent: SingleLiveEvent<Classes> = SingleLiveEvent()
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
    itemViewModel.clickEvent.observeForever {
      clickEvent.value=data
      }
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