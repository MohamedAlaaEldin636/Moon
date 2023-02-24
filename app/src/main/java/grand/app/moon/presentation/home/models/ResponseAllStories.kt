package grand.app.moon.presentation.home.models

import com.google.gson.annotations.SerializedName
import grand.app.moon.helpers.paging.MABasePaging

data class ResponseAllStories(
	@SerializedName("followed_stores_stories") var followedStoresStories: MABasePaging<ResponseStory>?,
	var stories: MABasePaging<ResponseStory>?,
)
/*

 {
                    "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                    "id": 5974,
                    "name": "مالك",
                    "nickname": "OTLOB STORE",
                    "email": "otlob@gmail.com",
                    "phone": "1093835740",
                    "country_code": null,
                    "website": "otlob@gmail.com",
                    "latitude": 24.692005324953,
                    "longitude": 46.718909472656,
                    "token": null,
                    "average_rate": 0,
                    "rate_count": 0,
                    "premium": 0,
                    "advertisements_count": 48,
                    "followers_count": 1,
                    "views_count": 24,
                    "is_following": true,
                    "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                    "background_image": null,
                    "social_media_links": null,
                    "working_hours": [
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:21",
                            "to": "21:21",
                            "status": true
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": true
                        }
                    ],
                    "description": "OTLOB اطلب وانت مطمن",
                    "created_at": "6 months ago",
                    "stories": [
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 16,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197592rCAoE.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 15,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/16571975781ro8f.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 14,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197549MoqvT.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 13,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197531Qr8Bc.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 12,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197506YzSZB.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 11,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197479gwikM.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 10,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197462ne74y.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 9,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197449Z0nc5.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        }
                    ]
                },

{
    "code": 200,
    "message": "The action ran successfully!",
    "data": {
        "followed_stores_stories": {
            "data": [
                {
                    "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                    "id": 5974,
                    "name": "مالك",
                    "nickname": "OTLOB STORE",
                    "email": "otlob@gmail.com",
                    "phone": "1093835740",
                    "country_code": null,
                    "website": "otlob@gmail.com",
                    "latitude": 24.692005324953,
                    "longitude": 46.718909472656,
                    "token": null,
                    "average_rate": 0,
                    "rate_count": 0,
                    "premium": 0,
                    "advertisements_count": 48,
                    "followers_count": 1,
                    "views_count": 24,
                    "is_following": true,
                    "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                    "background_image": null,
                    "social_media_links": null,
                    "working_hours": [
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:20",
                            "to": "21:20",
                            "status": true
                        },
                        {
                            "from": "09:21",
                            "to": "21:21",
                            "status": true
                        },
                        {
                            "from": null,
                            "to": null,
                            "status": true
                        }
                    ],
                    "description": "OTLOB اطلب وانت مطمن",
                    "created_at": "6 months ago",
                    "stories": [
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 16,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197592rCAoE.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 15,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/16571975781ro8f.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 14,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197549MoqvT.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 13,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197531Qr8Bc.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 12,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197506YzSZB.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 11,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197479gwikM.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 10,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197462ne74y.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        },
                        {
                            "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store?story=view",
                            "id": 9,
                            "mime_type": "image/jpeg",
                            "story_link_type": 1,
                            "file": "https://sooqmoon.net/storage/stories/1657197449Z0nc5.webp",
                            "created_at": "6 months ago",
                            "is_liked": false,
                            "is_seen": true,
                            "store": {
                                "share_link": "http://eg.sooqmoon.net/en/shop/5974/otlob-store",
                                "id": 5974,
                                "name": "مالك",
                                "nickname": "OTLOB STORE",
                                "email": "otlob@gmail.com",
                                "phone": "1093835740",
                                "country_code": null,
                                "website": "otlob@gmail.com",
                                "latitude": 24.692005324953,
                                "longitude": 46.718909472656,
                                "token": null,
                                "average_rate": 0,
                                "rate_count": 0,
                                "premium": 0,
                                "advertisements_count": 0,
                                "followers_count": 0,
                                "views_count": 0,
                                "is_following": true,
                                "image": "https://sooqmoon.net/storage/stores/1657126115UQWJr.webp",
                                "background_image": null,
                                "social_media_links": null,
                                "working_hours": [
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:20",
                                        "to": "21:20",
                                        "status": true
                                    },
                                    {
                                        "from": "09:21",
                                        "to": "21:21",
                                        "status": true
                                    },
                                    {
                                        "from": null,
                                        "to": null,
                                        "status": true
                                    }
                                ],
                                "description": "OTLOB اطلب وانت مطمن",
                                "created_at": "6 months ago"
                            }
                        }
                    ]
                },
 */
