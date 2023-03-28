package grand.app.moon.helpers.paging

import com.google.gson.annotations.SerializedName

fun <T, R> MABasePaging<T>.map(transformation: (T) -> R): MABasePaging<R> {
	return MABasePaging(
		data?.map { transformation(it) },
		links, MAPagingMeta(totalNumberOfItems), currentPage, nextPageUrl, firstPageUrl, lastPage, fromInPage, toInPage, totalNumberOfItems, numOfItemsPerPage
	)
}

data class MABasePaging<T>(
		val data: List<T>?,

    val links: MAPagingLinks?,

    val meta: MAPagingMeta?,

    @SerializedName("current_page") val currentPage: Int?,

    @SerializedName("next_page_url") val nextPageUrl: String?,
    /** "first_page_url": "https://samee.my-staff.net/api/v1/orders?page=1" */
    @SerializedName("first_page_url") val firstPageUrl: String?,

    @SerializedName("last_page") val lastPage: Int?,

    /** 1 is the start number ex. index + 1 as doesn't start from 0 */
    @SerializedName("from") val fromInPage: Int?,
    /** 1 */
    @SerializedName("to") val toInPage: Int?,
    /** 1 */
    @SerializedName("total") val totalNumberOfItems: Int?,
    /** 20 */
    @SerializedName("per_page") val numOfItemsPerPage: Int?,
)

data class MAPagingLinks(
	var next: String?,
)

data class MAPagingMeta(
	var total: Int?,
)

/*
"current_page": 1,
            "first_page_url": "https://hassan.my-staff.net/api/v1/categories/1?page=1",
            "from": 1,
            "last_page": 1,
            "last_page_url": "https://hassan.my-staff.net/api/v1/categories/1?page=1",
            "links": [
                {
                    "url": null,
                    "label": "pagination.previous",
                    "active": false
                },
                {
                    "url": "https://hassan.my-staff.net/api/v1/categories/1?page=1",
                    "label": "1",
                    "active": true
                },
                {
                    "url": null,
                    "label": "pagination.next",
                    "active": false
                }
            ],
            "next_page_url": null,
            "path": "https://hassan.my-staff.net/api/v1/categories/1",
            "per_page": 20,
            "prev_page_url": null,
            "to": 2,
            "total": 2
 */
