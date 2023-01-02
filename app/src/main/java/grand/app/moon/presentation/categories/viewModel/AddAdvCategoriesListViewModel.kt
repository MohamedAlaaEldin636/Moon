@file:Suppress("OPT_IN_USAGE")

package grand.app.moon.presentation.categories.viewModel

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.toJsonInlinedOrNull
import grand.app.moon.presentation.categories.AddAdvCategoriesListFragmentDirections
import grand.app.moon.presentation.categories.adapter.RVItemIconTextArrow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddAdvCategoriesListViewModel @Inject constructor(
	private val homeUseCase: HomeUseCase
) : ViewModel() {

	private val _categories =
		MutableStateFlow<Resource<BaseResponse<List<ItemCategory>?>>>(Resource.Default)
	val categories: Flow<Resource<BaseResponse<List<ItemCategory>?>>> = _categories

	val showLoading = categories.mapLatest {
		it is Resource.Loading
	}

	val adapter = RVItemIconTextArrow { view, id, name, subcategories ->
		view.findNavController().navigate(
			AddAdvCategoriesListFragmentDirections.actionDestAddAdvCategoriesListToDestAddAdvSubCategoriesList(
				id,
				name,
				subcategories.toJsonInlinedOrNull().orEmpty()
			)
		)
	}

	init {
		getCategories()
	}

	fun getCategories() {
		homeUseCase.getCategories2().onEach {
			_categories.value = it
		}.launchIn(viewModelScope)
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

}
