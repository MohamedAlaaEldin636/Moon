package grand.app.moon.domain.ads

data class ResponseFilterDetails(
	var properties: List<ItemProperty>?,
)

data class ItemProperty(
	var id: Int?,
	var name: String?,
	var valueString: String?,
	var valueId: Int?,
	var valueBoolean: Boolean?,
	var type: Int?,
    /*
    var is_range: Int?,
    "image": null,
    "text": null,
    "min": 0,
    "max": 0,
     */
    var children: List<ItemProperty>?
)

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
