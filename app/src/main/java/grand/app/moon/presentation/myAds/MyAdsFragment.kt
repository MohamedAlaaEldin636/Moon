package grand.app.moon.presentation.myAds

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import grand.app.moon.R
import grand.app.moon.databinding.FragmentMyAdsBinding
import grand.app.moon.extensions.*
import grand.app.moon.presentation.base.BaseFragment
import grand.app.moon.presentation.myAds.viewModel.MyAdsViewModel

@AndroidEntryPoint
class MyAdsFragment : BaseFragment<FragmentMyAdsBinding>()  {

	private val viewModel by viewModels<MyAdsViewModel>()

	override fun getLayoutId(): Int = R.layout.fragment_my_ads

	override fun setBindingVariables() {
		binding.viewModel = viewModel
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		viewModel.searchNow(this)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)

		binding.recyclerView.setupWithRVItemCommonListUsage(
			viewModel.adapter,
			false,
			1
		)

		observeBackStackEntrySavedStateHandleLiveDataViaGsonNotNull<NewAdvertisementChange> { change ->
			when (change.state) {
				NewAdvertisementState.BECAME_PREMIUM -> {
					viewModel.adapter.list.indexOfFirstOrNull { it.id == change.id }?.also {
						viewModel.adapter.list[it].makePremium()

						viewModel.adapter.notifyItemChanged(it)
					}
				}
				NewAdvertisementState.DELETED -> {
					viewModel.adapter.list.indexOfFirstOrNull { it.id == change.id }?.also {
						viewModel.adapter.deleteAt(it)
					}
				}
			}
		}

		binding.recyclerView.attachSwipingFeature()
	}

	private fun RecyclerView.attachSwipingFeature() {
		val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.DOWN or ItemTouchHelper.UP) {
			override fun onMove(
				recyclerView: RecyclerView,
				viewHolder: RecyclerView.ViewHolder,
				target: RecyclerView.ViewHolder
			): Boolean {
				TODO("Not yet implemented")
			}

			override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
				General.TODO("swiped") // onscroll listener msln isa.
			}
		}

		this.addOnScrollListener(object : RecyclerView.OnScrollListener() {
			override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
				MyLogger.e("aaa -> ch 4 $newState")
				super.onScrollStateChanged(recyclerView, newState)
			}

			override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
				MyLogger.e("aaa -> ch 5 $dy $dx")
				super.onScrolled(recyclerView, dx, dy)
			}
		})
		/*this.setOnGenericMotionListener()
		this.setOnDragListener()*/
		/*this.addOnItemTouchListener(object : RecyclerView.OnItemTouchListener {
			override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
				MyLogger.e("aaa -> chh 1 $e")
				return false
			}

			override fun onTouchEvent(rv: RecyclerView, e: MotionEvent) {
				MyLogger.e("aaa -> chh 2 $e")
			}

			override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
				MyLogger.e("aaa -> chh 3")
			}
		})*/

		ItemTouchHelper(itemTouchHelper).attachToRecyclerView(this)
	}

	data class NewAdvertisementChange(
		var id: Int,
		var state: NewAdvertisementState,
	)

	enum class NewAdvertisementState {
		BECAME_PREMIUM, DELETED
	}

}
