package grand.app.moon.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

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
