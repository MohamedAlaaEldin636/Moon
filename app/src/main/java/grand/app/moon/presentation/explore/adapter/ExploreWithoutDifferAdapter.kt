package grand.app.moon.presentation.explore.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.explore.entity.ExploreListPaginateData
import grand.app.moon.presentation.explore.ExploreFragmentDirections
import grand.app.moon.presentation.explore.viewmodel.ExploreViewModel
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel
import java.util.*
import kotlin.collections.ArrayList

class ExploreWithoutDifferAdapter: RecyclerView.Adapter<ExploreWithoutDifferAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<Int> = MutableLiveData()
  val user = grand.app.moon.domain.auth.entity.model.User()
  val list = ArrayList<Explore>()



  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_explore, parent, false)
    Log.d(TAG, "onCreateViewHolder: WELCOMe")
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = list[position]
    val itemViewModel = ItemExploreViewModel(data,position,user)
    Log.d(TAG, "onBindViewHolder: "+data.file)
    holder.itemLayoutBinding.itemExplore.setOnClickListener {
      Log.d(TAG, "onBindViewHolder: ")
      clickEvent.value = position

//      val list = ArrayList(differ.currentList)
//      viewModel.lastData.list.clear()
//      viewModel.lastData.list.addAll(list)
//      Collections.swap(viewModel.lastData.list, 0, it);

//      var lastData = ExploreListPaginateData()
//      it!!.findNavController().navigate(ExploreFragmentDirections.actionExploreFragmentToNavExplore(lastData,1))


    }
    holder.setViewModel(itemViewModel)
  }


  private val TAG = "ExploreAdapter"
  fun insertData(insertList: List<Explore>) {
    val array = ArrayList<Explore>(list.size)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: "+size)
//    notifyItemRangeInserted(size,array.size)
    list.addAll(array)
    notifyDataSetChanged()
  }



  override fun getItemCount(): Int {
    return list.size
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