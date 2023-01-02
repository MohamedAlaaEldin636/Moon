package grand.app.moon.extensions

import android.app.Activity
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

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

private object TypeAdapterLocalYearMonth : JsonSerializer<YearMonth>, JsonDeserializer<YearMonth> {
    override fun serialize(
        src: YearMonth?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return if (src == null) JsonNull.INSTANCE else try {
            JsonPrimitive(src.format(DateTimeFormatter.ofPattern("uuuu-MM")))
        }catch (e: DateTimeException) {
            null
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): YearMonth? {
        return if (json is JsonPrimitive && json.isString) {
            try {
                YearMonth.parse(json.asString)
            }catch (e: DateTimeParseException) {
                null
            }
        }else {
            null
        }
    }
}

private object TypeAdapterLocalDateTime : JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {
    override fun serialize(
        src: LocalDateTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return if (src == null) JsonNull.INSTANCE else try {
            JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
        }catch (e: DateTimeException) {
            null
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDateTime? {
        return if (json is JsonPrimitive && json.isString) {
            try {
                LocalDateTime.parse(json.asString)
            }catch (e: DateTimeParseException) {
                null
            }
        }else {
            null
        }
    }
}

private object TypeAdapterLocalDate : JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {
    override fun serialize(
        src: LocalDate?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return if (src == null) JsonNull.INSTANCE else try {
            JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_DATE))
        }catch (e: DateTimeException) {
            null
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalDate? {
        return if (json is JsonPrimitive && json.isString) {
            try {
                LocalDate.parse(json.asString)
            }catch (e: DateTimeParseException) {
                null
            }
        }else {
            null
        }
    }
}

private object TypeAdapterLocalTime : JsonSerializer<LocalTime>, JsonDeserializer<LocalTime> {
    override fun serialize(
        src: LocalTime?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement? {
        return if (src == null) JsonNull.INSTANCE else try {
            JsonPrimitive(src.format(DateTimeFormatter.ISO_LOCAL_TIME))
        }catch (e: DateTimeException) {
            null
        }
    }

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): LocalTime? {
        return if (json is JsonPrimitive && json.isString) {
            try {
                LocalTime.parse(json.asString)
            }catch (e: DateTimeParseException) {
                null
            }
        }else {
            null
        }
    }
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

fun <T> Fragment.getGsonUsedInStartActivity(type: Type): T? {
    return activity?.intent?.getStringExtra(Params.INTENT_GSON_ANY).let {
        it.fromJsonOrNull(type)
    }
}

inline fun <reified T> Fragment.getGsonUsedInStartActivityInlined(): T? {
    return activity?.intent?.getStringExtra(Params.INTENT_GSON_ANY).let {
        it.fromJsonInlinedOrNull()
    }
}

/*inline fun <reified T> Context.startActivityWithGsonInlined(value: T?, page: String, title: String = "") {
    MovementManager.startActivityWithGson(
        this, page, title, value.toJsonInlinedOrNull() ?: return
    )
}

inline fun <reified T> Context.startActivityWithGsonInlinedClearTop(value: T?, page: String, title: String = "") {
    MovementManager.startActivityWithGsonClearTop(
        this, page, title, value.toJsonInlinedOrNull() ?: return
    )
}

fun Context.startActivityWithNothingAttached(page: String, title: String = "") {
    MovementManager.startActivity(
        this, page, title
    )
}

inline fun <reified T> Context.startActivityFinishAbleWithGsonInlined(value: T?, page: String, title: String = "") {
    startActivityForResultWithGsonInlined(value, page, Params.FINISH_ABLE_ACTIVITY, title)
}

fun Activity.finishFinishAbleActivity() {
    finishActivity(Params.FINISH_ABLE_ACTIVITY)
}

inline fun <reified T> Context.startActivityForResultWithGsonInlined(value: T?, page: String, requestCode: Int, title: String = "") {
    MovementManager.startActivityForResultWithGson(
        this, page, title, value.toJsonInlinedOrNull() ?: return, requestCode
    )
}

inline fun <reified T> Context.getIntentForStartActivityWithGson(value: T?, page: String, title: String = ""): Intent {
    return MovementManager.getIntentForStartActivityWithGson(
        this, page, title, value.toJsonInlinedOrNull().orEmpty()
    )
}

fun <T> Context.startActivityWithGson(value: T?, type: Type, page: String, title: String = "") {
    MovementManager.startActivityWithGson(
        this, page, title, value.toJsonOrNull(type) ?: return
    )
}*/

/** @param key `null` to use set tag without key isa. */
inline fun <reified T> View.setTagJson(value: T, key: Int? = null) {
    value.toJsonInlinedOrNull().also {
        if (key == null) {
            tag = it
        }else {
            setTag(key, it)
        }
    }
}

/** @param key `null` to use set tag without key isa. */
inline fun <reified T> View.getTagJson(key: Int? = null): T? {
    val json = (if (key == null) {
        tag
    }else {
        getTag(key)
    }) as? String

    return json.fromJsonInlinedOrNull()
}

inline fun <reified T> T?.toJsonInBundleInIntentInlined(): Intent {
    return Intent().apply {
        this@toJsonInBundleInIntentInlined.toJsonInlinedOrNull()?.also {
            putExtra(
                Params.INTENT_SPECIAL_BUNDLE_FOR_GSON,
                bundleOf(Params.INTENT_GSON_ANY to it)
            )
        }
    }
}

inline fun <reified T> Intent?.fromJsonInBundleInIntentInlined(): T? {
    return this?.getBundleExtra(Params.INTENT_SPECIAL_BUNDLE_FOR_GSON)
        ?.getString(Params.INTENT_GSON_ANY)?.fromJsonInlinedOrNull()
}

inline fun <reified T> T?.toJsonInBundleInlined(): Bundle {
    return bundleOf(Params.INTENT_GSON_ANY to toJsonInlinedOrNull())
}

inline fun <reified T> Bundle?.fromJsonInBundleInlined(): T? {
    return this?.getString(Params.INTENT_GSON_ANY)?.fromJsonInlinedOrNull()
}

inline fun <reified C : DialogFragment> Fragment.showDialogFragment(args: Bundle = Bundle.EMPTY) {
    val javaClassName = C::class.java.name

    val dialogFragment = kotlin.runCatching {
        Class.forName(javaClassName).newInstance() as DialogFragment
    }.getOrNull() ?: return

    dialogFragment.arguments = args

    dialogFragment.show(childFragmentManager, javaClassName)
}

//getGsonUsedInStartActivityInlined
/*inline fun <reified T> Context.startActivityWithGsonInlined(value: T?, page: String, title: String = ""): Intent {
    return Intent(this, BaseActivity::class.java).apply {
        putExtra(Params.INTENT_PAGE, page)
        putExtra(Params.INTENT_PAGE_TITLE, title)
        putExtra(Params.INTENT_GSON_ANY, value.toJsonInlinedOrNull())
    }
}*/
/*
Intent intent = new Intent(context, BaseActivity.class);
        intent.putExtra(Params.INTENT_PAGE, page);
        intent.putExtra(Params.INTENT_PAGE_TITLE, title);
        intent.putExtra(Params.INTENT_GSON_ANY, json);
 */
