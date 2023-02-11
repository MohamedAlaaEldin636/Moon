package grand.app.moon.extensions

import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.jvm.isAccessible

fun Any.setDeclaredMemberProperty(name: String, value: Any) {
	kotlin.runCatching {
		val kClass = javaClass.kotlin
		val property = kClass.declaredMemberProperties.firstOrNull { it.name == name }
			?: return@runCatching

		val isAccessible = property.isAccessible

		property.isAccessible = true
		if (property is KMutableProperty<*>) {
			property.setter.call(this, 20_000L)
		}

		property.isAccessible = isAccessible
	}
}
