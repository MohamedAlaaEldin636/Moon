package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreCategoryBinding
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.domain.shop.ResponseStoreSubCategory
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.StoreCategoriesFragment
import grand.app.moon.presentation.myStore.StoreSubCategoriesFragment
import javax.inject.Inject

@HiltViewModel
class StoreSubCategoriesViewModel @Inject constructor(
	application: Application,
	private val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	val subCategories = repoShop.getMySubCategories()

	val adapter = RVPagingItemCommonListUsage<ItemStoreCategoryBinding, ResponseStoreSubCategory>(
		R.layout.item_store_category,
		areItemsTheSameComparison = { oldItem, newItem ->
			oldItem.id == newItem.id
		},
		additionalListenersSetups = { adapter, binding ->
			binding.delImageView.setOnClickListener { view ->
				val context = view.context ?: return@setOnClickListener

				val fragment = view.findFragmentOrNull<StoreSubCategoriesFragment>() ?: return@setOnClickListener

				val id = binding.textView.tag as? Int ?: return@setOnClickListener

				fragment.showCustomYesNoWarningDialog(
					context.getString(R.string.confirm_deletion),
					context.getString(R.string.del_sub_category_desc)
				) { dialog ->
					fragment.handleRetryAbleActionCancellableNullable(
						action = {
							repoShop.deleteSubCategory(id)
						}
					) {
						dialog.dismiss()

						fragment.showMessage(context.getString(R.string.done_successfully))

						adapter.refresh()
					}
				}
			}

			binding.editImageView.setOnClickListener { view ->
				val id = binding.textView.tag as? Int ?: return@setOnClickListener

				val parentId = binding.editImageView.tag as? Int ?: return@setOnClickListener

				val name = binding.textView.text.toStringOrEmpty()

				val fragment = view.findFragmentOrNull<StoreSubCategoriesFragment>() ?: return@setOnClickListener

				fragment.findNavController().navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.add.or.edit.store.sub.category.edit",
					paths = arrayOf(id.toString(), name, parentId.toString())
				)
			}
		}
	) { binding, _, item ->
		binding.textView.tag = item.id
		binding.editImageView.tag = item.parentId

		binding.textView.text = item.name
	}

	fun addNewCategory(view: View) {
		val fragment = view.findFragmentOrNull<StoreSubCategoriesFragment>() ?: return

		fragment.findNavController().navigateDeepLinkWithOptions(
			"fragment-dest-2",
			"grand.app.moon.dest.add.or.edit.store.sub.category.create",
		)
	}

}
