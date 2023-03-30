package grand.app.moon.presentation.stats.viewModels

import android.app.Application
import androidx.core.view.isVisible
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.R
import grand.app.moon.data.shop.RepoShop
import grand.app.moon.databinding.ItemUserInStatsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.stats.StatsUsersHistoryFragmentArgs
import grand.app.moon.presentation.stats.models.ResponseUserInGeneralStats
import javax.inject.Inject

@HiltViewModel
class StatsUsersHistoryViewModel @Inject constructor(
	application: Application,
	val repoShop: RepoShop,
	val args: StatsUsersHistoryFragmentArgs,
) : AndroidViewModel(application) {

	val showWholePageLoader = MutableLiveData(true)

	val users = repoShop.getStatusUsersHistory(
		args.type,
		args.userId,
		if (args.advId >= 0) args.advId else null,
		if (args.storeId == -1) null else args.storeId,
		if (args.exploreOrStoryId == -1) null else args.exploreOrStoryId,
		args.simpleUserListOfInteractionsFragmentTypeAPiValue,
	)

	val adapter = RVPagingItemCommonListUsage<ItemUserInStatsBinding, ResponseUserInGeneralStats>(
		R.layout.item_user_in_stats,
		additionalListenersSetups = { _, binding ->
			binding.whatsappImageView.setOnClickListener { view ->
				val item = (binding.constraintLayout.tag as? String).fromJsonInlinedOrNull<ResponseUserInGeneralStats>()
					?: return@setOnClickListener

				view.context?.launchWhatsApp(item.countryCode.orEmpty() + item.phone.orEmpty())
			}
		}
	) { binding, position, item ->
		binding.constraintLayout.tag = item.toJsonInlinedOrNull()

		val count = item.count
		binding.countTextView.isVisible = false
		//binding.countTextView.text = count.orZero().toString()

		binding.imageImageView.setupWithGlide {
			load(item.image)
				.placeholder(R.drawable.ic_default_user)
		}

		binding.nameTextView.text = item.name
		binding.emailValueTextView.text = item.email
		binding.phoneValueTextView.text = "${item.countryCode} ${item.phone}"

		binding.dateTextView.text = item.createdAt
	}

}
