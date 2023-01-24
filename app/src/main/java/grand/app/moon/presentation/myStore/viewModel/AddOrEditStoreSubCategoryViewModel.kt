package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showError
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.presentation.myStore.AddOrEditStoreSubCategoryFragment
import grand.app.moon.presentation.myStore.AddOrEditStoreSubCategoryFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AddOrEditStoreSubCategoryViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: AddOrEditStoreSubCategoryFragmentArgs,
) : AndroidViewModel(application) {

	val allCategories = MutableLiveData<List<IdAndName>?>()

	val subCategoryName = MutableLiveData(args.name)

	private val selectedCategory = allCategories.mapToMutableLiveData { list ->
		list.orEmpty().firstOrNull { args.parentId?.toIntOrNull() == it.id }
	}
	val categoryName = selectedCategory.map { it?.name.orEmpty() }

	val buttonText = if (args.id == null) {
		app.getString(R.string.addition_12)
	}else {
		app.getString(R.string.update)
	}

	fun selectCategory(view: View) {
		val fragment = view.findFragmentOrNull<AddOrEditStoreSubCategoryFragment>() ?: return

		if (allCategories.value.isNullOrEmpty()) {
			return fragment.showMessage(fragment.getString(R.string.create_main_category_firstly))
		}

		view.showPopup(
			allCategories.value.orEmpty().map { it.name.orEmpty() },
			listener = { menuItem ->
				selectedCategory.value = allCategories.value?.firstOrNull { it.name == menuItem.title?.toString() }
			}
		)
	}

	fun addOrEdit(view: View) {
		val fragment = view.findFragmentOrNull<AddOrEditStoreSubCategoryFragment>() ?: return

		if (selectedCategory.value == null) {
			return fragment.showError(fragment.getString(R.string.pick_main_category_firstly))
		}

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				val id = args.id
				val name = subCategoryName.value.orEmpty()
				val selectedCategoryId = selectedCategory.value?.id.orZero()
				if (id == null) {
					repoShop.createSubCategory(name, selectedCategoryId)
				}else {
					repoShop.updateSubCategory(id.toIntOrNull().orZero(), name, selectedCategoryId)
				}
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navUpThenSetResultInBackStackEntrySavedStateHandleViaGson(
				true // Done successfully whether creation or edition
			)
		}
	}

}
