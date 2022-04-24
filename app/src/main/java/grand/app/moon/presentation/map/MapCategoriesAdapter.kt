package grand.app.moon.presentation.map

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCategoryTextBinding
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.map.viewModel.ItemCategoryTextViewModel

class MapCategoriesAdapter : RecyclerView.Adapter<MapCategoriesAdapter.ViewHolder>() {

  lateinit var context: Context
  var clickEvent: SingleLiveEvent<CategoryItem> = SingleLiveEvent()
  var selected = -1
  private val differCallback = object : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(
      oldItem: CategoryItem,
      newItem: CategoryItem
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: CategoryItem,
      newItem: CategoryItem
    ): Boolean {
      return oldItem == newItem
    }
  }


  override fun getItemId(position: Int): Long {
    return super.getItemId(position)
  }

  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_category_text, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCategoryTextViewModel(data, selected == position)
    holder.setViewModel(itemViewModel)
    holder.itemLayoutBinding.tvItemCategoryText.setOnClickListener {
      if(selected != -1) notifyItemChanged(selected)
      selected = position
      clickEvent.value = data
      notifyItemChanged(selected)
    }
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
    lateinit var itemLayoutBinding: ItemCategoryTextBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemCategoryTextViewModel) {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
      itemLayoutBinding.viewModel = itemViewModel
    }
  }

}