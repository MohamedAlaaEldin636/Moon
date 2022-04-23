package grand.app.moon.presentation.filter.dialog.multiCheck

import android.util.Log
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.filter.entitiy.FilterProperty
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.store.adapter.ReportAdapter
import javax.inject.Inject


@HiltViewModel
class FilterMultiSelectDialogViewModel @Inject constructor(
) : BaseViewModel() {

  var property: FilterProperty = FilterProperty()

  val progress = ObservableBoolean(false)
  val title = ObservableField("")
  val adapter = FilterMultiAdapter()
  val list = arrayListOf<AppTutorial>()

  init {
  }

  private  val TAG = "FilterChildrenDialogVie"
  fun setData(property : FilterProperty) {
    this.property = property
    property.children.forEach { children ->
      list.add(AppTutorial(children.id,content = children.name))
    }
    if(property.selectedList.isNotEmpty()){
      adapter.selected.clear()
      adapter.selected.addAll(property.selectedList)
    }

    adapter.differ.submitList(list)
  }
}