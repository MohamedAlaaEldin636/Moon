package grand.app.moon.presentation.explore.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.cometchat.pro.models.User
import grand.app.moon.R
import grand.app.moon.databinding.ItemExploreBinding
import grand.app.moon.databinding.ItemExploreGidEqualBinding
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.explore.ExploreFragmentDirections
import grand.app.moon.presentation.explore.viewmodel.ExploreViewModel
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel
import grand.app.moon.presentation.store.views.StoreDetailsFragmentDirections
import java.util.*
import kotlin.collections.ArrayList

class ExploreGridEqualAdapter: RecyclerView.Adapter<ExploreGridEqualAdapter.ViewHolder>() {
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
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_explore_gid_equal, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemExploreViewModel(data,position,user,false)
    holder.itemLayoutBinding.itemExplore.setOnClickListener {
//      val list = ExploreListPaginateData()
//      list.list.addAll(differ.currentList)
//      holder.itemLayoutBinding.itemExplore.findNavController()
//        .navigate(R.id.exploreListFragment, bundleOf(
//          "page" to 1,
//          "data" to list,
//          "from_store" to true
//        ), Constants.NAVIGATION_OPTIONS)

//      holder.itemLayoutBinding.itemExplore.findNavController().navigate(
//        StoreDetailsFragmentDirections.actionStoreDetailsFragmentToExploreListFragment2(
//          list,
//          1,
//          true
//        )
//      )
    }
    holder.setViewModel(itemViewModel)
  }

  fun updateExplores(result: ExploreListPaginateData) {
    if(differ.currentList.size != result.list.size){
      differ.submitList(null)
      differ.submitList(result.list)
    }
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
    lateinit var itemLayoutBinding: ItemExploreGidEqualBinding

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