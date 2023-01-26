package grand.app.moon.presentation.more

import android.app.Application
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreFullDataBinding
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.myStore.model.ItemStoreInfo
import javax.inject.Inject

@HiltViewModel
class MyAccount2ViewModel @Inject constructor(
	application: Application,
	userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
	val repoPackages: RepositoryPackages,
	val accountRepository: AccountRepository,
) : AndroidViewModel(application) {

	private val countryIso = accountRepository.getKeyFromLocal(Constants.COUNTRY_ISO)
	private val lang = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

	val user = userLocalUseCase()

	val adapter = RVItemCommonListUsage<ItemStoreFullDataBinding, ItemStoreInfo>(
		R.layout.item_store_full_data,
		listOf(
			ItemStoreInfo.complete(R.drawable.ic_profile, R.string.my_personal_data),
			ItemStoreInfo.complete(R.drawable.ic_view, R.string.last_ads_seen),
		),
		onItemClick = { _, binding ->
			val navController = binding.root.findNavController()

			when (binding.constraintLayout.tag as? Int) {
				R.drawable.ic_profile -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"" // todo ...
					)
					TODO()
				}
				R.drawable.ic_store_data_4 -> {
					if (user.isStore.orFalse()) {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest_store.full.data"
						)
					}else {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.become.shop.packages"
						)
					}
				}
				R.drawable.ic_about_moon_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.webFragment",
						paths = arrayOf(
							binding.textTextView.text.toStringOrEmpty(),
							"https://${countryIso}.souqmoon.com/${lang}/mobile/about"
						)
					)
				}
				R.drawable.ic_chat_with_app_managers -> {
					General.TODO("will be programmed after comet chat starts to work properly so later isa.")
				}
				R.drawable.ic_contact_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.contactUsFragment"
					)
				}
				R.drawable.ic_complains_and_suggestions -> {
					General.TODO("will be programmed in the next sprint isa.")
				}
				R.drawable.ic_terms_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.webFragment",
						paths = arrayOf(
							binding.textTextView.text.toStringOrEmpty(),
							"https://${countryIso}.souqmoon.com/${lang}/mobile/terms"
						)
					)
				}
				R.drawable.ic_help_settings -> {
					TODO()
				}
			}
		}
	) { binding, _, item ->
		binding.constraintLayout.tag = item.imageDrawableRes

		binding.imageImageView.setImageResource(item.imageDrawableRes)

		binding.notCompleteImageView.isVisible = item.notComplete

		binding.textTextView.text = binding.root.context.getString(item.nameStringRes)
	}

}
