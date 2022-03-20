package grand.app.moon.presentation.home.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.domain.home.models.Instructor
import com.structure.base_mvvm.R
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.databinding.ItemTeacherBinding
import grand.app.moon.presentation.home.viewModels.ItemTeacherViewModel

class TeacherAdapter : RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {
  var clickEvent: SingleLiveEvent<Instructor> = SingleLiveEvent()

  private val differCallback = object : DiffUtil.ItemCallback<Instructor>() {
    override fun areItemsTheSame(oldItem: Instructor, newItem: Instructor): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Instructor, newItem: Instructor): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teacher, parent, false)
    return ViewHolder(view)
  }

  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemTeacherViewModel(data)
    itemViewModel.clickEvent.observeForever {
      clickEvent.value = data
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

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    private lateinit var itemLayoutBinding: ItemTeacherBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemTeacherViewModel) {
      itemLayoutBinding.itemViewModels = itemViewModel
    }
  }
}