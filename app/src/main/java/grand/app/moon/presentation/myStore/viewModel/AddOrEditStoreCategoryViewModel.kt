package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.AddOrEditStoreCategoryFragment
import grand.app.moon.presentation.myStore.AddOrEditStoreCategoryFragmentArgs
import javax.inject.Inject

@HiltViewModel
class AddOrEditStoreCategoryViewModel @Inject constructor(
	application: Application,
	private val repoShop: RepoShop,
	val args: AddOrEditStoreCategoryFragmentArgs,
) : AndroidViewModel(application) {

	val categoryName = MutableLiveData(args.name.orEmpty())

	val buttonText = if (args.id == null) {
		app.getString(R.string.addition_12)
	}else {
		app.getString(R.string.update)
	}

	fun addOrEdit(view: View) {
		val fragment = view.findFragmentOrNull<AddOrEditStoreCategoryFragment>() ?: return

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				val id = args.id
				val name = categoryName.value.orEmpty()
				if (id == null) {
					repoShop.createCategory(name)
				}else {
					repoShop.updateCategory(id.toIntOrNull().orZero(), name)
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
