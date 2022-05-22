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

class ExploreAdapter: RecyclerView.Adapter<ExploreAdapter.ViewHolder>() {
  val tmpList= ArrayList<Explore>()

  var swapPosition: Int = -1
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
    Log.d(TAG, "onBindViewHolder: "+data.file)
    holder.itemLayoutBinding.itemExplore.setOnClickListener {
      Log.d(TAG, "onBindViewHolder: $position")
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

  var changed = false
  fun updateExplores(result: ExploreListPaginateData) {
//    val list = ArrayList(differ.currentList)
    if(swapPosition != -1) {
//      Log.d(TAG, "updateExplores: swap ${swapPosition} id Before ${result.list[0].id} with likes ${result.list[0].likes} , ${differ.currentList[0].id}")
//      Log.d(TAG, "updateExplores: list ${result.list.size}")
//      Log.d(TAG, "updateExplores: list before ${differ.currentList.size}")
//      Log.d(TAG, "updateExplores: prev id ${result.list[0].id}")
//      Collections.swap(result.list, 0, swapPosition)
//      Log.d(TAG, "updateExplores: After id ${result.list[0].id}")
//      Log.d(TAG, "updateExplores: id After ${result.list[0].id} , ${differ.currentList[0].id}")



//      result.list.forEachIndexed { index, explore ->
//        Log.d(TAG, "updateExplores: ${differ.currentList[index].id} , ${explore.id}")
//      }
    }

//    tmpList.forEachIndexed{ index,explore ->
//      if(differ.currentList[index].id != explore.id || differ.currentList[index].likes != explore.likes || differ.currentList[index].comments != explore.comments ){
//        Log.d(TAG, "updateExplores: DONE")
////        differ.currentList[index].id = explore.id
////        differ.currentList[index].file = explore.file
//        notifyItemChanged(index)
//      }
//    }
    notifyDataSetChanged()
//    result.list.forEachIndexed{ index, explore ->
//      changed = false
//      Log.d(TAG, "updateExplores: Starigng => ${differ.currentList[index].id} => ${differ.currentList[index].likes} , ${explore.id} ${explore.likes} =>Tmp ${tmpList[index].id} ${tmpList[index].likes}")
//      if(differ.currentList[index].comments != result.list[index].comments && index < result.list.size){
//        differ.currentList[index].comments = result.list[index].comments
////        Log.d(TAG, "updateExplores: WORKED")
//        changed = true
//      }
//      if(differ.currentList[index].likes != explore.likes && index < result.list.size){
//        Log.d(TAG, "updateExplores: WORKED Like")
//        differ.currentList[index].likes = explore.likes
//        changed = true
//      }
//      if(changed) {
//        Log.d(TAG, "updateExplores: notified ${index} with likes ${differ.currentList[index].likes}")
//        notifyItemChanged(index)
//      }
//    }
//    Log.d(TAG, "updateExplores: ${result.list.size}")
//    var changed: Boolean
//    differ.currentList.forEachIndexed{ index, explore ->
//      changed = false
//      Log.d(TAG, "updateExplores: ${explore.likes}")
//      Log.d(TAG, "updateExplores: ${result.list[index].likes}")
//      Log.d(TAG, "=================================================")
//
//      if(explore.likes != result.list[index].likes){
//        changed = true
//        explore.likes = result.list[index].likes
//      }
//      if(explore.comments != result.list[index].comments){
//        changed = true
//        explore.comments = result.list[index].comments
//      }
//      if(changed) notifyItemChanged(index)
//    }
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