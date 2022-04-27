package grand.app.moon.presentation.category.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCategoryBinding
import grand.app.moon.databinding.ItemCategoryTotalAdsBinding
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.category.viewModels.ItemCategoryViewModel

class CategoriesAdapter : RecyclerView.Adapter<CategoriesAdapter.ViewHolder>() {

  lateinit var context: Context
  var clickEvent: SingleLiveEvent<CategoryItem> = SingleLiveEvent()
  var percentage = 100
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

  override fun getItemViewType(position: Int): Int {
    return when(differ.currentList[position].total){
      null -> Constants.ADVERTISEMENT
      else -> Constants.TOTAL_ADS
    }
  }

  override fun getItemId(position: Int): Long {
    return super.getItemId(position)
  }

  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    if(viewType == Constants.ADVERTISEMENT) {
      Log.d(TAG, "onCreateViewHolder: ")
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_category, parent, false)
      context = parent.context
      return ViewHolder(view)
    }else{
      val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_category_total_ads, parent, false)
      context = parent.context
      return ViewHolder(view)
    }
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    Log.d(TAG, "onBindViewHolder: $position")
    val data = differ.currentList[position]
    val itemViewModel = ItemCategoryViewModel(data,percentage)
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
    lateinit var itemLayoutBinding: ItemCategoryBinding
    lateinit var itemLayoutTotalAdsBinding: ItemCategoryTotalAdsBinding

    init {
      bind()
    }

    fun bind() {
      Log.d(TAG, "bind: $layoutPosition")
      if(layoutPosition != -1) {
        if(differ.currentList.isNotEmpty() && layoutPosition < differ.currentList.size) {
          if (differ.currentList[layoutPosition].total == null) {
            Log.d(TAG, "bind: YES")
            itemLayoutBinding = DataBindingUtil.bind(itemView)!!
          } else
            itemLayoutTotalAdsBinding = DataBindingUtil.bind(itemView)!!
        }
      }
    }

    fun unBind() {
      if(layoutPosition != -1) {
        if(differ.currentList.isNotEmpty() && layoutPosition < differ.currentList.size) {
          if (differ.currentList[layoutPosition].total == null)
            itemLayoutBinding.unbind()
          else
            itemLayoutTotalAdsBinding.unbind()
        }
      }



    }

    fun setViewModel(itemViewModel: ItemCategoryViewModel) {
      Log.d(TAG, "setViewModel: $layoutPosition")
      if(differ.currentList.isNotEmpty() && layoutPosition != -1 ) {
        Log.d(TAG, "setViewModel: YEPR")
        if (differ.currentList[layoutPosition].total == null) {
          if(!this::itemLayoutBinding.isInitialized)
            itemLayoutBinding = DataBindingUtil.bind(itemView)!!
          itemLayoutBinding.itemViewModels = itemViewModel
        }else{
          if(!this::itemLayoutTotalAdsBinding.isInitialized)
            itemLayoutTotalAdsBinding = DataBindingUtil.bind(itemView)!!
          itemLayoutTotalAdsBinding.itemViewModels = itemViewModel
        }
      }
    }
  }

}