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
import grand.app.moon.R
import grand.app.moon.appMoonHelper.ListHelper
import grand.app.moon.databinding.ItemExploreListBinding
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.explore.entity.Explore
import grand.app.moon.domain.explore.entity.ExploreAction
import grand.app.moon.domain.explore.use_case.ExploreUseCase
import grand.app.moon.extensions.orZero
import grand.app.moon.presentation.explore.viewmodel.ItemExploreViewModel

class ExploreListAdapter : RecyclerView.Adapter<ExploreListAdapter.ViewHolder>() {
  var clickEvent: MutableLiveData<String> = MutableLiveData()
  var position = -1
  var user = User()
  var fromStore = false
  lateinit var exploreUseCase: ExploreUseCase
  val exploreAction = ExploreAction()

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
    val view =
      LayoutInflater.from(parent.context).inflate(R.layout.item_explore_list, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemExploreViewModel(data, position,user,fromStore)
//    holder.itemLayoutBinding.appCompatEditText.setOnEditorActionListener(TextView.OnEditorActionListener { textView: TextView, i: Int, keyEvent: KeyEvent? ->
//      if (i == EditorInfo.IME_ACTION_SEND) {
//        exploreAction.exploreId = data.id
//        exploreAction.comment = holder.itemLayoutBinding.appCompatEditText.text.toString()
//        exploreUseCase.setExploreAction(exploreAction)
//        differ.currentList[position].comments++
//        notifyItemChanged(position)
//        return@OnEditorActionListener true
//      }
//      false
//    })

//    holder.itemLayoutBinding.lifecycleOwner?.let {
//      itemViewModel.submitEvent.observe(holder.itemLayoutBinding.root, {
//        this.position = position
//        clickEvent.value = it
//      })
//    }
    holder.setViewModel(itemViewModel)

  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<Explore>) {
    val array = ArrayList<Explore>(differ.currentList)
    val size = array.size
    array.addAll(insertList)
    Log.d(TAG, "insertData: " + size)
//    notifyItemRangeInserted(size,array.size)
    differ.submitList(array)
    notifyDataSetChanged()
  }

  fun removeItem(i: Int) {
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

  fun updateTotalComments(position: Int, total: Int) {
    Log.d(TAG, "updateTotalComments: ")
    if(position != -1 && position < differ.currentList.size) {
      Log.d(TAG, "updateTotalComments: $position")
      differ.currentList[position].comments = total
      notifyItemChanged(position)
    }
  }

  fun changeFollowing(id: Int, following: Boolean) {
    differ.currentList.forEachIndexed{ index , explore ->
      if(explore.store.id == id && explore.store.isFollowing != following){
        explore.store.isFollowing = following
        notifyItemChanged(index)
      }
    }
  }

  fun checkFollowingStore() {
    differ.currentList.forEachIndexed{ index , explore ->
      if (explore.store.isFollowing != ListHelper.getFollowStore(explore.store.id.orZero())) {
        explore.store.isFollowing = ListHelper.getFollowStore(explore.store.id.orZero())
        notifyItemChanged(index)
      }
    }
  }

  fun checkBlockStore() {
    val array = ArrayList<Explore>()
    differ.currentList.forEachIndexed{ index, explore ->
      if(!ListHelper.checkBlockStore(differ.currentList[index].store.id.orZero())){
        array.add(explore)
//        notifyItemRemoved(index)
      }
    }
    if(array.size != differ.currentList.size) {
      differ.submitList(null)
      differ.submitList(array)
//      notifyItemRangeChanged(0,differ.currentList.size)
    }
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemExploreListBinding

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