package grand.app.moon.presentation.explore.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.models.User
import grand.app.moon.R
import grand.app.moon.databinding.ItemExploreBinding
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel

class ExploreAdapter: RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  val user = grand.app.moon.domain.auth.entity.model.User()
  private val differCallback = object : DiffUtil.ItemCallback<Explore>() {
    override fun areItemsTheSame(oldItem: Explore, newItem: Explore): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Explore, newItem: Explore): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_explore, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemExploreViewModel(data,position,user)
//    Log.d(TAG, "onBindViewHolder: "+data.file)
    holder.itemLayoutBinding.itemExplore.setOnClickListener {
      Log.d(TAG, "onBindViewHolder: ")
      clickEvent.value = position
    }
    holder.setViewModel(itemViewModel)
  }

  private val TAG = "ExploreAdapter"
  fun insertData(insertList: List<Explore>) {
    val array = ArrayList<Explore>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem(i: Int){
    val array = ArrayList(differ.currentList)
    array.remove(array[i])
    differ.submitList(array)
    notifyItemRemoved(i)
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
    lateinit var itemLayoutBinding: ItemExploreBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemExploreViewModel) {
      itemLayoutBinding.viewModel = itemViewModel
    }
  }


}