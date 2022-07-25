package grand.app.moon.presentation.auth.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
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
import grand.app.moon.databinding.FragmentProfileBinding
import grand.app.moon.domain.auth.entity.request.UpdateProfileRequest
import grand.app.moon.domain.utils.Resource
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import kotlinx.coroutines.flow.collect
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

  private val viewModel: ProfileViewModel by viewModels()

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
          pickImageOrRequestPermissions()
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
            backToPreviousScreen()
          }
          is Resource.Failure -> {
            hideLoading()
            handleApiError(it)
          }
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
      activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
    }else {
      // From gallery
      activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
    }
  }

  private val activityResultImageCamera = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult
      viewModel.request.uri = getUriFromBitmapRetrievedByCamera(bitmap)
      loadImageProfile()
      navigateSafe(ProfileFragmentDirections.actionProfileFragmentToCropFragment(viewModel.request))
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


  private fun getUriFromBitmapRetrievedByCamera(bitmap: Bitmap): Uri {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 70, stream)
    val byteArray = stream.toByteArray()
    val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    val path = MediaStore.Images.Media.insertImage(
      requireContext().contentResolver, compressedBitmap, Date(System.currentTimeMillis()).toString() + "photo", null
    )
    return Uri.parse(path)
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