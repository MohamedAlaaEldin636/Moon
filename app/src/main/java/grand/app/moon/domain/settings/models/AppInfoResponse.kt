package grand.app.moon.domain.settings.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class AppInfoResponse(
    @SerializedName("address")
    val address: List<SettingsData> = listOf(),
    @SerializedName("email")
    val email: List<SettingsData> = listOf(),
    @SerializedName("phone")
    val phone: List<SettingsData> = listOf()
)