package grand.app.moon.core.di.module

import androidx.lifecycle.SavedStateHandle
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import grand.app.moon.extensions.asBundle
import grand.app.moon.presentation.home.AppGlobalAnnouncementDialogFragmentArgs

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelArgsModule {

	@Provides
	fun provideAppGlobalAnnouncementDialogFragmentArgs(state: SavedStateHandle): AppGlobalAnnouncementDialogFragmentArgs {
		return AppGlobalAnnouncementDialogFragmentArgs.fromBundle(state.asBundle())
	}

}
