package grand.app.moon.extensions

import android.content.Context
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import grand.app.moon.R
import grand.app.moon.core.extenstions.isLogin
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.home.OtherAdvDetailsFragment
import grand.app.moon.presentation.home.OtherStoreDetailsFragment
import grand.app.moon.presentation.home.models.*
import grand.app.moon.presentation.map.model.ResponseMapData

fun ResponseMapData.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = stories,
		phone = phone,
		image = image,
		name = name,
		createdAt = createdAt,
		nickname = nickname,
		id = id,
		countryCode = country?.countryCode
	)
}
fun ItemAdvertisementInResponseHome.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = store?.stories,
		phone = store?.phone,
		image = store?.image,
		name = store?.name,
		createdAt = store?.createdAt,
		nickname = store?.nickname,
		id = store?.id,
		countryCode = store?.country?.countryCode
	)
}
fun ItemAdvertisementInResponseHome.Store?.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = this?.stories,
		phone = this?.phone,
		image = this?.image,
		name = this?.name,
		createdAt = this?.createdAt,
		nickname = this?.nickname,
		id = this?.id,
		countryCode = this?.country?.countryCode
	)
}
fun ItemStoreInResponseHome?.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = this?.stories,
		phone = this?.phone,
		image = this?.image,
		name = this?.name,
		createdAt = this?.createdAt,
		nickname = this?.nickname,
		id = this?.id,
		countryCode = this?.country?.countryCode
	)
}
fun ResponseSearchResult?.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = this?.stories,
		phone = this?.phone,
		image = this?.image,
		name = this?.name,
		createdAt = this?.createdAt,
		nickname = this?.nickname,
		id = this?.id,
		countryCode = this?.country?.countryCode
	)
}
fun ItemStoreInHomeExplore?.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = this?.stories,
		phone = this?.phone,
		image = this?.logo,
		name = this?.name,
		createdAt = this?.createdAt,
		nickname = this?.nickname,
		id = this?.id,
		countryCode = this?.country?.countryCode
	)
}
fun ResponseStoreDetails?.toResponseStory(): ResponseStory {
	return ResponseStory(
		stories = this?.stories,
		phone = this?.phone,
		image = this?.image,
		name = this?.name,
		createdAt = this?.createdAt,
		nickname = this?.nickname,
		id = this?.id,
		countryCode = this?.country?.countryCode
	)
}

fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	item: ResponseStoreDetails
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, item.id, item.toResponseStory())
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	item: ItemAdvertisementInResponseHome
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, item.store?.id, item.toResponseStory())
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	mapData: ResponseMapData
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, mapData.id, mapData.toResponseStory())
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	mapData: ResponseSearchResult
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, mapData.id, mapData.toResponseStory(), false)
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	item: ItemAdvertisementInResponseHome.Store?
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, item?.id, item.toResponseStory())
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	item: ItemStoreInResponseHome?
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, item?.id, item.toResponseStory())
fun UserLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
	context: Context,
	navController: NavController,
	id: Int?
) = goToStoreStoriesOrDetailsCheckIfMyStore(context, navController, id, null)
fun UserLocalUseCase.goToStoreStoriesOrDetailsCheckIfMyStore(
	context: Context,
	navController: NavController,
	storeId: Int?,
	responseStory: ResponseStory?,
	fromViewNotSearch: Boolean = true
) {
	if (responseStory?.stories.isNullOrEmpty()) {
		if (context.isLogin() && storeId == this().id) {
			// My Store
			navController.navigateDeepLinkWithOptions(
				"fragment-dest",
				"grand.app.moon.dest.create.store"
			)
		}else {
			// Other stores
			OtherStoreDetailsFragment.launch(
				navController,
				storeId.orZero(),
				fromViewNotSearch
			)
			/*navController.navigate(
				R.id.nav_store,
				bundleOf(
					"id" to storeId.orZero(),
					"type" to 3
				), Constants.NAVIGATION_OPTIONS
			)*/
		}
	}else {
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
		OtherAdvDetailsFragment.launch(navController, item.id.orZero())
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
		OtherAdvDetailsFragment.launch(navController, item.id.orZero())
	}
}

fun UserLocalUseCase.goToAdvDetailsCheckIfMyAdv(
	context: Context,
	navController: NavController,
	advId: Int,
	isMyAdv: Boolean,
) {
	if (context.isLogin() && isMyAdv) {
		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
			paths = arrayOf(advId.toString())
		)
	}else {
		OtherAdvDetailsFragment.launch(navController, advId)
	}
}

fun UserLocalUseCase.goToAdvDetailsCheckIfMyAdv(
	context: Context,
	navController: NavController,
	item: ResponseSearchResult
) {
	val store = item.store

	if (context.isLogin() && store?.id == this().id) {
		navController.navigateDeepLinkWithOptions(
			"fragment-dest",
			"grand.app.moon.presentation.myAds.dest.my.adv.details.id",
			paths = arrayOf(item.id.orZero().toString())
		)
	}else {
		OtherAdvDetailsFragment.launch(navController, item.id.orZero(), false)
	}
}
