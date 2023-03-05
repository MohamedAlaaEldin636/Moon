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
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.core.extenstions.launchCometChat
import grand.app.moon.core.extenstions.redirectIfNotLoggedIn
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreFullDataBinding
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.myStore.model.ItemStoreInfo
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
	application: Application,
	val userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
	val repoPackages: RepositoryPackages,
	val accountRepository: AccountRepository,
) : AndroidViewModel(application) {

	private val countryIso = accountRepository.getKeyFromLocal(Constants.COUNTRY_ISO)
	private val lang = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

	val user = MutableLiveData(userLocalUseCase())

	val restDaysInPackage = MutableLiveData<Int?>()

	val storeNoticeText = restDaysInPackage.mapToMutableLiveData {
		val res = when {
			app.isLogin().not() || user.value?.isStore.orFalse().not() -> R.string.upgrade_to_shop_now
			else -> if (it.orZero() > 0) {
				R.string.check_ad_premium
			}else {
				R.string.renew_and_subscribe_in_another_package
			}
		}

		app.getString(res)
	}

	val showBecomeStoreIcon = user.map {
		it?.isStore.orFalse().not()
	}

	val adapter = RVItemCommonListUsage<ItemStoreFullDataBinding, ItemStoreInfo>(
		R.layout.item_store_full_data,
		listOf(
			ItemStoreInfo.complete(R.drawable.ic_profile, if (app.isLogin()) R.string.my_account else R.string.log_in),
			ItemStoreInfo.complete(R.drawable.ic_store_data_4, R.string.store_data),
			ItemStoreInfo.complete(R.drawable.ic_about_moon_settings, R.string.about_moon),
			ItemStoreInfo.complete(R.drawable.ic_chat_with_app_managers, R.string.chat_with_app_managers),
			ItemStoreInfo.complete(R.drawable.ic_contact_settings, R.string.contact_us),
			ItemStoreInfo.complete(R.drawable.ic_complains_and_suggestions, R.string.complains_and_suggestions),
			ItemStoreInfo.complete(R.drawable.ic_terms_settings, R.string.terms),
			ItemStoreInfo.complete(R.drawable.ic_help_settings, R.string.general_settings),
		),
		onItemClick = { _, binding ->
			val fragment = binding.root.findFragmentOrNull<MoreFragment>() ?: return@RVItemCommonListUsage

			val navController = binding.root.findNavController()

			when (binding.constraintLayout.tag as? Int) {
				R.drawable.ic_profile -> {
					if (binding.root.context.redirectIfNotLoggedIn().not()) {
						navController.navigateDeepLinkWithOptions(
							"fragment-dest",
							"grand.app.moon.dest.my.account.two"
						)
					}
				}
				R.drawable.ic_store_data_4 -> {
					if (binding.root.context.redirectIfNotLoggedIn().not()) {
						if (user.value?.isStore.orFalse()) {
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
				}
				R.drawable.ic_about_moon_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.webFragment",
						paths = arrayOf(
							binding.textTextView.text.toStringOrEmpty(),
							//BuildConfig.API_BASE_URL // net // com // sooqmoon.net // souqmoon.com
							"https://${countryIso}.souqmoon.com/${lang}/mobile/about".also {
								MyLogger.e("hiiiiiiiiiiiii $it")
							}
						)
					)
				}
				R.drawable.ic_chat_with_app_managers -> {
					fragment.handleRetryAbleActionCancellable(
						action = {
							repoShop.getChatAgent()
						}
					) {
						fragment.context?.launchCometChat {
							uid = it.uid
							name = it.name
							avatar = it.avatar
							status = it.status
							role = it.role
						}
					}
				}
				R.drawable.ic_contact_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.contact.us.two"
					)
				}
				R.drawable.ic_complains_and_suggestions -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.complains.and.suggestions",
					)
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
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.generalSettingsFragment",
					)
				}
			}
		}
	) { binding, _, item ->
		binding.constraintLayout.tag = item.imageDrawableRes

		binding.imageImageView.setImageResource(item.imageDrawableRes)

		binding.notCompleteImageView.isVisible = item.notComplete

		binding.textTextView.text = binding.root.context.getString(item.nameStringRes)
	}

	fun onBecomeShopOrAlreadySubscribedClick(view: View) {
		val fragment = view.findFragmentOrNull<MoreFragment>() ?: return

		val context = fragment.context ?: return

		if (context.redirectIfNotLoggedIn()) {
			return
		}

		val navController = fragment.findNavController()

		when {
			user.value?.isStore.orFalse().not() -> {
				navController.navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.become.shop.packages"
				)
			}
			else -> if (restDaysInPackage.value.orZero() > 0) {
				navController.navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.my.become.shop.package"
				)
			}else {
				navController.navigateDeepLinkWithOptions(
					"fragment-dest",
					"grand.app.moon.dest.become.shop.packages"
				)
			}
		}
	}

}
