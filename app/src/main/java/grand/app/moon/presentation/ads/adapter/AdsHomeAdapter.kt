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
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.ItemAdsHomeBinding
import grand.app.moon.domain.ads.repository.AdsRepository
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.CategoryAdvertisement
import grand.app.moon.presentation.ads.viewModels.ItemAdsHomeViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import javax.inject.Inject

class AdsHomeAdapter @Inject constructor(private val adsRepository: AdsRepository) :
  RecyclerView.Adapter<AdsHomeAdapter.ViewHolder>() {
  lateinit var context: Context
  var clickEvent: SingleLiveEvent<CategoryAdvertisement> = SingleLiveEvent()

  private val differCallback = object : DiffUtil.ItemCallback<CategoryAdvertisement>() {
    override fun areItemsTheSame(
      oldItem: CategoryAdvertisement,
      newItem: CategoryAdvertisement
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: CategoryAdvertisement,
      newItem: CategoryAdvertisement
    ): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.item_ads_home, parent, false)
    context = parent.context
    return ViewHolder(view)
  }

  private val TAG = "AdsHomeAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemAdsHomeViewModel(data, adsRepository)
    holder.itemLayoutBinding.rvAds.isNestedScrollingEnabled = false;

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

  var check = false
  fun updateFavourite() {
    differ.currentList.forEachIndexed { indexCategoryAds, categoryAds ->
      categoryAds.advertisements.forEachIndexed { indexAds, ads ->
        check = false
        if (ListHelper.isExistAdvertisement(ads.id)) {
          check = true
          val advertisement = ListHelper.getAdvertisement(ads.id)
          differ.currentList[indexCategoryAds].advertisements[indexAds].shareCount = advertisement.shareCount
          differ.currentList[indexCategoryAds].advertisements[indexAds].isFavorite = advertisement.isFavorite
          differ.currentList[indexCategoryAds].advertisements[indexAds].favoriteCount = advertisement.favoriteCount
          differ.currentList[indexCategoryAds].advertisements[indexAds].viewsCount = advertisement.viewsCount
        }
//        if (ListHelper.isExist(ads.id) && ads.isFavorite != ListHelper.getFavourite(ads.id)) {
//          check = true
//          val boolean = ListHelper.getFavourite(ads.id)
//          differ.currentList[indexCategoryAds].advertisements[indexAds].isFavorite = boolean
//        }
        if(ads.store.isFollowing != ListHelper.getFollowStore(ads.store.id)){
          check = true
          ads.store.isFollowing = ListHelper.getFollowStore(ads.store.id)
        }
        if (check) notifyItemChanged(indexCategoryAds)

      }
    }
  }

  fun checkBlockStore() {
    ListHelper.blockStores.forEach {
      Log.d(TAG, "blockStoreHERE: blockStores $it")
    }
    var isBlocked = false
    val list = ArrayList<CategoryAdvertisement>(differ.currentList)
    differ.currentList.forEachIndexed { indexCategoryAds, categoryAds ->
      val listAds = ArrayList<Advertisement>()
      categoryAds.advertisements.forEachIndexed { indexAds, ads ->
        if (!ListHelper.checkBlockStore(ads.store.id)) {
          isBlocked = true
          Log.d(TAG, "checkBlockStore: ADDEDD to unBlock")
          listAds.add(ads)
        }
      }

      Log.d(TAG, "checkBlockStore=>>>>>>>>>>>>: ${list[indexCategoryAds].advertisements.size}")
      list[indexCategoryAds].advertisements.clear()
      list[indexCategoryAds].advertisements.addAll(listAds)
      Log.d(TAG, "checkBlockStore=>>>>>>>>>>>>: ${list[indexCategoryAds].advertisements.size}")
    }
    if (isBlocked) {
      Log.d(TAG, "checkBlockStore: FINISH")
      differ.submitList(null)
      differ.submitList(list)
    }
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemAdsHomeBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemAdsHomeViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }

}