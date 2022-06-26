package grand.app.moon.presentation.auth.profile

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentCropBinding
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.base.extensions.*
import grand.app.moon.presentation.base.utils.Constants
import java.io.ByteArrayOutputStream
import java.util.*

@AndroidEntryPoint
class CropFragment : BaseFragment<FragmentCropBinding>() {

  val args: CropFragmentArgs by navArgs()
  private val viewModel: ProfileViewModel by viewModels()

  override
  fun getLayoutId() = R.layout.fragment_crop

  override
  fun setBindingVariables() {
    binding.viewModel = viewModel
    binding.cropImageView.setImageUriAsync(args.request.uri)
  }

  private val TAG = "CropFragment"

  private fun getUriFromBitmapRetrievedByCamera(bitmap: Bitmap): Uri {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    val byteArray = stream.toByteArray()
    val compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)

    val path = MediaStore.Images.Media.insertImage(
      requireContext().contentResolver,
      compressedBitmap,
      Date(System.currentTimeMillis()).toString() + "photo",
      null
    )
    return Uri.parse(path)
  }

  override
  fun setupObservers() {
    binding.btnCrop.setOnClickListener {
      val bitmap = binding.cropImageView.getCroppedImage(3000, 3000)
      args.request.uriCrop = bitmap?.let { it1 -> getUriFromBitmapRetrievedByCamera(it1) }
      val bundle = Bundle()
      bundle.putSerializable(Constants.REQUEST,args.request)
      setFragmentResult(Constants.BUNDLE, bundle)
      backToPreviousScreen()
    }
  }
}