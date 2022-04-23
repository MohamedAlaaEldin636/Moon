package grand.app.moon.presentation.filter.dialog

import android.util.Log
import androidx.databinding.Bindable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.adapter.ReportAdapter
import javax.inject.Inject


@HiltViewModel
class FilterChildrenDialogViewModel @Inject constructor(
) : BaseViewModel() {

  var property: FilterProperty = FilterProperty()

  val progress = ObservableBoolean(false)
  val title = ObservableField("")
  val adapter = ReportAdapter()
  val list = arrayListOf<AppTutorial>()

  init {
  }

  private  val TAG = "FilterChildrenDialogVie"
  fun setData(property : FilterProperty) {
    this.property = property
    property.children.forEachIndexed { index, children ->
      list.add(AppTutorial(children.id,content = children.name))
      if(property.selectedList.isNotEmpty() && children.id == property.selectedList[0]){
        adapter.lastPosition = index
        adapter.lastSelected = property.selectedList[0]
      }
    }
    Log.d(TAG, "setData: ${list.size}")
    adapter.differ.submitList(list)
  }
}