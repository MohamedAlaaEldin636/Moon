package grand.app.moon.domain.shop

import com.google.gson.annotations.SerializedName
import grand.app.moon.helpers.paging.MABasePaging
import grand.app.moon.presentation.base.utils.Constants

data class ResponseExploreInShopInfo(
	@SerializedName("explores_rest_count") var exploresRestCount: Int?,
	var explores: MABasePaging<ItemExploreInShopInfo>
)

data class ItemExploreInShopInfo(
	var id: Int?,
	@SerializedName("mime_type") var mimeType: String?,
	@SerializedName("file") var files: List<String>?,
	@SerializedName("likes_count") var likesCount: Int?,
	@SerializedName("comments_count") var commentsCount: Int?,
	@SerializedName("shares_count") var sharesCount: Int?,
	@SerializedName("is_liked") var isLiked: Boolean?,
	@SerializedName("created_at") var createdAt: String?,
	@SerializedName("explores_rest_count") var exploresRestCount: Int?,
) {
	val isVideo get() = mimeType?.startsWith(Constants.VIDEO) == true
}
/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": {
        "explores_rest_count": 4,
        "explores": {
            "data": [
                {
                    "id": 99,
                    "mime_type": "video/mp4",
                    "file": [
                        "https://sooqmoon.net/storage/explores/16743966666NPga.mp4",
                        "https://sooqmoon.net/storage/explores/16743966671LZ5S.webp"
                    ],
                    "likes_count": 0,
                    "": 0,
                    "": 0,
                    "": false,
                    "": "2023 January 22"
                },
                {
                    "id": 98,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396624whzde.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 97,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396574Fvm3T.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 96,
                    "mime_type": "video/mp4",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674396459Gz57h.mp4"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 95,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674392095tTbs1.webp"
                    ],
                    "likes_count": 0,
                    "comments_count": 0,
                    "shares_count": 0,
                    "is_liked": false,
                    "created_at": "2023 January 22"
                },
                {
                    "id": 94,
                    "mime_type": "image/png",
                    "file": [
                        "https://sooqmoon.net/storage/explores/1674058065qEFoi.webp"
                    ],
                    "likes_count": 1,
                    "comments_count": 2,
                    "shares_count": 1,
                    "is_liked": false,
                    "created_at": "2023 January 18"
                }
            ],
 */
