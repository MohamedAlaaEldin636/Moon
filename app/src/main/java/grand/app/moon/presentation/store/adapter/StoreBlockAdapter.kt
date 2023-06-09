package grand.app.moon.presentation.store.adapter

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
import grand.app.moon.databinding.ItemStoreBlockedBinding
import grand.app.moon.databinding.ItemStoreFollowedBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.extensions.orFalse
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel

class StoreBlockAdapter : RecyclerView.Adapter<StoreBlockAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  val unBlocks = ArrayList<Int>()
  var position = -1
  private val differCallback = object : DiffUtil.ItemCallback<Store>() {
    override fun areItemsTheSame(oldItem: Store, newItem: Store): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Store, newItem: Store): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_store_blocked, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemStoreViewModel(data, 3, 100, null, position)
    holder.setViewModel(itemViewModel)
  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<Store>) {
    val array = ArrayList<Store>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: " + size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem() {
    Log.d(TAG, "removeItem: $position")
    Log.d(TAG, "removeItem_size_total: ${differ.currentList.size}")
    val list = ArrayList(differ.currentList)
    list.removeAt(position)
    Log.d(TAG, "removeItem_tmp: ${list.size}")
//    notifyItemRemoved(position)
    differ.submitList(null)
    differ.submitList(list)
//    notifyDataSetChanged()
    Log.d(TAG, "removeItem_size_after: ${differ.currentList.size}")
//    Log.d(TAG, "removeItem: $position")
//    differ.currentList.removeAt(position)
//    notifyItemRemoved(position)
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

  fun changeBlock(id: Int) {
    Log.d(TAG, "changeBlock: $id")
    Log.d(TAG, "changeBlock: ${unBlocks}")
    if (!unBlocks.contains(id)) unBlocks.add(id)
    else unBlocks.remove(id)
    Log.d(TAG, "changeBlock: ${unBlocks.size}")
    Log.d(TAG, "changeBlock: ${unBlocks}")
    differ.currentList[position].isBlocked = !differ.currentList[position].isBlocked.orFalse()
    notifyItemChanged(position)
  }

  fun setAllBlocks(list: ArrayList<Store>){
    list.forEach {
      unBlocks.add(it.id.orZero())
    }
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemStoreBlockedBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemStoreViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }
}