package grand.app.moon.presentation.ads.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemAdsBinding
import grand.app.moon.databinding.ItemAdsGridBinding
import grand.app.moon.databinding.ItemStoreLinearBinding
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.ads.viewModels.ItemAdsViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel
import javax.inject.Inject

class AdsAdapter @Inject constructor(private val adsRepository: AdsRepository) :
  RecyclerView.Adapter<AdsAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Advertisement> = SingleLiveEvent()
  var percentageAds = 90
  var grid = Constants.GRID_1


  override fun getItemViewType(position: Int): Int {
    return grid
  }

  //view 2 , search 5
  var type = 2

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
    return when (grid) {
      Constants.GRID_1 -> {
        val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.item_ads, parent, false)
        context = parent.context
        ViewHolder(view)
      }
      else -> {
        val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.item_ads_grid, parent, false)
        context = parent.context
        ViewHolder(view)
      }
    }
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemAdsViewModel(data, percentageAds, adsRepository)
    holder.setViewModel(itemViewModel)

    when (grid) {
      Constants.GRID_1 -> {
        holder.itemLayoutBinding.itemAdsContainer.setOnClickListener {
          holder.itemLayoutBinding.root.findNavController().navigate(
            R.id.nav_ads, bundleOf(
              "id" to data.id,
              "type" to type
            )
          )
        }
      }
      Constants.GRID_2 -> {
        holder.itemLayoutGridBinding.itemAdsContainer.setOnClickListener {
          holder.itemLayoutGridBinding.root.findNavController().navigate(
            R.id.nav_ads, bundleOf(
              "id" to data.id,
              "type" to type
            )
          )
        }
      }
    }

//, Constants.NAVIGATION_OPTIONS
//    //take-care
//    holder.itemLayoutBinding.icFav.setOnClickListener {
//      data.isFavorite = data.isFavorite != true
//      notifyItemChanged(position)
//    }
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

  fun insertData(insertList: List<Advertisement>) {
    val array = ArrayList<Advertisement>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    differ.submitList(array)
    notifyDataSetChanged()
  }


  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemAdsBinding
    lateinit var itemLayoutGridBinding: ItemAdsGridBinding

    init {
      bind()
    }

    fun bind() {
      Log.d(TAG, "bindHere: $grid")
      if (layoutPosition != -1) {
        when (grid) {
          Constants.GRID_1 -> {
            itemLayoutBinding = DataBindingUtil.bind(itemView)!!
          }
          else -> {
            itemLayoutGridBinding = DataBindingUtil.bind(itemView)!!
          }
        }
      }
    }

    fun unBind() {
      if (layoutPosition != -1) {
        when (grid) {
          Constants.GRID_1 -> {
            if (this::itemLayoutBinding.isInitialized) itemLayoutBinding.unbind()

          }
          else -> {
            if (this::itemLayoutGridBinding.isInitialized) itemLayoutGridBinding.unbind()
          }
        }
      }
    }


    fun setViewModel(itemViewModel: ItemAdsViewModel) {
      if (differ.currentList.isNotEmpty() && layoutPosition != -1) {
//        Log.d(TAG, "setViewModel: YEPR")
        when (grid) {
          Constants.GRID_1 -> {
            if (!this::itemLayoutBinding.isInitialized)
              itemLayoutBinding = DataBindingUtil.bind(itemView)!!
            itemLayoutBinding.viewModel = itemViewModel
          }
          else -> {
            if (!this::itemLayoutGridBinding.isInitialized)
              itemLayoutGridBinding = DataBindingUtil.bind(itemView)!!
            itemLayoutGridBinding.viewModel = itemViewModel
          }
        }
      }
    }

  }

}