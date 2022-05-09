package grand.app.moon.presentation.ads.adapter

import android.content.Context
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
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.presentation.ads.viewModels.ItemAdsViewModel
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import javax.inject.Inject

class AdsAdapter @Inject constructor(private val adsRepository: AdsRepository) : RecyclerView.Adapter<AdsAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<Advertisement> = SingleLiveEvent()
  var percentageAds = 90
  var grid = Constants.GRID_1


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
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_ads, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemAdsViewModel(data,percentageAds,adsRepository)
    holder.setViewModel(itemViewModel)



    holder.itemLayoutBinding.itemAdsContainer.setOnClickListener {
      holder.itemLayoutBinding.root.findNavController().navigate(
        R.id.nav_ads, bundleOf(
          "id" to data.id,
          "type" to type
        )
      )
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