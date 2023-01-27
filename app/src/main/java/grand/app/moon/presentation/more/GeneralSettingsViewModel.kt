package grand.app.moon.presentation.more

import android.app.Application
import android.content.Intent
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import grand.app.moon.core.extenstions.rateApp
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreFullDataBinding
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.myStore.model.ItemStoreInfo
import javax.inject.Inject

@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(
	application: Application,
	userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
	val repoPackages: RepositoryPackages,
	val accountRepository: AccountRepository,
	val logInUseCase: LogInUseCase,
) : AndroidViewModel(application) {

	private val countryIso = accountRepository.getKeyFromLocal(Constants.COUNTRY_ISO)
	private val lang = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

	val user = userLocalUseCase()

	val adapter = RVItemCommonListUsage<ItemStoreFullDataBinding, ItemStoreInfo>(
		R.layout.item_store_full_data,
		listOf(
			ItemStoreInfo.complete(R.drawable.ic_help_settings, R.string.help),
			ItemStoreInfo.complete(R.drawable.ic_language_settings, R.string.change_language),
			ItemStoreInfo.complete(R.drawable.ic_country_settings, R.string.change_country),
			ItemStoreInfo.complete(R.drawable.ic_social_settings, R.string.social_media_2),
			ItemStoreInfo.complete(R.drawable.ic_night_mode_999999999999, R.string.night_mode),
			ItemStoreInfo.complete(R.drawable.ic_share_settings, R.string.share_app),
			ItemStoreInfo.complete(R.drawable.ic_rate_app, R.string.rate_app),
		),
		onItemClick = { _, binding ->
			val fragment = binding.root.findFragmentOrNull<MyAccount2Fragment>() ?: return@RVItemCommonListUsage

			val context = fragment.context ?: return@RVItemCommonListUsage

			val navController = binding.root.findNavController()

			when (binding.constraintLayout.tag as? Int) {
				R.drawable.ic_help_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.webFragment",
						paths = arrayOf(
							binding.textTextView.text.toStringOrEmpty(),
							"https://${countryIso}.souqmoon.com/${lang}/mobile/help"
						)
					)
				}
				R.drawable.ic_language_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.languageFragment2",
						paths = arrayOf(Constants.MORE)
					)
				}
				R.drawable.ic_country_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.countriesFragment3",
						paths = arrayOf(Constants.MORE)
					)
				}
				R.drawable.ic_social_settings -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.socialFragment",
					)
				}
				R.drawable.ic_night_mode_999999999999 -> {
					General.TODO("Will be programmed in next sprint isa.")
				}
				R.drawable.ic_share_settings -> {
					val intent = Intent(Intent.ACTION_SEND)
					intent.type = "text/plain"

					val messageDisplay = "https://play.google.com/store/apps/details?id=${context.applicationContext.packageName}"
					intent.putExtra(Intent.EXTRA_SUBJECT, context.getString(R.string.app_name))
					intent.putExtra(Intent.EXTRA_TEXT, messageDisplay)
					context.startActivity(
						Intent.createChooser(
							intent,
							context.getString(R.string.share)
						)
					)
				}
				R.drawable.ic_rate_app -> {
					fragment.rateApp()
				}
			}
		}
	) { binding, _, item ->
		binding.constraintLayout.tag = item.imageDrawableRes

		binding.imageImageView.setImageResource(item.imageDrawableRes)

		binding.notCompleteImageView.isVisible = item.notComplete

		binding.textTextView.text = binding.root.context.getString(item.nameStringRes)

		binding.arrowImageView.isVisible = item.showArrow
	}

}
