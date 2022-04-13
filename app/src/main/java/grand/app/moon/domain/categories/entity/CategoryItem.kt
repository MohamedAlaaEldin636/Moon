package grand.app.moon.domain.categories.entity


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryItem(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("image")
    val image: String? = "",
    @SerializedName("name")
    var name: String = "",
    @SerializedName("sub-categories")
    val subCategories: ArrayList<CategoryItem>?,
    var total: Int?,
)