package grand.app.moon.presentation.social

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemNotificationBinding
import grand.app.moon.databinding.ItemSocialBinding
import grand.app.moon.domain.settings.models.NotificationData
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.notfication.viewmodel.ItemNotificationViewModel
import grand.app.moon.presentation.social.viewModels.ItemSocialViewModel

class SocialAdapter: RecyclerView.Adapter<SocialAdapter.ViewHolder>() {
  var clickEvent: SingleLiveEvent<SettingsData> = SingleLiveEvent()
  lateinit var useCase: SettingsUseCase
  private val differCallback = object : DiffUtil.ItemCallback<SettingsData>() {
    override fun areItemsTheSame(oldItem: SettingsData, newItem: SettingsData): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: SettingsData, newItem: SettingsData): Boolean {
      return oldItem == newItem
    }

  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_social, parent, false)
    return ViewHolder(view)
  }


  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
//    Log.d(TAG, "onBindViewHolder: ${data.id}")
//    Log.d(TAG, "onBindViewHolder: ${data.image}")
    val itemViewModel = ItemSocialViewModel(data)

    holder.setViewModel(itemViewModel)
  }

  private val TAG = "NotificationAdapter"
  fun insertData(insertList: List<SettingsData>) {
    val array = ArrayList<SettingsData>(differ.currentList)
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
    lateinit var itemLayoutBinding: ItemSocialBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemSocialViewModel) {
      itemLayoutBinding.notifyItemViewModels = itemViewModel
    }
  }


}