package ma.ya.cometchatintegration.extensions

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

object GsonUtils {
	const val GSON_KEY = "GSON_KEY"
}

@PublishedApi
internal val gson by lazy {
	GsonBuilder()
		.disableHtmlEscaping()
		.setLenient()
		.serializeNulls()
		// when used below announcement api response couldn't been using toJson ext. fun dunno why ?!
		// .registerTypeAdapter(YearMonth::class.java, TypeAdapterLocalYearMonth)
		//.registerTypeAdapter(LocalDateTime::class.java, TypeAdapterLocalDateTime)
		//.registerTypeAdapter(LocalDate::class.java, TypeAdapterLocalDate)
		//.registerTypeAdapter(LocalTime::class.java, TypeAdapterLocalTime)
		.create()
}

fun Any?.toJsonOrNull(type: Type): String? = kotlin.runCatching {
	gson.toJson(this, type)
}.getOrNull()

inline fun <reified T> T?.toJsonInlinedOrNull(): String? = kotlin.runCatching {
	gson.toJson(this, object : TypeToken<T>() {}.type)
}.getOrNull()

fun <T> String?.fromJsonOrNull(type: Type): T? = kotlin.runCatching {
	gson.fromJson<T>(this, type)
}.getOrNull()

inline fun <reified T> String?.fromJsonInlinedOrNull(): T? = kotlin.runCatching {
	gson.fromJson<T>(this, object : TypeToken<T>() {}.type)
}.getOrNull()
