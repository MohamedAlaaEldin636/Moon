package grand.app.moon.presentation.contactUs.viewModels

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.ObservableField
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import grand.app.moon.domain.settings.models.ContactUsRequest
import grand.app.moon.domain.settings.use_case.SettingsUseCase
import grand.app.moon.domain.utils.BaseResponse
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseViewModel
import grand.app.moon.presentation.base.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import es.dmoral.toasty.Toasty
import grand.app.moon.BR
import grand.app.moon.R
import grand.app.moon.core.extenstions.callPhone
import grand.app.moon.core.extenstions.navigateMap
import grand.app.moon.core.extenstions.sendMail
import grand.app.moon.domain.auth.entity.model.User
import grand.app.moon.domain.settings.models.AppInfoResponse
import grand.app.moon.domain.settings.models.SettingsData
import grand.app.moon.domain.utils.isValidEmail
import grand.app.moon.helpers.PopUpMenuHelper
import grand.app.moon.presentation.base.utils.Constants
import grand.app.moon.presentation.social.SocialAdapter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ContactUsViewModel @Inject constructor(
  private val settingsUseCase: SettingsUseCase,

  private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

//  val _settingsResponse =
//    MutableStateFlow<Resource<BaseResponse<ArrayList<SettingsData>>>>(Resource.Default)
//  val settingsResponse = _settingsResponse

  val appInfoResponse = ObservableField<AppInfoResponse>()

  var type = ObservableField("")
  val title = ObservableField("")
  val title1 = ObservableField("")
  val title2 = ObservableField("")

  var imageSelect = ObservableField<Int>(R.drawable.ic_email)

  var request = ContactUsRequest()
  val response = MutableStateFlow<Resource<BaseResponse<*>>>(Resource.Default)
  val _contactReasonResponse = MutableStateFlow<Resource<BaseResponse<List<SettingsData>>>>(Resource.Default)
  val contactReasonResponse = _contactReasonResponse

  val socialResponse =
    MutableStateFlow<Resource<BaseResponse<List<SettingsData>>>>(Resource.Default)

  val _contactAppInfoResponse = MutableStateFlow<Resource<BaseResponse<AppInfoResponse>>>(Resource.Default)

  var validationException = SingleLiveEvent<Int>()


  @Bindable
  var socialAdapter = SocialAdapter()


  init {
    //2=> get Contact Us
    getContactUs()
  }

  private fun getContactUs() {
    settingsUseCase.settings("2").onEach { result ->
      contactReasonResponse.value = result
    }.launchIn(viewModelScope)

    settingsUseCase.settingsAppInfo("13").onEach { result ->
      _contactAppInfoResponse.value = result
    }.launchIn(viewModelScope)

    settingsUseCase.settings("3",false)
      .onEach { result ->
        socialResponse.value = result
      }
      .launchIn(viewModelScope)


  }

  fun onContactClicked(v: View) {
    if (request.name.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_name));
      return
    }
    if (request.message.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_message));
      return
    }

    if (request.email.trim().isEmpty()) {
      showError(v.context, v.context.getString(R.string.please_enter_your_email));
      return
    }
    if (!request.email.isValidEmail()) {
      showError(v.context, v.context.getString(R.string.please_enter_valid_email));
      return
    }
    if (request.reason_id == -1) {
      showError(v.context, v.context.getString(R.string.please_enter_your_reason));
      return
    }
    settingsUseCase(request).onEach {
      response.value = it
    }.launchIn(viewModelScope)
  }

  fun select(v: View, type: String) {
    this.type.set(type)
    when (type) {
      Constants.MAIL -> {
        setData(v.context.getString(R.string.send_mail), "", "", R.drawable.ic_email)
        if(appInfoResponse.get()?.email?.isNotEmpty() == true){
          this.title1.set(appInfoResponse.get()?.email!![0].content)
          if(appInfoResponse.get()?.email?.size!! > 1)
            this.title2.set(appInfoResponse.get()!!.email[1].content)
        }
      }
      Constants.LOCATION -> {
        setData(v.context.getString(R.string.main_moon_location), "", "", R.drawable.ic_pin)
        if(appInfoResponse.get()?.address?.isNotEmpty() == true){
          this.title1.set(appInfoResponse.get()?.address!![0].content)
          if(appInfoResponse.get()?.address?.size!! > 1)
            this.title2.set(appInfoResponse.get()!!.address[1].content)
        }
      }
      Constants.PHONE -> {
        setData(v.context.getString(R.string.call_for_phone_list), "", "", R.drawable.ic_phone)
        if(appInfoResponse.get()?.phone?.isNotEmpty() == true){
          this.title1.set(appInfoResponse.get()?.phone!![0].content)
          if(appInfoResponse.get()?.phone?.size!! > 1)
            this.title2.set(appInfoResponse.get()!!.phone[1].content)
        }
      }
    }
  }

  fun setData(title: String, title1: String, title2: String, image: Int) {
    this.title.set(title)
    this.imageSelect.set(image)
    this.title1.set(title1)
    this.title2.set(title2)
  }

  val titles = arrayListOf<SettingsData>()
  val items = arrayListOf<String>()

  var popUpMenuHelper: PopUpMenuHelper = PopUpMenuHelper()

  private  val TAG = "ContactUsViewModel"
  fun chooseReason(v: View) {
    Log.d(TAG, "chooseReason: ${items.size}")
    popUpMenuHelper.openPopUp(v.context, v, items) { position ->
      Log.d(TAG, "chooseReason: $position")
      Log.d(TAG, "chooseReason: ${items[position]}")
      request.contact.set(items[position])
      request.reason_id = titles[position].id
    }
  }

  fun setData(title: List<SettingsData>) {
    titles.clear()
    titles.addAll(title)
    title.forEach {
      Log.d(TAG, "setData: ")
      items.add(it.content)
    }
  }

  fun clickTitle(position:Int , v: View){
    when (this.type.get()) {
      Constants.MAIL -> {
        v.context.sendMail(appInfoResponse.get()!!.email[position].content,v.context.getString(R.string.app_name),"")
      }
      Constants.PHONE -> {
        v.context.callPhone(appInfoResponse.get()!!.phone[position].content)
      }
    }
  }

  fun setSocial(data: List<SettingsData>) {
    socialAdapter.insertData(data)
    notifyPropertyChanged(BR.adapter)
  }

  fun setData(appInfoResponse: AppInfoResponse) {
    this.appInfoResponse.set(appInfoResponse)
  }


}