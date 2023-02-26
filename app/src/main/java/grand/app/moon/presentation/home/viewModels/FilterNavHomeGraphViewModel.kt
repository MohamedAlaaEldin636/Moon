package grand.app.moon.presentation.home.viewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import grand.app.moon.presentation.home.AllStoresFragment
import grand.app.moon.presentation.home.FilterAllFragment
import javax.inject.Inject

@HiltViewModel
class FilterNavHomeGraphViewModel @Inject constructor(
	application: Application,
) : AndroidViewModel(application) {

	var filter = FilterAllFragment.Filter()

	val filterForStores = MutableLiveData(AllStoresFragment.Filter())

}
