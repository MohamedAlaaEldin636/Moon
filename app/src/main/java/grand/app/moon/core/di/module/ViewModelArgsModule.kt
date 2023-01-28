package grand.app.moon.core.di.module

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import grand.app.moon.extensions.asBundle
import grand.app.moon.presentation.auth.completeLogin.CompleteLoginFragmentArgs
import grand.app.moon.presentation.categories.AddAdvSubCategoriesListFragmentArgs
import grand.app.moon.presentation.home.AnnouncementDialogFragmentArgs
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragmentArgs
import grand.app.moon.presentation.myAds.*
import grand.app.moon.presentation.myStore.AddOrEditStoreCategoryFragmentArgs
import grand.app.moon.presentation.myStore.AddOrEditStoreSubCategoryFragmentArgs
import grand.app.moon.presentation.myStore.ShowImagesOrVideoDialogFragmentArgs

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

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
