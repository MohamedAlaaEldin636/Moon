package grand.app.moon.presentation.intro.tutorial

import androidx.core.content.ContextCompat
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.general.use_case.GeneralUseCases
import grand.app.moon.domain.intro.entity.AppTutorial
import grand.app.moon.domain.intro.use_case.IntroUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.domain.account.repository.AccountRepository
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class TutorialViewModel @Inject constructor(
  private val generalUseCases: GeneralUseCases,
  private val settingsUseCase: SettingsUseCase,
  val accountRepository: AccountRepository
) : BaseViewModel() {
  var position = ObservableInt(-1)
  private val _appTutorialResponse =
    MutableStateFlow<Resource<BaseResponse<List<AppTutorial>>>>(Resource.Default)
  val appTutorialResponse = _appTutorialResponse

  val dots = mutableListOf<Int>(R.drawable.dot_1,R.drawable.dot_2,R.drawable.dot_3)
  val tutorial = ObservableField<AppTutorial>()
  val openIntro = SingleLiveEvent<Void>()

  init {
    settingsUseCase.onBoard("1").onEach { result ->
      println(result.toString())
      _appTutorialResponse.value = result
    }
      .launchIn(viewModelScope)
  }

  fun setFirstTime(isFirstTime: Boolean) {
    viewModelScope.launch {
      generalUseCases.setFirstTimeUseCase(isFirstTime)
    }
  }

  fun next(){
    position.set(position.get()+1)
    if(position.get() < data.size)
      updateIntro()
    else{
      submitEvent.value = Constants.SKIP
    }
  }

  fun skip(){
    submitEvent.value = Constants.SKIP
  }

  val data = ArrayList<AppTutorial>()
  fun setData(data: List<AppTutorial>) {
    if(data.isNotEmpty()) data[0].drawable = dots[0]
    if(data.size > 1) data[1].drawable = dots[1]
    if(data.size > 2) data[2].drawable = dots[2]
    this.data.addAll(data)
    if(data.isNotEmpty()) {
      next()
    }
  }

  fun updateIntro(){
    if(position.get() > -1 && position.get() < data.size) {
      tutorial.set(data[position.get()])
      submitEvent.value = Constants.NEXT
    }
  }



}