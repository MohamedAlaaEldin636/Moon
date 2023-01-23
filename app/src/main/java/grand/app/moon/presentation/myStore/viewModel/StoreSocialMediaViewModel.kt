package grand.app.moon.presentation.myStore.viewModel

import android.app.Application
import android.view.View
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreSocialMediaBinding
import grand.app.moon.domain.shop.ResponseStoreSocialMedia
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.myStore.StoreSocialMediaFragment
import javax.inject.Inject

@HiltViewModel
class StoreSocialMediaViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop
) : AndroidViewModel(application) {

	val adapter = RVItemCommonListUsage<ItemStoreSocialMediaBinding, ResponseStoreSocialMedia>(
		R.layout.item_store_social_media,
		additionalListenersSetups = { adapter, binding ->
			binding.editText.doAfterTextChanged {
				val position = binding.linearLayout.tag as? Int ?: return@doAfterTextChanged

				adapter.list[position].linkUrl = it.toStringOrEmpty()

				// No need to notify item changed as edit text set text by it's own but save in the list
				// so when it gets recycled it still maintains the value isa.
			}
		}
	) { binding, position, item ->
		val context = binding.root.context ?: return@RVItemCommonListUsage

		binding.linearLayout.tag = position

		binding.imageView.setImageResource(item.typeOfLink?.drawableRes.orZero())

		binding.editText.hint = item.typeOfLink?.stringRes?.let { context.getString(it) }.orEmpty()
		binding.editText.setText(item.linkUrl.orEmpty())
	}

	fun save(view: View) {
		val fragment = view.findFragmentOrNull<StoreSocialMediaFragment>() ?: return

		fragment.handleRetryAbleActionCancellableNullable(
			action = {
				repoShop.saveSocialMedia(adapter.list)
			}
		) {
			fragment.showMessage(fragment.getString(R.string.done_successfully))

			fragment.findNavController().navigateUp()
		}
	}

}
