package grand.app.moon.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

inline fun <X, Y> LiveData<X>.mapToMutableLiveData(crossinline transform: (X) -> Y): MutableLiveData<Y> =
	TransformationsUtils.map(this) { transform(it) }

inline fun <X, Y> LiveData<X>.mapNullableToMutableLiveData(crossinline transform: (X?) -> Y): MutableLiveData<Y> =
	TransformationsUtils.map(this) { transform(it) }

inline fun <X, Y> LiveData<X>.switchMapToLiveData(crossinline transform: (X) -> LiveData<out Y>): LiveData<out Y> =
	TransformationsUtils.switchMap(this) { transform(it) }

inline fun <Y> switchMapMultiple(
	vararg liveData: LiveData<*>,
	crossinline transform: () -> LiveData<Y>
): LiveData<Y> {
	return TransformationsUtils.switchMapMultiple(
		{ transform() },
		*liveData
	)
}

inline fun <Y> switchMapMultiple2(
	vararg liveData: LiveData<*>,
	crossinline transform: () -> Y
): LiveData<Y> = switchMapMultiple(*liveData) {
	MutableLiveData(transform())
}

fun <T> LiveData<T>.ignoreFirstTimeChanged(): LiveData<T> {
	val mediatorLiveData = MediatorLiveData<T>()
	mediatorLiveData.addSource(this, object : Observer<T> {
		var isFirstTime = true

		override fun onChanged(t: T) {
			if (isFirstTime) {
				isFirstTime = false
			}else {
				mediatorLiveData.value = t
			}
		}
	})
	return mediatorLiveData
}
