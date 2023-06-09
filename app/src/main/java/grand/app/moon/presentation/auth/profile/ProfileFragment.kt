package grand.app.moon.presentation.auth.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import grand.app.moon.core.extenstions.checkSelfPermissionGranted
import grand.app.moon.core.extenstions.convertToString
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.core.extenstions.showPopup
import grand.app.moon.databinding.FragmentProfileBinding
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.shop.MAImagesOrVideo
import grand.app.moon.domain.utils.Resource
import grand.app.moon.extensions.PickImagesOrVideoHandler
import grand.app.moon.extensions.toStringOrEmpty
import grand.app.moon.helpers.utils.getUriFromBitmapRetrievedByCamera
import grand.app.moon.helpers.utils.handleCaptureImageRotation
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

  private val viewModel: ProfileViewModel by viewModels()

	var gettingImagesOrVideoHandler: PickImagesOrVideoHandler? = null
		private set

	override fun onCreate(savedInstanceState: Bundle?) {
		gettingImagesOrVideoHandler = PickImagesOrVideoHandler(
			this,
			PickImagesOrVideoHandler.SupportedMediaType.IMAGE,
			requestMultipleImages = false,
			getAnchor = { _binding?.imgProfile }
		) { uris, _, _ ->
			val uri = uris.firstOrNull() ?: return@PickImagesOrVideoHandler

			viewModel.request.uri = uri
			loadImageProfile()
			navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
		}

		super.onCreate(savedInstanceState)
	}

	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		return super.onCreateView(inflater, container, savedInstanceState)?.also {
			binding.ccp.registerCarrierNumberEditText(binding.edtLoginPhone)

			binding.edtLoginPhone.setText(viewModel.phone.value.orEmpty())
			binding.edtLoginPhone.doAfterTextChanged {
				viewModel.phone.value = it.toStringOrEmpty()
			}
		}
	}

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    setFragmentResultListener(Constants.BUNDLE) { requestKey, bundle ->
      if (bundle.containsKey(Constants.REQUEST)) {
        val request = bundle.getSerializable(Constants.REQUEST) as UpdateProfileRequest
        if(request.uriCrop != null){
          request.uri = request.uriCrop
          loadImageProfile()
          request.uriCrop = null
        }
      }
    }

	  viewModel.phone.observe(viewLifecycleOwner) {
		  viewModel.showValidPhoneNum.value = binding.ccp.isValidFullNumber
	  }
  }
  
  override
  fun getLayoutId() = R.layout.fragment_profile

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    viewModel.request.country_code
    var countryCode = viewModel.request.country_code
    if(countryCode.startsWith("+")) countryCode = countryCode.substring(1)
    binding.ccp.setCountryForPhoneCode(countryCode.toInt())
  }

  private val TAG = "SignUpFragment"

  override
  fun setupObservers() {
    initView()
    viewModel.submitEvent.observe(this) {
      when (it) {
        Constants.PICKER_IMAGE -> {
					gettingImagesOrVideoHandler?.requestImageOrVideo()
	        /*permissionLocationRequest.launch(arrayOf(
		        Manifest.permission.READ_EXTERNAL_STORAGE,
		        Manifest.permission.WRITE_EXTERNAL_STORAGE,
		        Manifest.permission.CAMERA,
	        ))*/
//          singleTedBottomPicker(requireActivity())
        }
      }
    }
    selectedImages.observeForever { result ->
      result.path?.let { path ->
        viewModel.request.setImage(path, Constants.IMAGE)
        binding.imgProfile.setImageURI(result)
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.response.collect {
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()
            showMessage(it.value.message)
            if (viewModel.phoneChanged) {
	            val gson = context?.convertToString(viewModel.request)
	            Log.d(TAG, "setupObservers: $gson")
	            //      app:uri="confirmCode://grand.app.moon.confirm.code/{country_code}/{phone}/{type}/{profile}" />
	            val uri = Uri.Builder()
		            .scheme("confirmCode")
		            .authority("grand.app.moon.confirm.code")
		            .appendPath(viewModel.request.country_code)
		            .appendPath(viewModel.request.phone)
		            .appendPath("verify")
		            //.appendPath(gson.orEmpty())
		            .build()
	            val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
	            findNavController().navigate(request)
            }else {
	            backToPreviousScreen()
            }
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
	        else -> {}
        }
      }
    }

    lifecycleScope.launchWhenResumed {
      viewModel.loginResponse.collect {
        Log.d(TAG, "setupObservers: ")
        when (it) {
          Resource.Loading -> {
            hideKeyboard()
            showLoading()
          }
          is Resource.Success -> {
            hideLoading()

            Log.d(TAG, "setupObservers: WORKED HERE")

	          //UpdateProfileRequest
            val gson = context?.convertToString(viewModel.request)
            Log.d(TAG, "setupObservers: $gson")
            val uri = Uri.Builder()
              .scheme("confirmCode")
              .authority("grand.app.moon.confirm.code")
              .appendPath(gson.orEmpty())
              .build()
            val request = NavDeepLinkRequest.Builder.fromUri(uri).build()
            findNavController().navigate(request)


//            navigateSafe(
//              LogInFragmentDirections.actionLogInFragmentToFragmentConfirmCode(
//                viewModel.request.country_code,viewModel.request.phone, Constants.Verify
//              )
//            )
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }

	        else -> {}
        }
      }
    }

  }

  fun pickImageOrRequestPermissions() {
    when {
      requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
        && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        && requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
        pickImageViaChooser()
      }
      requireContext().checkSelfPermissionGranted(Manifest.permission.READ_EXTERNAL_STORAGE)
        && requireContext().checkSelfPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) -> {
        pickImage(false)
      }
      requireContext().checkSelfPermissionGranted(Manifest.permission.CAMERA) -> {
        pickImage(true)
      }
      else -> {
        permissionLocationRequest.launch(arrayOf(
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE,
          Manifest.permission.CAMERA,
        ))
      }
    }
  }

  private val permissionLocationRequest = registerForActivityResult(
    ActivityResultContracts.RequestMultiplePermissions()
  ) { permissions ->
    when {
      permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.CAMERA] == true -> {
        pickImageViaChooser()
      }
      permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
        && permissions[Manifest.permission.WRITE_EXTERNAL_STORAGE] == true -> {
        pickImage(false)
      }
      permissions[Manifest.permission.CAMERA] == true -> {
        pickImage(true)
      }
      else -> {
        showMessage(getString(R.string.you_didn_t_accept_permission))
      }
    }
  }

  private fun pickImageViaChooser() {
    val camera = getString(R.string.camera)
    val gallery = getString(R.string.gallery)

    binding.imgProfile.showPopup(listOf(camera, gallery), listener = {
      pickImage(it.title?.toString() == camera)
    })
  }

  private fun pickImage(fromCamera: Boolean) {
    if (fromCamera) {
      imageUri = createImageUri()!!
      activityResultImageCameraFile.launch(imageUri)
    }else {
      // From gallery
      activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }
  }

  fun createImageUri(): Uri? {
    fileCameraCapture = File(
      activity?.applicationContext?.filesDir,
      "camera_photo${SimpleDateFormat("yyyy-MM-dd_HH:mm:ss", Locale.ENGLISH)}.png"
    )
    activity?.applicationContext?.let {
      return FileProvider.getUriForFile(it, "grand.app.moon.fileprovider", fileCameraCapture!!)
    }
    return null
  }

  var imageUri: Uri? = null
  var fileCameraCapture: File? = null
  private val activityResultImageCameraFile = registerForActivityResult(
    ActivityResultContracts.TakePicture()
  ) {
    Log.d(TAG, "pickImageViaChooser: on listener Fetch")
    if (it != null && it && imageUri != null) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        val bitmap = handleCaptureImageRotation(fileCameraCapture,imageUri)
        bitmap?.let {
          imageUri = getUriFromBitmapRetrievedByCamera(it)
          viewModel.request.uri = imageUri
          loadImageProfile()
          navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
        }
      }
    }
  }


  private val activityResultImageGallery = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      val uri = it.data?.data ?: return@registerForActivityResult
      viewModel.request.uri = uri
      loadImageProfile()
      navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
    }
  }


  fun loadImageProfile(){
    Glide.with(this)
      .load(viewModel.request.uri)
      .apply(RequestOptions().centerCrop())
      .into(binding.imgProfile)
  }

  private fun initView() {
    if(!viewModel.user.country_code.contains("+"))
      viewModel.request.country_code = "+${viewModel.user.country_code}"
    binding.ccp.setDefaultCountryUsingNameCode(viewModel.user.country_code)
    binding.ccp.setOnCountryChangeListener {
      viewModel.request.country_code = "+${binding.ccp.selectedCountryCode}"
    }
  }
}