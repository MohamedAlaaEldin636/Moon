package grand.app.moon.presentation.auth.language.viewModels

import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.Bindable
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.BR
import grand.app.moon.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.core.extenstions.setInitialAppLaunch
import grand.app.moon.core.extenstions.setSelectedLangBefore
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.countries.entity.Country
import grand.app.moon.extensions.applicationScope
import grand.app.moon.extensions.findFragmentOrNull
import grand.app.moon.extensions.indexOfFirstOrNull
import grand.app.moon.extensions.navigateSafely
import grand.app.moon.presentation.auth.countries.adapters.CountriesAdapter
import grand.app.moon.presentation.auth.language.LanguageFragment
import grand.app.moon.presentation.auth.language.LanguageFragmentArgs
import grand.app.moon.presentation.auth.language.LanguageFragmentDirections
import grand.app.moon.presentation.base.extensions.openActivityAndClearStack
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.intro.IntroActivity
import grand.app.moon.presentation.splash.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
val inputStream: InputStream = // your input stream of application/octet-stream data
val outputFile: File = // your output file

// Create a MediaExtractor to extract the media data from the input stream
val extractor = MediaExtractor()
extractor.setDataSource(inputStream)

// Find the first track of the desired type (video)
var trackIndex = -1
for (i in 0 until extractor.trackCount) {
    val format = extractor.getTrackFormat(i)
    val mime = format.getString(MediaFormat.KEY_MIME)
    if (mime.startsWith("video/")) {
        extractor.selectTrack(i)
        trackIndex = i
        break
    }
}

if (trackIndex == -1) {
    // No video track found
    return
}

// Create a MediaMuxer to write the media data to the output file in the desired format (video/mp4)
val muxer = MediaMuxer(outputFile.absolutePath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4)

// Add the video track to the MediaMuxer
val trackFormat = extractor.getTrackFormat(trackIndex)
val muxerTrackIndex = muxer.addTrack(trackFormat)
muxer.start()

// Write the video data to the output file
val bufferInfo = MediaCodec.BufferInfo()
val buffer = ByteBuffer.allocate(1024 * 1024)
while (true) {
    val sampleSize = extractor.readSampleData(buffer, 0)
    if (sampleSize < 0) {
        break
    }

    bufferInfo.presentationTimeUs = extractor.sampleTime
    bufferInfo.flags = extractor.sampleFlags

    muxer.writeSampleData(muxerTrackIndex, buffer, bufferInfo)

    extractor.advance()
}

// Release the resources
extractor.release()
muxer.stop()
muxer.release()

 */

@HiltViewModel
class LanguagesViewModel @Inject constructor(
  val accountRepository: AccountRepository,
  val repoShop: RepoShop
) : BaseViewModel() {

  val languages = arrayListOf<Country>()
  @Bindable
  val adapter: CountriesAdapter = CountriesAdapter()
  @Bindable
  var lang : String = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

	var oldSetLang : String = accountRepository.getKeyFromLocal(Constants.LANGUAGE)

  var languageNavArgs: LanguageFragmentArgs? = null
  private  val TAG = "LanguagesViewModel"
  init {
    Log.d(TAG, "language Before : $lang")
    if(lang.isEmpty()) lang = Constants.DEFAULT_LANGUAGE
    Log.d(TAG, "language $lang ")
    languages.add(Country(name = "English" ,lang = "en",active = if(lang == "en" ) 1 else 0))
    languages.add(Country(name = "عربى" ,lang = "ar",active = if(lang == "ar") 1 else 0))
    Log.d(TAG, "language: "+languages[0].active)
    Log.d(TAG, "language: "+languages[1].active)
	  languages.indexOfFirstOrNull { it.active == 1 }?.also { index ->
			adapter.lastPosition = index
			adapter.lastSelected = languages[index].id
		  //this.lang = languages[index].lang
		  //oldSetLang = languages[index].lang
	  }
    updateAdapter(languages)
  }
  fun updateAdapter(countries: List<Country>) {
    adapter.differ.submitList(countries)
    notifyPropertyChanged(BR.adapter)
  }

  fun updateLanguage(lang: String) {
    accountRepository.saveKeyToLocal(Constants.LANGUAGE,lang)
    this.lang = lang
    notifyPropertyChanged(BR.lang)
  }

	fun updateLanguageWithoutLocalSave(lang: String) {
		this.lang = lang
		notifyPropertyChanged(BR.lang)
	}
	fun updateLanguageLocalSave(lang: String) {
		accountRepository.saveKeyToLocal(Constants.LANGUAGE,lang)
	}

  fun next(v : View) {
	  val fragment = v.findFragmentOrNull<LanguageFragment>() ?: return
	  val activity = fragment.activity ?: return

	  accountRepository.saveKeyToLocal(Constants.LANGUAGE, lang)

	  if (languageNavArgs?.type == Constants.SPLASH) {
		  fragment.findNavController().navigateSafely(
			  LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2()
		  )
	  }else {
		  //fragment.findNavController().navigateUp()
	  }

	  if (true || activity.isSameAsCurrentLocale(lang).not()) {
		  activity.setCurrentLangFromSharedPrefs(lang)

		  activity.applicationScope?.launch {
			  repoShop.fetchAllCategoriesAndSaveThemLocallyIfPossible()
		  }

		  //activity.setCurrentLocale(lang, autoRecreate)

		  if (activity is IntroActivity) {
			  activity.recreate()
		  }else {
			  activity.openActivityAndClearStack(MASplash2Activity::class.java)
		  }

		  //activity.getContextForLocaleMA(lang)

		  //activity.recreate()
		  //activity.openActivityAndClearStack(activity.javaClass/*MASplash2Activity::class.java*/)
	  }

    /*Log.d(TAG, "next: ")
    languageNavArgs?.type?.let {
      Log.d(TAG, "next: $it")
      if(it == Constants.SPLASH) {
	      if (this.lang == oldSetLang) {
		      v.findNavController().navigate(LanguageFragmentDirections.actionLanguageFragmentToCountriesFragment2())
	      }else {
		      accountRepository.saveKeyToLocal(Constants.LANGUAGE_SELECTED_ON_APP_LAUNCH_BEFORE, true.toString())

		      clickEvent.value = Constants.BACK
	      }
			} else {
	      clickEvent.value = Constants.BACK
      }
    }*/
  }
  fun back(v: View){
    v.findNavController().popBackStack()
  }
}