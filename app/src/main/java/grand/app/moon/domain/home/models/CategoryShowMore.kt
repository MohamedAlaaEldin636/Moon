package grand.app.moon.domain.home.models


import com.google.gson.annotations.SerializedName
import androidx.annotation.Keep

@Keep
data class CategoryShowMore(
  var categoryId: Int? = -1,
  var subCategoryId: Int? = -1,
  var storeId: Int? = -1,
)