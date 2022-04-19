package grand.app.moon.presentation.category.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCategoryFilterBinding
import grand.app.moon.databinding.ItemCityBinding
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.auth.countries.viewModels.ItemCityViewModel
import grand.app.moon.presentation.category.viewModels.ItemCategoryFilterViewModel

class CategoriesFilterAdapter : RecyclerView.Adapter<CategoriesFilterAdapter.ViewHolder>() {
  var lastSelected = -1
  var lastPosition = -1
  val selected = ArrayList<Int>()
  var changeEvent: SingleLiveEvent<CategoryItem> = SingleLiveEvent()
  private val differCallback = object : DiffUtil.ItemCallback<CategoryItem>() {
    override fun areItemsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CategoryItem, newItem: CategoryItem): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_category_filter, parent, false)
    return ViewHolder(view)
  }

  private val TAG = "CountriesAdapter"

  @SuppressLint("RecyclerView")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCategoryFilterViewModel(data, this.selected.contains(data.id))
    holder.itemLayoutBinding.llCategoryFilter.setOnClickListener {
      if (this.selected.contains(data.id)) {
        this.selected.remove(data.id)
      } else {
        data.id?.let { it1 -> this.selected.add(it1) }
      }
      notifyItemChanged(position)
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

  fun submitList(data: List<CategoryItem>) {
    differ.submitList(data)
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemCategoryFilterBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemCategoryFilterViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }
}