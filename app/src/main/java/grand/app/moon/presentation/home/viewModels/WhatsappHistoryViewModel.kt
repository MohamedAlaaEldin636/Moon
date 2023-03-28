package grand.app.moon.presentation.home.viewModels

import android.annotation.SuppressLint
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.navigation.findNavController
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemWhatsappHistoryBinding
import grand.app.moon.domain.account.use_case.UserLocalUseCase
import grand.app.moon.extensions.*
import grand.app.moon.presentation.home.models.ResponseWhatsappHistoryItem
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WhatsappHistoryViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val userLocalUseCase: UserLocalUseCase,
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	val storesHistory = repoShop.getWhatsappHistory()

	@SuppressLint("SetTextI18n")
	val adapter = RVPagingItemCommonListUsage<ItemWhatsappHistoryBinding, ResponseWhatsappHistoryItem>(
		R.layout.item_whatsapp_history,
		onItemClick = { _, binding ->
			val item = binding.root.getTagJson<ResponseWhatsappHistoryItem>() ?: return@RVPagingItemCommonListUsage

			userLocalUseCase.goToStoreDetailsIgnoringStoriesCheckIfMyStore(
				binding.root.context ?: return@RVPagingItemCommonListUsage,
				binding.root.findNavController(),
				item.id
			)
		},
		additionalListenersSetups = { _, binding ->
			binding.callButtonConstraintLayout.setOnClickListener {
				val item = binding.root.getTagJson<ResponseWhatsappHistoryItem>() ?: return@setOnClickListener

				val context = binding.root.context ?: return@setOnClickListener

				context.applicationScope?.launch {
					repoShop.interactionForAdCall(item.id.orZero())
				}

				context.launchDialNumber(item.fullAdsPhone)
			}

			binding.whatsAppButtonConstraintLayout.setOnClickListener {
				val item = binding.root.getTagJson<ResponseWhatsappHistoryItem>() ?: return@setOnClickListener

				val context = binding.root.context ?: return@setOnClickListener

				context.applicationScope?.launch {
					repoShop.interactionForAdWhatsApp(item.id.orZero())
				}

				context.launchWhatsApp(item.fullWhatsAppPhone)
			}
		}
	) { binding, _, item ->
		binding.root.setTagJson(item)

		binding.imageView.setupWithGlide {
			load(item.image).saveDiskCacheStrategyAll()
		}

		binding.nameTextView.text = item.name.orEmpty()

		binding.nicknameTextView.text = "( ${item.nickname.orEmpty()} )"

		binding.ratingBar.setProgressBAFloat(item.averageRate.orZero()/* * 20f*/)
		binding.ratingTextView.text = "( ${item.averageRate?.round(1).orZero()
			.toIntIfNoFractionsOrThisFloat().toStringOrEmpty()} )"

		binding.placeTextView.text = "${item.country?.name.orEmpty()} / ${item.city?.name.orEmpty()}"
	}

}
