package grand.app.moon.presentation.ads.adapter

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
import grand.app.moon.databinding.ItemAdsBinding
import grand.app.moon.databinding.ItemStoryBinding
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.story.entity.StoryItem
import grand.app.moon.presentation.ads.viewModels.ItemAdsHomeViewModel
import grand.app.moon.presentation.ads.viewModels.ItemAdsViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.story.viewModels.ItemStoryViewModel

class AdsAdapter : RecyclerView.Adapter<AdsAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Advertisement> = SingleLiveEvent()

  private val differCallback = object : DiffUtil.ItemCallback<Advertisement>() {
    override fun areItemsTheSame(
      oldItem: Advertisement,
      newItem: Advertisement
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: Advertisement,
      newItem: Advertisement
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_ads, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private  val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemAdsViewModel(data)
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
    lateinit var itemLayoutBinding: ItemAdsBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemAdsViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }

}