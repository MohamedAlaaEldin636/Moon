package grand.app.moon.presentation.more

import android.app.Application
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.packages.RepositoryPackages
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemStoreFullDataBinding
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.account.use_case.LogOutUseCase
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.domain.auth.use_case.LogInUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.extensions.logout
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.extensions.showMessage
import grand.app.moon.presentation.home.AdsInteractedWithByOtherUsersFragment
import grand.app.moon.presentation.home.FollowedStoresFragment
import grand.app.moon.presentation.home.HomeActivity
import grand.app.moon.presentation.myStore.model.ItemStoreInfo
import javax.inject.Inject

@HiltViewModel
class MyAccount2ViewModel @Inject constructor(
	application: Application,
	userLocalUseCase: UserLocalUseCase,
	val repoShop: RepoShop,
	val repoPackages: RepositoryPackages,
	val accountRepository: AccountRepository,
	val logInUseCase: LogInUseCase,
	val logOutUseCase: LogOutUseCase
) : AndroidViewModel(application) {

	val user = userLocalUseCase()

	val adapter = RVItemCommonListUsage<ItemStoreFullDataBinding, ItemStoreInfo>(
		R.layout.item_store_full_data,
		listOf(
			ItemStoreInfo.complete(R.drawable.ic_profile, R.string.my_personal_data),
			ItemStoreInfo.complete(R.drawable.ic_view, R.string.last_ads_seen),
			ItemStoreInfo.complete(R.drawable.stores_followed, R.string.stores_had_been_followed),
			ItemStoreInfo.complete(R.drawable.ic_last_search, R.string.last_search),
			ItemStoreInfo.complete(R.drawable.ic_favourite_user, R.string.favourite),
			ItemStoreInfo.complete(R.drawable.ic_stores_block, R.string.stores_had_been_blocked),
			ItemStoreInfo.complete(R.drawable.ic_login, R.string.logout, false),
			ItemStoreInfo.complete(R.drawable.ic_del_acc_permenently, R.string.del_account_permenantly, false),
		),
		onItemClick = { _, binding ->
			val fragment = binding.root.findFragmentOrNull<MyAccount2Fragment>() ?: return@RVItemCommonListUsage

			val navController = binding.root.findNavController()

			when (binding.constraintLayout.tag as? Int) {
				R.drawable.ic_profile -> {
					navController.navigateDeepLinkWithOptions(
						"profile",
						"grand.app.moon.profile"
					)
				}
				R.drawable.ic_view -> {
					AdsInteractedWithByOtherUsersFragment.launch(
						navController,
						app.getString(R.string.last_ads_seen),
						AdsInteractedWithByOtherUsersFragment.Type.LAST_VIEWED
					)
					/*navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.adsListFragment",
						paths = arrayOf(2.toString(), app.getString(R.string.last_ads_seen), true.toString())
					)*/
				}
				R.drawable.stores_followed -> {
					FollowedStoresFragment.launch(navController)
				}
				R.drawable.ic_last_search -> {
					AdsInteractedWithByOtherUsersFragment.launch(
						navController,
						app.getString(R.string.last_search),
						AdsInteractedWithByOtherUsersFragment.Type.LAST_SEARCHED
					)
					/*navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.adsListFragment",
						paths = arrayOf(5.toString(), app.getString(R.string.last_search), true.toString())
					)*/
				}
				R.drawable.ic_favourite_user -> {
					AdsInteractedWithByOtherUsersFragment.launch(
						navController,
						app.getString(R.string.favourite),
						AdsInteractedWithByOtherUsersFragment.Type.FAV
					)
					/*navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.adsListFragment",
						paths = arrayOf(1.toString(), app.getString(R.string.favourite), true.toString())
					)*/
				}
				R.drawable.ic_stores_block -> {
					navController.navigateDeepLinkWithOptions(
						"fragment-dest",
						"grand.app.moon.dest.storeBlockListFragment",
					)
				}
				R.drawable.ic_login -> {
					fragment.handleRetryAbleActionCancellableNullable(
						action = {
							logInUseCase.logoutSuspend()
						}
					) {
						fragment.logout()
						logOutUseCase()
						fragment.openActivityAndClearStack(HomeActivity::class.java)
					}
				}
				R.drawable.ic_del_acc_permenently -> {
					fragment.showCustomYesNoWarningDialog(
						fragment.getString(R.string.confirm_deletion),
						fragment.getString(R.string.are_you_sure_del_account)
					) { dialog ->
						fragment.handleRetryAbleActionCancellableNullable(
							action = {
								repoShop.deleteAccountPermanently()
							}
						) {
							fragment.logout()
							logOutUseCase()

							dialog.dismiss()

							fragment.showMessage(fragment.getString(R.string.done_successfully))

							fragment.openActivityAndClearStack(HomeActivity::class.java)
						}
					}
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
