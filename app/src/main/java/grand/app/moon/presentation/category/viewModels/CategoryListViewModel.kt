package grand.app.moon.presentation.category.viewModels

import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.BR
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.categories.entity.CategoryItem
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.category.adapter.CategoriesAdapter
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryListViewModel @Inject constructor(
  private val useCase: SettingsUseCase,
  val accountRepository: AccountRepository,
  ) : BaseViewModel() {

  @Bindable
  val categoriesAdapter = CategoriesAdapter()
  val all = arrayListOf<CategoryItem>()
  val listShow = arrayListOf<CategoryItem>()

  init {
    viewModelScope.launch {
      accountRepository.getCategories().collect {
        all.addAll(it.data)
        listShow.addAll(it.data)
        categoriesAdapter.differ.submitList(it.data)
        notifyPropertyChanged(BR.categoriesAdapter)
      }
    }
  }

  override fun onCleared() {
    job.cancel()
    super.onCleared()
  }

  fun search(text: String) {
    listShow.clear()
    all.forEach {
      if(it.name.contains(text)) listShow.add(it)
    }
    categoriesAdapter.differ.submitList(listShow)
    notifyPropertyChanged(BR.categoriesAdapter)
  }
}