package grand.app.moon.presentation.auth.profile

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.hbb20.CountryCodePicker
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentProfileBinding
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

  override
  fun getLayoutId() = R.layout.fragment_profile

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
  }

  private val TAG = "SignUpFragment"

  override
  fun setupObservers() {
    initView()
    viewModel.submitEvent.observe(this) {
      when (it) {
        Constants.PICKER_IMAGE -> {
//          val camera = getString(R.string.camera)
//          val gallery = getString(R.string.gallery)
//
//          binding.imgProfile.showPopup(listOf(camera, gallery)) {
//            if (it.title?.toString() == camera) {
//              activityResultImageCamera.launch(Intent(MediaStore.ACTION_IMAGE_CAPTURE))
//            }else {
//              // From gallery
//              activityResultImageGallery.launch(Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI))
//            }
//          }

          singleTedBottomPicker(requireActivity())
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
  }

  private val activityResultImageCamera = registerForActivityResult(
    ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      val bitmap = it.data?.extras?.get("data") as? Bitmap ?: return@registerForActivityResult

      val uri = getUriFromBitmapRetrievedByCamera(bitmap)

//      viewModel.imageUri = uri
//
//      Glide.with(this)
//        .load(uri)
//        .apply(RequestOptions().centerCrop())
//        .into(binding.imageView)
    }
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