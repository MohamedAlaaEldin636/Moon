package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemSocialMedia2Binding
import grand.app.moon.extensions.RVItemCommonListUsage
import grand.app.moon.extensions.launchBrowser
import grand.app.moon.extensions.setupWithGlide
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.presentation.home.models.ResponseSocialMedia
import javax.inject.Inject

@HiltViewModel
class SocialMedia2ViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
) : AndroidViewModel(application) {

	val adapter = RVItemCommonListUsage<ItemSocialMedia2Binding, ResponseSocialMedia>(
		R.layout.item_social_media_2,
		onItemClick = { _, binding ->
			val link = binding.textView.text.toStringOrEmpty()

			binding.root.context?.launchBrowser(link)
		}
	) { binding, _, item ->
		binding.imageView.setupWithGlide {
			load(item.image)
		}

		binding.textView.text = item.content
	}

}
