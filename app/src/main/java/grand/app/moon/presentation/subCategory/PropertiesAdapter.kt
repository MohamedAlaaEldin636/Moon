package grand.app.moon.presentation.subCategory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCategoryPropertyBinding
import grand.app.moon.databinding.ItemCategoryTextBinding
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.subCategory.entity.Property
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.map.viewModel.ItemCategoryTextViewModel
import grand.app.moon.presentation.subCategory.viewModel.ItemPropertyViewModel

class PropertiesAdapter : RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {

  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Property> = SingleLiveEvent()
  var selected = -1
  var position = -1
  private val differCallback = object : DiffUtil.ItemCallback<Property>() {
    override fun areItemsTheSame(
      oldItem: Property,
      newItem: Property
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: Property,
      newItem: Property
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
      .inflate(R.layout.item_category_property, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemPropertyViewModel(data, position,selected == position)
    holder.setViewModel(itemViewModel)
    holder.itemLayoutBinding.tvItemCategoryText.setOnClickListener {

    }
  }

  fun submitSelect(){
    if(selected != -1) notifyItemChanged(selected)
    selected = position
    notifyItemChanged(selected)
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
    lateinit var itemLayoutBinding: ItemCategoryPropertyBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemPropertyViewModel) {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
      itemLayoutBinding.viewModel = itemViewModel
    }
  }

}