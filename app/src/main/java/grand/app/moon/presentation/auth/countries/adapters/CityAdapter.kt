package grand.app.moon.presentation.auth.countries.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import grand.app.moon.R
import grand.app.moon.databinding.ItemCityBinding
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import grand.app.moon.presentation.auth.countries.viewModels.ItemCityViewModel

class CityAdapter : RecyclerView.Adapter<CityAdapter.ViewHolder>() {
  var lastSelected = -1
  var lastPosition = -1
  val selected = ArrayList<Int>()
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
    val view = LayoutInflater.from(parent.context).inflate(R.layout.item_city, parent, false)
    return ViewHolder(view)
  }

  private  val TAG = "CountriesAdapter"
  @SuppressLint("RecyclerView")
  override fun onBindViewHolder(holder: ViewHolder, position: Int) {
    val data = differ.currentList[position]
    val itemViewModel = ItemCityViewModel(data,this.selected.contains(data.id))
    holder.itemLayoutBinding.rlCitySelected.setOnClickListener {
      if(this.selected.contains(data.id)){
        this.selected.remove(data.id)
      }else{
        this.selected.add(data.id)
      }
      notifyItemChanged(position)
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

  fun submitList(data: List<Country>) {
    differ.submitList(data)
    notifyDataSetChanged()
  }

  inner class ViewHolder(itemView: View) :
    RecyclerView.ViewHolder(itemView) {
    lateinit var itemLayoutBinding: ItemCityBinding

    init {
      bind()
    }

    fun bind() {
      itemLayoutBinding = DataBindingUtil.bind(itemView)!!
    }

    fun unBind() {
      itemLayoutBinding.unbind()
    }

    fun setViewModel(itemViewModel: ItemCityViewModel) {
      itemLayoutBinding.countryItemViewModels = itemViewModel
    }
  }
}