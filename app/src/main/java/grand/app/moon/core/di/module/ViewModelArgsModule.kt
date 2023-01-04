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
import grand.app.moon.presentation.myAds.AddAdvFinalPageFragmentArgs
import grand.app.moon.presentation.myAds.AddAdvertisementFragmentArgs

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

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
