@file:Suppress("OPT_IN_USAGE")

package grand.app.moon.presentation.categories.viewModel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.domain.categories.entity.ItemCategory
import grand.app.moon.domain.categories.entity.ItemSubCategory
import grand.app.moon.domain.home.use_case.HomeUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.MyLogger
import grand.app.moon.extensions.fromJsonInlinedOrNull
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentDirections
import grand.app.moon.presentation.categories.adapter.RVItemIconTextArrow
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AddAdvSubCategoriesListViewModel @Inject constructor(
	private val args: AddAdvSubCategoriesListFragmentArgs
) : ViewModel() {

	val title = MutableLiveData(args.nameOfMainCategory)

	val adapter = RVItemIconTextArrow { view, id, _, _ ->
		view.findNavController().navigate(
			AddAdvSubCategoriesListFragmentDirections.actionDestAddAdvSubCategoriesListToDestAddAdvFinalPage(
				args.idOfMainCategory,
				id
			)
		)
	}

	init {
		adapter.submitList(
			args.jsonListOfItemSubcategory.fromJsonInlinedOrNull<List<ItemSubCategory>>().orEmpty()
		)
	}

	fun goBack(view: View) {
		view.findNavController().navigateUp()
	}

}
