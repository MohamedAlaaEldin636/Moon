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
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.ItemStoreFollowedBinding
import grand.app.moon.domain.home.models.Store
import grand.app.moon.presentation.store.viewModels.ItemStoreViewModel

class StoreFollowingAdapter : RecyclerView.Adapter<StoreFollowingAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  var position  = 0
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
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_store_followed, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemStoreViewModel(data, 100,3,null,position)
    holder.itemLayoutBinding.btnFollowStore.setOnClickListener {
      this.position = position
      clickEvent.value = position
    }
    holder.setViewModel(itemViewModel)
  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<Store>) {
    val array = ArrayList<Store>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem(){
    val list = ArrayList(differ.currentList)
    list[position].isFollowing = !list[position].isFollowing
    notifyItemChanged(position)
    ListHelper.addFollowStore(list[position].id,list[position].isFollowing)

//    list.removeAt(position)
//    differ.submitList(list)
//    notifyItemRemoved(position)
  }

  fun changeFollowingText(){
    val list = ArrayList(differ.currentList)
    list[position].isFollowing = !list[position].isFollowing
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

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemStoreFollowedBinding

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