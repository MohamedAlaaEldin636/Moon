package grand.app.moon.domain.ads

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.shop.IdAndName
import grand.app.moon.extensions.orZero

fun ResponseFilterDetails.toResponseFilterProperties(isSingleSelectionNotMultiple: Boolean = false): ResponseFilterProperties {
	val list = mutableListOf<DynamicFilterProperty>()

	for (property in properties.orEmpty()) {
		list += when (property.propertyType) {
			ItemProperty.PropertyType.SELECTION -> {
				DynamicFilterProperty.Selection(
					property.id.orZero(),
					property.name.orEmpty(),
					isSingleSelectionNotMultiple,
					property.children.orEmpty().map {
						IdAndName(it.id, it.name)
					}
				)
			}
			ItemProperty.PropertyType.CHECKED -> {
				DynamicFilterProperty.Checked(property.id.orZero(), property.name.orEmpty())
			}
			ItemProperty.PropertyType.TEXT -> if (property.isRange) {
				DynamicFilterProperty.RangedText(property.id.orZero(), property.name.orEmpty(), property.min ?: Float.MIN_VALUE, property.max ?: Float.MAX_VALUE)
			}else {
				DynamicFilterProperty.Text(property.id.orZero(), property.name.orEmpty())
			}
		}
	}

	return ResponseFilterProperties(
		list,
		minPrice ?: Float.MIN_VALUE,
		maxPrice ?: Float.MAX_VALUE
	)
}

data class ResponseFilterProperties(
	var properties: List<DynamicFilterProperty> = emptyList(),
	var minPrice: Float = Float.MIN_VALUE,
	var maxPrice: Float = Float.MAX_VALUE,
)

sealed interface DynamicFilterProperty {
	data class Selection(val id: Int, val title: String, val isSingleSelectionNotMultiple: Boolean, val data: List<IdAndName>, val selectedData: List<IdAndName> = emptyList()) : DynamicFilterProperty
	data class RangedText(val id: Int, val title: String, val min: Float, val max: Float, var from: String = "", var to: String = "") : DynamicFilterProperty
	data class Text(val id: Int, val title: String, var value: String = "") : DynamicFilterProperty
	data class Checked(val id: Int, val title: String, val isSelected: Boolean = false) : DynamicFilterProperty
}

data class ResponseFilterDetails(
	var properties: List<ItemProperty>?,
	@SerializedName("min_price") var minPrice: Float?,
	@SerializedName("max_price") var maxPrice: Float?,
)

data class ItemProperty(
	var id: Int?,
	var name: String?,
	var valueString: String?,
	var valueId: Int?,
	var valueBoolean: Boolean?,
	var type: Int?,
	@SerializedName("is_range") var range: Int?,
	var min: Float?,
	var max: Float?,
  var children: List<ItemProperty>?,
) {
	val isRange get() = range == 1

	val propertyType get() = PropertyType.values().firstOrNull { it.apiValue == type } ?: PropertyType.CHECKED

	enum class PropertyType(val apiValue: Int) {
		SELECTION(1), TEXT(3), CHECKED(2)
	}
}

/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": {
        "properties": [
            {
                "id": 959,
                "name": "النوع",
                "type": 3,
                "is_range": 0,
                "image": null,
                "text": null,
                "min": 0,
                "max": 0,
                "children": []
            },
            {
                "id": 960,
                "name": "الحالة",
                "type": 1,
                "is_range": 0,
                "image": null,
                "text": null,
                "min": 0,
                "max": 0,
                "children": [
                    {
                        "id": 1433,
                        "name": "جديد",
                        "type": null,
                        "is_range": 0,
                        "image": null,
                        "text": null,
                        "min": 0,
                        "max": 0
                    },
                    {
                        "id": 1434,
                        "name": "مستعمل",
                        "type": null,
                        "is_range": 0,
                        "image": null,
                        "text": null,
                        "min": 0,
                        "max": 0
                    }
                ]
            },
 */
