package grand.app.moon.extensions

import android.content.Context
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.models.ItemAdvertisementInResponseHome
import grand.app.moon.presentation.home.models.ResponseStory
import grand.app.moon.presentation.map.model.ResponseMapData

fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	item: ItemAdvertisementInResponseHome
) {
	val store = item.store

	if (context.isLogin() && store?.id == this().id) {
		val stories = store.stories.orEmpty()

		if (stories.isNotEmpty()) {
			val responseStory = ResponseStory(
				stories = stories,
				phone = store.phone,
				image = store.image,
				name = store.name,
				createdAt = store.createdAt,
				nickname = store.nickname,
				id = store.id,
				countryCode = store.country?.countryCode
			)

			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.story.player",
				paths = arrayOf(
					listOf(responseStory).toJsonInlinedOrNull().orEmpty(),
					0.toString()
				)
			)
		}else {
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.create.store"
			)
		}
	}else {
		navController.navigate(
			R.id.nav_store,
			bundleOf(
				"id" to item.store?.id.orZero(),
				"type" to 3
			), Constants.NAVIGATION_OPTIONS
		)
	}
}

fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	mapData: ResponseMapData
) {
	if (mapData.stories.isNullOrEmpty()) {
		if (context.isLogin() && mapData.id == this().id) {
			// My Store
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.create.store"
			)
		}else {
			// Other stores
			navController.navigate(
				R.id.nav_store,
				bundleOf(
					"id" to mapData.id.orZero(),
					"type" to 3
				), Constants.NAVIGATION_OPTIONS
			)
		}
	}else {
		val responseStory = ResponseStory(
			stories = mapData.stories,
			phone = mapData.phone,
			image = mapData.image,
			name = mapData.name,
			createdAt = mapData.createdAt,
			nickname = mapData.nickname,
			id = mapData.id,
			countryCode = mapData.country?.countryCode
		)

		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.story.player",
			paths = arrayOf(
				listOf(responseStory).toJsonInlinedOrNull().orEmpty(),
				0.toString()
			)
		)
	}
}

fun UserLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
	context: Context,
	navController: NavController,
	id: Int?
) {
	if (context.isLogin() && id == this().id) {
		// My Store
		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.dest.create.store"
		)
	}else {
		// Other stores
		navController.navigate(
			R.id.nav_store,
			bundleOf(
				"id" to id.orZero(),
				"type" to 3
			), Constants.NAVIGATION_OPTIONS
		)
	}
}

fun UserLocalUseCase.goToAdvDetailsCheckIfMyAdv(
	context: Context,
	navController: NavController,
	item: ItemAdvertisementInResponseHome
) {
	val store = item.store

	if (context.isLogin() && store?.id == this().id) {
		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
			paths = arrayOf(item.id.orZero().toString())
		)
	}else {
		navController.navigate(
			R.id.nav_ads, bundleOf(
				"id" to item.id.orZero(),
				"type" to 2,
				"from_store" to false
			)
		)
	}
}

fun UserLocalUseCase.goToAdvDetailsCheckIfMyAdv(
	context: Context,
	navController: NavController,
	item: ResponseMapData
) {
	val store = item.store

	if (context.isLogin() && store?.id == this().id) {
		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
			paths = arrayOf(item.id.orZero().toString())
		)
	}else {
		navController.navigate(
			R.id.nav_ads, bundleOf(
				"id" to item.id.orZero(),
				"type" to 2,
				"from_store" to false
			)
		)
	}
}
