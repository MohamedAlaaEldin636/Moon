package grand.app.moon.core.di.module

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import grand.app.moon.extensions.asBundle
import grand.app.moon.presentation.auth.completeLogin.CompleteLoginFragmentArgs
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.home.*
import grand.app.moon.presentation.map.MapOfDataFragmentArgs
import grand.app.moon.presentation.myAds.*
import grand.app.moon.presentation.myStore.AddOrEditStoreCategoryFragmentArgs
import grand.app.moon.presentation.myStore.AddOrEditStoreSubCategoryFragmentArgs
import grand.app.moon.presentation.myStore.ExploreInShopInfoFragmentArgs
import grand.app.moon.presentation.myStore.ShowImagesOrVideoDialogFragmentArgs
import grand.app.moon.presentation.myStore.StoryInShopInfoFragmentArgs
import grand.app.moon.presentation.packages.SuccessShopSubscriptionFragmentArgs
import grand.app.moon.presentation.stats.GeneralStatsFragmentArgs
import grand.app.moon.presentation.stats.StatsUsersHistoryFragmentArgs

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

	@Provides
	fun provideAllAdsOfCategoryFragmentArgs(state: SavedStateHandle): AllAdsOfCategoryFragmentArgs {
		return AllAdsOfCategoryFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAdsSortDialogFragmentArgs(state: SavedStateHandle): AdsSortDialogFragmentArgs {
		return AdsSortDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAllAdsFragmentArgs(state: SavedStateHandle): AllAdsFragmentArgs {
		return AllAdsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideCategoryDetails2FragmentArgs(state: SavedStateHandle): CategoryDetails2FragmentArgs {
		return CategoryDetails2FragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideFilterAllFragmentArgs(state: SavedStateHandle): FilterAllFragmentArgs {
		return FilterAllFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideStoresSortDialogFragmentArgs(state: SavedStateHandle): StoresSortDialogFragmentArgs {
		return StoresSortDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAllStoresFragmentArgs(state: SavedStateHandle): AllStoresFragmentArgs {
		return AllStoresFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideSimpleUserListOfInteractionsFragmentArgs(state: SavedStateHandle): SimpleUserListOfInteractionsFragmentArgs {
		return SimpleUserListOfInteractionsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideSingleOrMultiSelectionFragmentArgs(state: SavedStateHandle): SingleOrMultiSelectionFragmentArgs {
		return SingleOrMultiSelectionFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideMapOfDataFragmentArgs(state: SavedStateHandle): MapOfDataFragmentArgs {
		return MapOfDataFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideStoryPlayerFragmentArgs(state: SavedStateHandle): StoryPlayerFragmentArgs {
		return StoryPlayerFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideHomeExploreSubsectionFragmentArgs(state: SavedStateHandle): HomeExploreSubsectionFragmentArgs {
		return HomeExploreSubsectionFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideStatsUsersHistoryFragmentArgs(state: SavedStateHandle): StatsUsersHistoryFragmentArgs {
		return StatsUsersHistoryFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideMyAdsFragmentArgs(state: SavedStateHandle): MyAdsFragmentArgs {
		return MyAdsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideStoryInShopInfoFragmentArgs(state: SavedStateHandle): StoryInShopInfoFragmentArgs {
		return StoryInShopInfoFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideExploreInShopInfoFragmentArgs(state: SavedStateHandle): ExploreInShopInfoFragmentArgs {
		return ExploreInShopInfoFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideSearchResultsFragmentArgs(state: SavedStateHandle): SearchResultsFragmentArgs {
		return SearchResultsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideGeneralStatsFragmentArgs(state: SavedStateHandle): GeneralStatsFragmentArgs {
		return GeneralStatsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideSuccessShopSubscriptionFragmentArgs(state: SavedStateHandle): SuccessShopSubscriptionFragmentArgs {
		return SuccessShopSubscriptionFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideRateInAdvDialogFragmentArgs(state: SavedStateHandle): RateInAdvDialogFragmentArgs {
		return RateInAdvDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAdvClientsReviewsFragmentArgs(state: SavedStateHandle): AdvClientsReviewsFragmentArgs {
		return AdvClientsReviewsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideShowImagesOrVideoDialogFragmentArgs(state: SavedStateHandle): ShowImagesOrVideoDialogFragmentArgs {
		return ShowImagesOrVideoDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAddOrEditStoreSubCategoryFragmentArgs(state: SavedStateHandle): AddOrEditStoreSubCategoryFragmentArgs {
		return AddOrEditStoreSubCategoryFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAddOrEditStoreCategoryFragmentArgs(state: SavedStateHandle): AddOrEditStoreCategoryFragmentArgs {
		return AddOrEditStoreCategoryFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideMyAdOrShopPackageFragmentArgs(state: SavedStateHandle): MyAdOrShopPackageFragmentArgs {
		return MyAdOrShopPackageFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideMakeAdvOrStorePremiumFragmentArgs(state: SavedStateHandle): MakeAdvOrStorePremiumFragmentArgs {
		return MakeAdvOrStorePremiumFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideLocationViewerFragmentArgs(state: SavedStateHandle): LocationViewerFragmentArgs {
		return LocationViewerFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideMyAdvDetailsFragmentArgs(state: SavedStateHandle): MyAdvDetailsFragmentArgs {
		return MyAdvDetailsFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideLocationSelectionFragmentArgs(state: SavedStateHandle): LocationSelectionFragmentArgs {
		return LocationSelectionFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAddAdvertisementFragmentArgs(state: SavedStateHandle): AddAdvertisementFragmentArgs {
		return AddAdvertisementFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAppGlobalAnnouncementDialogFragmentArgs(state: SavedStateHandle): AppGlobalAnnouncementDialogFragmentArgs {
		return AppGlobalAnnouncementDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAnnouncementDialogFragmentArgs(state: SavedStateHandle): AnnouncementDialogFragmentArgs {
		return AnnouncementDialogFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAddAdvSubCategoriesListFragmentArgs(state: SavedStateHandle): AddAdvSubCategoriesListFragmentArgs {
		return AddAdvSubCategoriesListFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideAddAdvFinalPageFragmentArgs(state: SavedStateHandle): AddAdvFinalPageFragmentArgs {
		return AddAdvFinalPageFragmentArgs.fromBundle(state.asBundle())
	}

	@Provides
	fun provideCompleteLoginFragmentArgs(state: SavedStateHandle): CompleteLoginFragmentArgs {
		return CompleteLoginFragmentArgs.fromBundle(state.asBundle())
	}

}
