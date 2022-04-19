package grand.app.moon.presentation.store.adapter

import android.content.Context
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
import grand.app.moon.databinding.ItemStoreBinding
import grand.app.moon.databinding.ItemStoreLinearBinding
import grand.app.moon.domain.home.models.Advertisement
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel

class StoreAdapter : RecyclerView.Adapter<StoreAdapter.ViewHolder>() {
  lateinit var context: Context
  var submitEvent: MutableLiveData<String> = MutableLiveData()
  var percentage = 100
  var position = 0
  var grid = Constants.GRID_2
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
    Log.d(TAG, "onBindViewHolder: $position")
    val data = differ.currentList[position]
    val itemViewModel = ItemStoreViewModel(data, percentage)
    holder.setViewModel(itemViewModel)
    when (grid) {
      Constants.GRID_1 -> {
        holder.itemLayoutLinearBinding.itemStoreContainer.setOnClickListener {
          Log.d(TAG, "onBindViewHolder: HAY HERE")

          holder.itemLayoutLinearBinding.root.findNavController().navigate(
            R.id.nav_store,
            bundleOf(
              "id" to data.id
            ), Constants.NAVIGATION_OPTIONS
          )

        }
        holder.itemLayoutLinearBinding.follow.setOnClickListener {
          Log.d(TAG, "onBindViewHolder: HAY THERE")
          data.isFollowing = data.isFollowing != true
          this.position = position
          submitEvent.value = Constants.FOLLOW
          notifyItemChanged(position)
        }
      }
      else -> {
        holder.itemLayoutBinding.itemStoreContainer.setOnClickListener {
          Log.d(TAG, "onBindViewHolder: HAY HERE")

          holder.itemLayoutBinding.root.findNavController().navigate(
            R.id.nav_store,
            bundleOf(
              "id" to data.id
            ), Constants.NAVIGATION_OPTIONS
          )

        }
        holder.itemLayoutBinding.follow.setOnClickListener {
          Log.d(TAG, "onBindViewHolder: HAY THERE")
          data.isFollowing = data.isFollowing != true
          this.position = position
          submitEvent.value = Constants.FOLLOW
          notifyItemChanged(position)
        }
      }
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

  fun insertData(list: ArrayList<Store>) {
    val array = ArrayList<Store>(differ.currentList)
    val size = array.size
    array.addAll(list)
    differ.submitList(array)
    notifyDataSetChanged()
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
        Log.d(TAG, "setViewModel: YEPR")
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