package app.grand.tafwak.presentation.auth.countries.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import app.grand.tafwak.domain.countries.entity.Country
import com.structure.base_mvvm.R
import app.grand.tafwak.presentation.auth.countries.viewModels.ItemCountryViewModel
import app.grand.tafwak.presentation.base.utils.SingleLiveEvent
import com.structure.base_mvvm.databinding.ItemCountryBinding

class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.ViewHolder>() {
  var lastSelected = -1
  private var lastPosition = -1
  var changeEvent: SingleLiveEvent<Country> = SingleLiveEvent()
  private val differCallback = object : DiffUtil.ItemCallback<Country>() {
    override fun areItemsTheSame(oldItem: Country, newItem: Country): Boolean {
      return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Country, newItem: Country): Boolean {
      return oldItem == newItem
    }
  }
  val differ = AsyncListDiffer(this, differCallback)
  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
    return ViewHolder(view)
  }

  @SuppressLint("RecyclerView")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCountryViewModel(data)
    itemViewModel.clickEvent.observeForever {
      notifyItemChanged(lastPosition)
      lastPosition = position
      lastSelected = data.id
      changeEvent.value = data // for listen on view
      notifyItemChanged(position)
    }
    holder.itemLayoutBinding.radio.isChecked = lastPosition == position
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
    lateinit var itemLayoutBinding: ItemCountryBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemCountryViewModel) {
      itemLayoutBinding.countryItemViewModels = itemViewModel
    }
  }
}