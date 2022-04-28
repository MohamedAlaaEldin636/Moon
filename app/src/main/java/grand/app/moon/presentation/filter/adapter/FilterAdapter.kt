package grand.app.moon.presentation.filter.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemFilterBinding
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.presentation.filter.FILTER_TYPE
import grand.app.moon.presentation.filter.viewModels.ItemFilterViewModel

class FilterAdapter : RecyclerView.Adapter<FilterAdapter.ViewHolder>() {
  lateinit var context: Context
  var position = -1
  var clickEvent: MutableLiveData<FilterProperty> = MutableLiveData()

  private val differCallback = object : DiffUtil.ItemCallback<FilterProperty>() {
    override fun areItemsTheSame(
      oldItem: FilterProperty,
      newItem: FilterProperty
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: FilterProperty,
      newItem: FilterProperty
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_filter, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemFilterViewModel(data)
    holder.itemLayoutBinding.itemMore.setOnClickListener {
      this.position = position
      clickEvent.value = data
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

  fun updateFilterSelected(property: FilterProperty) {
    Log.d(TAG, "updateFilterSelected: ${property.selectedText}")
    Log.d(TAG, "updateFilterSelected: ${property.selectedList.size}")
    Log.d(TAG, "updateFilterSelected: ${position}")
    differ.currentList[position].selectedText = property.selectedText
    differ.currentList[position].selectedList = property.selectedList
    notifyItemChanged(position)
  }

  fun replaceSubCategories(it: FilterProperty) {
    differ.currentList.forEachIndexed { index, filterProperty ->
      run {
        if (filterProperty.filterType == FILTER_TYPE.SUB_CATEGORY) {
          filterProperty.children.clear()
          filterProperty.children.addAll(it.children)
          notifyItemChanged(index)
        }
      }
    }
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemFilterBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemFilterViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }

}