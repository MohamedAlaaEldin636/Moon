package grand.app.moon.presentation.store.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.ItemStoreBinding
import grand.app.moon.databinding.ItemStoreLinearBinding
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.domain.store.entity.FollowStoreRequest
import grand.app.moon.domain.store.use_case.StoreUseCase
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.auth.AuthActivity
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel
import kotlinx.coroutines.flow.collect

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
  var adapterType: Int = -1
  lateinit var context: Context
  var isLogin = false
  lateinit var useCase : StoreUseCase
  var submitEvent: MutableLiveData<String> = MutableLiveData()
  var percentage = 100
  var position = 0
  var grid = Constants.GRID_2
  var type = 3
  private val differCallback = object : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(
      oldItem: Store,
      newItem: Store
    ): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
      oldItem: Store,
      newItem: Store
    ): Boolean {
      return oldItem == newItem
    }
  }

  override fun getItemViewType(position: Int): Int {
    return grid
  }


  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    return when (grid) {
      Constants.GRID_1 -> {
        val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.item_store_linear, parent, false)
        context = parent.context
        ViewHolder(view)
      }
      else -> {
        val view = LayoutInflater.from(parent.context)
          .inflate(R.layout.item_store, parent, false)
        context = parent.context
        ViewHolder(view)
      }
    }
  }

  private val TAG = "MoreAdapter"
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//    Log.d(TAG, "onBindViewHolder: $position")
    val data = differ.currentList[position]
    val itemViewModel = ItemStoreViewModel(data,type, percentage,useCase,position,adapterType)
    holder.setViewModel(itemViewModel)
    itemViewModel.submitEvent.observeForever{
//          Log.d(TAG, "onBindViewHolder: HAY THERE")
      data.isFollowing = data.isFollowing != true
      this.position = position
      //            submitEvent.value = Constants.FOLLOW
      notifyItemChanged(position)
    }
    holder.setViewModel(itemViewModel)

  }


  fun changeFollowingText(){
    val list = ArrayList(differ.currentList)
    list[position].isFollowing = !list[position].isFollowing.orFalse()
    ListHelper.addFollowStore(list[position].id.orZero(),list[position].isFollowing.orFalse())
    notifyItemChanged(position)
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

  fun insertData(list: ArrayList<Store>) {
    val array = ArrayList<Store>(differ.currentList)
    val size = array.size
    array.addAll(list)
    differ.submitList(array)
//    notifyDataSetChanged()
  }

  fun setFollowing(storeId: Int, boolean: Boolean) {
    differ.currentList.forEachIndexed{ index, store ->
      if(differ.currentList[index].id == storeId && differ.currentList[index].isFollowing != boolean){
        differ.currentList[index].isFollowing = boolean
        notifyItemChanged(index)
      }
    }
  }

  fun checkBlockStore() {
    val array = ArrayList(differ.currentList)
    differ.currentList.forEachIndexed{ index, store ->
      if(ListHelper.checkBlockStore(differ.currentList[index].id.orZero())){
        array.removeAt(index)
//        notifyItemRemoved(index)
      }
    }
    if(array.size != differ.currentList.size) {
      differ.submitList(null)
      differ.submitList(array)
//      notifyItemRangeChanged(0,differ.currentList.size)
    }
  }
  fun checkFollowingStore(){
    differ.currentList.forEachIndexed { index, store ->
      if (store.isFollowing != ListHelper.getFollowStore(store.id.orZero())) {
        store.isFollowing = ListHelper.getFollowStore(store.id.orZero())
        notifyItemChanged(index)
      }
    }
  }

    inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemStoreBinding
    lateinit var itemLayoutLinearBinding: ItemStoreLinearBinding

    init {
      bind()
    }

    fun bind() {
      if (layoutPosition != -1) {
        when (grid) {
          Constants.GRID_1 -> {
            itemLayoutLinearBinding = DataBindingUtil.bind(itemView)!!
          }
          else -> {
            itemLayoutBinding = DataBindingUtil.bind(itemView)!!
          }
        }
      }
    }

    fun unBind() {
      if (layoutPosition != -1) {
        when (grid) {
          Constants.GRID_1 -> {
            if (this::itemLayoutLinearBinding.isInitialized)
              itemLayoutLinearBinding.unbind()
          }
          else -> {
            if (this::itemLayoutBinding.isInitialized)
              itemLayoutBinding.unbind()
          }
        }
      }
    }

    fun setViewModel(itemViewModel: ItemStoreViewModel) {
      if (differ.currentList.isNotEmpty() && layoutPosition != -1) {
//        Log.d(TAG, "setViewModel: YEPR")
        when (grid) {
          Constants.GRID_1 -> {
            if (!this::itemLayoutLinearBinding.isInitialized)
              itemLayoutLinearBinding = DataBindingUtil.bind(itemView)!!
            itemLayoutLinearBinding.itemViewModels = itemViewModel
          }
          else -> {
            if (!this::itemLayoutBinding.isInitialized)
              itemLayoutBinding = DataBindingUtil.bind(itemView)!!

            itemLayoutBinding.itemViewModels = itemViewModel
          }
        }
      }
    }
  }

}