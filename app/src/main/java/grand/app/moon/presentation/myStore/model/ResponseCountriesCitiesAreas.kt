package grand.app.moon.presentation.myStore.model

import com.google.gson.annotations.SerializedName
import grand.app.moon.domain.countries.entity.Country

fun Country?.toResponseCountry(): ResponseCountry {
	return ResponseCountry(
		this?.id,
		this?.name,
		this?.currency,
		this?.countryCode,
		this?.isoCode,
		this?.image,
		this?.cities?.map {
			it.toResponseCity()
		},
		this?.enIsoCode
	)
}

fun Country?.toResponseCity(): ResponseCity {
	return ResponseCity(
		this?.id,
		this?.name,
		this?.currency,
		this?.countryCode,
		this?.isoCode,
		this?.image,
		this?.areas?.map {
			it.toResponseArea()
		},
	)
}

fun Country?.toResponseArea(): ResponseArea {
	return ResponseArea(
		this?.id,
		this?.name,
		this?.currency,
		this?.countryCode,
		this?.isoCode,
		this?.image,
	)
}

data class ResponseCountry(
	var id: Int?,
	var name: String?,
	var currency: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
	var image: String?,
	var cities: List<ResponseCity>?,
	@SerializedName("english_iso_code") var englishCurrencyIsoCode: String?,
)

data class ResponseCity(
	var id: Int?,
	var name: String?,
	var currency: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
	var image: String?,
	var areas: List<ResponseArea>?,
)

data class ResponseArea(
	var id: Int?,
	var name: String?,
	var currency: String?,
	@SerializedName("country_code") var countryCode: String?,
	@SerializedName("iso_code") var isoCode: String?,
	var image: String?,
)
/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": [
        {
            "id": 1,
            "name": "Egypt",
            "currency": "EGP",
            "country_code": "+20",
            "iso_code": "EG",
            "image": "https://sooqmoon.net/storage/countries/1649929860lmSvU.webp",
            "cities": [
                {
                    "id": 7,
                    "name": "cairo",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1656942886qaUpy.webp",
                    "areas": [
                        {
                            "id": 187,
                            "name": "East Nasr City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 188,
                            "name": "Al Salam first",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 191,
                            "name": "Heliopolis",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 192,
                            "name": "elnozha elgdida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 193,
                            "name": "shobra",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 194,
                            "name": "Sheraton",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 195,
                            "name": "hadayiq alquba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 196,
                            "name": "alshuruq",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 197,
                            "name": "Ain Shams",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 198,
                            "name": "Rehab",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 279,
                            "name": "Al-Zaytoun",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 280,
                            "name": "El-Zawya El-Hamraa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 281,
                            "name": "Al Sharbiah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 282,
                            "name": "El Sahel",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 283,
                            "name": "Rod El Farag",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 284,
                            "name": "Al Amiriyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 285,
                            "name": "Al Salam second",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 286,
                            "name": "Al Marj",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 287,
                            "name": "Al Matariyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 288,
                            "name": "West Nasr City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 289,
                            "name": "El Weili",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 290,
                            "name": "Mansheya Nasir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 291,
                            "name": "Downtown Cairo",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 292,
                            "name": "Bab El Sharia",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 293,
                            "name": "Al Azbakeya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 294,
                            "name": "Bulaq",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 295,
                            "name": "Al Muski",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 296,
                            "name": "Abdeen",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 297,
                            "name": "El Mokattam",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 298,
                            "name": "El-Khalifa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 299,
                            "name": "El Sayeda Zeinab",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 300,
                            "name": "Old Cairo",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 301,
                            "name": "Dar Al Salam",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 302,
                            "name": "El Basatin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 303,
                            "name": "Maadi",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 304,
                            "name": "Tura",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 305,
                            "name": "Al Ma'sarah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 306,
                            "name": "15 May City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 307,
                            "name": "Helwan",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 308,
                            "name": "El Tebbin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 309,
                            "name": "New Cairo City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 310,
                            "name": "Badr City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 311,
                            "name": "El Shorouk City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 133,
                    "name": "The Red Sea",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653400056U4NPm.webp",
                    "areas": [
                        {
                            "id": 545,
                            "name": "Hurghada",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 546,
                            "name": "Ras Ghareb",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 134,
                    "name": "Aswan",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653385782NZWRU.webp",
                    "areas": [
                        {
                            "id": 586,
                            "name": "Aswan",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 587,
                            "name": "New Aswan",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 588,
                            "name": "Daraw",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 589,
                            "name": "Kom Ombo",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 590,
                            "name": "nasr alnnwba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 591,
                            "name": "Kalabsha",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 592,
                            "name": "Edfu",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 593,
                            "name": "Rhodesia",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 594,
                            "name": "albasilia",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 595,
                            "name": "As Sibaiyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 596,
                            "name": "Abu Simbel",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 136,
                    "name": "Beheira",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653460702w3PIV.webp",
                    "areas": [
                        {
                            "id": 364,
                            "name": "Damanhour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 365,
                            "name": "Kafr El-Dawar",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 366,
                            "name": "Rasheed",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 367,
                            "name": "Idku",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 368,
                            "name": "Abu Al Matamir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 369,
                            "name": "Abu Hummus",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 370,
                            "name": "Ad Dilinjat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 371,
                            "name": "El-Mahmoudeya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 372,
                            "name": "AR Rahmaniyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 373,
                            "name": "Itay Al Barud",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 374,
                            "name": "Housh Eissa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 375,
                            "name": "Shubrakhit",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 376,
                            "name": "Kawm Hamadah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 377,
                            "name": "Badr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 378,
                            "name": "Natrn Valley",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 379,
                            "name": "New Nubariya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 137,
                    "name": "Sharqia",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653460229d2sGe.webp",
                    "areas": [
                        {
                            "id": 449,
                            "name": "Zagazig",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 450,
                            "name": "10th of Ramadan",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 451,
                            "name": "Minya Al Qamh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 452,
                            "name": "Belbes",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 453,
                            "name": "Mashtul as Suq",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 454,
                            "name": "Al Qanayat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 455,
                            "name": "Abou Hammad",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 456,
                            "name": "Al Qorin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 457,
                            "name": "Hihya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 458,
                            "name": "Abu Kibir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 459,
                            "name": "Faqus",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 460,
                            "name": "As Saleheyah Al Gadidah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 461,
                            "name": "Al Ibrahimeyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 462,
                            "name": "Dyarb Negm",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 463,
                            "name": "Kafr Saqr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 464,
                            "name": "Awlad Saqr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 465,
                            "name": "El-Husseiniya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 466,
                            "name": "San al-Hagar",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 467,
                            "name": "Monshaat Abou Omar",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 138,
                    "name": "Gharbia",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653460626NnmLB.webp",
                    "areas": [
                        {
                            "id": 431,
                            "name": "Tanta",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 432,
                            "name": "El-Mahalla El-Kubra",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 433,
                            "name": "Kafr El-Zayat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 434,
                            "name": "Zifta",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 435,
                            "name": "As Santah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 436,
                            "name": "Qotour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 437,
                            "name": "Basioun",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 438,
                            "name": "Samannoud",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 139,
                    "name": "the new Valley",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653461068kKI3n.webp",
                    "areas": [
                        {
                            "id": 540,
                            "name": "Kharga",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 541,
                            "name": "Baris",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 542,
                            "name": "Mut",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 543,
                            "name": "Farafra",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 544,
                            "name": "Balat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 140,
                    "name": "South of Sinaa",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/16534622070WXZP.webp",
                    "areas": [
                        {
                            "id": 495,
                            "name": "El Tor",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 496,
                            "name": "Sharm El-Shaikh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 497,
                            "name": "dahab",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 498,
                            "name": "Nuweiba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 499,
                            "name": "Taba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 500,
                            "name": "Saint Catherine",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 501,
                            "name": "Abu Redis",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 502,
                            "name": "Abu Zenima",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 503,
                            "name": "Ras Sidr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 141,
                    "name": "North Sinai",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653471986XSROG.webp",
                    "areas": [
                        {
                            "id": 489,
                            "name": "Arish",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 490,
                            "name": "Sheikh Zuweid",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 491,
                            "name": "Rafah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 492,
                            "name": "Bir al-Abd",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 493,
                            "name": "Hasna",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 494,
                            "name": "Nakhl",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 142,
                    "name": "Dakahlia",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653462650XnPV1.webp",
                    "areas": [
                        {
                            "id": 399,
                            "name": "Mansoura",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 400,
                            "name": "Talkha",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 401,
                            "name": "Mit Ghamr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 402,
                            "name": "Dikirnis",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 403,
                            "name": "Aga",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 404,
                            "name": "Menyet El Nasr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 405,
                            "name": "El-Senbellawein",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 406,
                            "name": "El-Kordy",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 407,
                            "name": "Bani Ebeid",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 408,
                            "name": "Al Manzalah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 409,
                            "name": "Timayy Al Imdid",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 410,
                            "name": "El-Gamaleya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 411,
                            "name": "Shirbin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 412,
                            "name": "Al Matariyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 413,
                            "name": "Belkas",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 414,
                            "name": "Mit Salsil",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 415,
                            "name": "Gamasa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 416,
                            "name": "Mahallat Damanah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 417,
                            "name": "Nabaruh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 143,
                    "name": "Qena",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653468376PJ8rN.webp",
                    "areas": [
                        {
                            "id": 567,
                            "name": "Qena",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 568,
                            "name": "New Qena",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 569,
                            "name": "Abu Tesht",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 570,
                            "name": "Nagaa Hammadi",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 571,
                            "name": "Dishna",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 572,
                            "name": "Waqf",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 573,
                            "name": "Qift",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 574,
                            "name": "Naqadah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 575,
                            "name": "Qus",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 576,
                            "name": "Farshut",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 144,
                    "name": "Bani Sweif",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653472286SMcJY.webp",
                    "areas": [
                        {
                            "id": 504,
                            "name": "Bani Sweif",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 505,
                            "name": "New Beni Suef",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 506,
                            "name": "Wasti",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 507,
                            "name": "nasir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 508,
                            "name": "ahnasiana",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 509,
                            "name": "baba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 510,
                            "name": "somsta",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 511,
                            "name": "alfashan",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 145,
                    "name": "Kafr El Sheikh",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/16534686251cgwq.webp",
                    "areas": [
                        {
                            "id": 418,
                            "name": "Kafr El Sheikh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 419,
                            "name": "Desouk",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 420,
                            "name": "Fuwwah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 421,
                            "name": "Mutubas",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 422,
                            "name": "Baltim",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 423,
                            "name": "El-Hamoul",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 424,
                            "name": "Biyala",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 425,
                            "name": "Riyadh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 426,
                            "name": "Sidi Salem",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 427,
                            "name": "Qillin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 428,
                            "name": "Sidi Ghazy",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 429,
                            "name": "Borollos Tower",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 430,
                            "name": "Misir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 146,
                    "name": "Sohag",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653468735AC3fE.webp",
                    "areas": [
                        {
                            "id": 554,
                            "name": "Sohag",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 555,
                            "name": "Sohag Al Gadida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 556,
                            "name": "Akhmim",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 557,
                            "name": "Akhmem Al Gadida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 558,
                            "name": "البلينا",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 559,
                            "name": "Al Maraghah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 560,
                            "name": "Al Minshah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 561,
                            "name": "Dar El Salam",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 562,
                            "name": "Girga",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 563,
                            "name": "Geheinah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 564,
                            "name": "Saqultah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 565,
                            "name": "Tima",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 566,
                            "name": "Tahta",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 147,
                    "name": "Minya",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653468890nhtso.webp",
                    "areas": [
                        {
                            "id": 519,
                            "name": "Minya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 520,
                            "name": "alminya aljadida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 521,
                            "name": "El Idwa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 522,
                            "name": "Maghagha",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 523,
                            "name": "Bani Mazar",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 524,
                            "name": "Matai",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 525,
                            "name": "samalout",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 526,
                            "name": "Al Madinah Al Fikriyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 527,
                            "name": "Mallawi",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 528,
                            "name": "Dayr Mawas",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 148,
                    "name": "Matrouh",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653468937gEbP9.webp",
                    "areas": [
                        {
                            "id": 380,
                            "name": "Marsa Matruh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 381,
                            "name": "El-Hamam",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 382,
                            "name": "El-Alamein",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 383,
                            "name": "El Dabaa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 384,
                            "name": "El Negaila",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 385,
                            "name": "Sidi Barrani",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 386,
                            "name": "El Salloum",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 387,
                            "name": "Siwa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 149,
                    "name": "Damietta",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653469229g8Hjb.webp",
                    "areas": [
                        {
                            "id": 388,
                            "name": "Damietta",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 389,
                            "name": "Damietta El-Gadeeda",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 390,
                            "name": "Ras El-Bar",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 391,
                            "name": "Faraskur",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 392,
                            "name": "Kafr Saad",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 393,
                            "name": "AZ Zarqa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 394,
                            "name": "as Sarou",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 395,
                            "name": "El-Rawda",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 396,
                            "name": "Kafr El Battikh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 397,
                            "name": "Izbat Al Burj",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 398,
                            "name": "Mit Abou Ghaleb",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 150,
                    "name": "Luxor",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653385763QMKp5.webp",
                    "areas": [
                        {
                            "id": 577,
                            "name": "luxor",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 578,
                            "name": "New Luxor",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 579,
                            "name": "New Tiba",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 580,
                            "name": "Zainiya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 581,
                            "name": "Al Bayadeyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 582,
                            "name": "Al Qarnah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 583,
                            "name": "Armant",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 584,
                            "name": "Al Toud",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 585,
                            "name": "Esna",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 151,
                    "name": "Damanhour",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653469315nQspX.webp",
                    "areas": []
                },
                {
                    "id": 152,
                    "name": "Menoufia",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653474597Pma7U.webp",
                    "areas": [
                        {
                            "id": 439,
                            "name": "Shebeen El-Kom",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 440,
                            "name": "El Sadat City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 441,
                            "name": "Minuf",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 442,
                            "name": "Sirs Al Layyanah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 443,
                            "name": "Ashmun",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 444,
                            "name": "El-Bagour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 445,
                            "name": "Quwaysna",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 446,
                            "name": "Birkat as SAB",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 447,
                            "name": "Tala",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 448,
                            "name": "alshuhada'",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 153,
                    "name": "Qalyubia",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653474560p3Ptg.webp",
                    "areas": [
                        {
                            "id": 338,
                            "name": "Banha",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 339,
                            "name": "Qalyub",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 340,
                            "name": "Shubra Al Khaymah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 341,
                            "name": "Al Qanatir Al Khayriyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 342,
                            "name": "Al Khankah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 343,
                            "name": "Kafr Shukr",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 344,
                            "name": "Toukh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 345,
                            "name": "Qaha",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 346,
                            "name": "El Obour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 347,
                            "name": "Al Khusus",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 348,
                            "name": "Shibin Al Qanatir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 154,
                    "name": "Hurghada",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/16534742065NFgL.webp",
                    "areas": [
                        {
                            "id": 547,
                            "name": "Hurghada",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 548,
                            "name": "Ras Ghareb",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 549,
                            "name": "Safaga",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 550,
                            "name": "Quseer",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 551,
                            "name": "Marsa Alam",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 552,
                            "name": "Shalateen",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 553,
                            "name": "Halayeb",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 155,
                    "name": "Fayoum",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653474150pibdT.webp",
                    "areas": [
                        {
                            "id": 512,
                            "name": "Fayoum",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 513,
                            "name": "alfiawm aljadida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 514,
                            "name": "Tamiya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 515,
                            "name": "snores",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 516,
                            "name": "Atsa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 517,
                            "name": "Ibsheway",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 518,
                            "name": "Youssef Al Seddik",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 156,
                    "name": "Asyut",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653474089D0gnq.webp",
                    "areas": [
                        {
                            "id": 529,
                            "name": "Asyut",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 530,
                            "name": "Asyut Al Gadida",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 531,
                            "name": "Dayrout",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 532,
                            "name": "Manfalut",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 533,
                            "name": "Al Qusiyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 534,
                            "name": "Abnub",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 535,
                            "name": "Abu Tij",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 536,
                            "name": "El-Ghanayem",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 537,
                            "name": "Sahel Selim",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 538,
                            "name": "Al Badari",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 539,
                            "name": "Sidfa",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 157,
                    "name": "Ismailia",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653473806skbBx.webp",
                    "areas": [
                        {
                            "id": 477,
                            "name": "Ismailia",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 478,
                            "name": "Fayed",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 479,
                            "name": "El-Qantara el-Sharqîya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 480,
                            "name": "El-Qantara el-Gharbia",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 481,
                            "name": "Tell El Kebir",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 482,
                            "name": "Abu Suwir El Mahata",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 483,
                            "name": "El-Kasasin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 158,
                    "name": "Port Said",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653473719kVDl7.webp",
                    "areas": [
                        {
                            "id": 469,
                            "name": "Sharq District",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 470,
                            "name": "south district",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 471,
                            "name": "West district",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 472,
                            "name": "Arab distirict",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 473,
                            "name": "El Dawahy District",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 474,
                            "name": "Al Zehour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 475,
                            "name": "El Manakh",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 476,
                            "name": "Port Fouad",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 159,
                    "name": "Zagazig",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653473518JUpcK.webp",
                    "areas": []
                },
                {
                    "id": 160,
                    "name": "Tanta",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/16534732964K9IE.webp",
                    "areas": []
                },
                {
                    "id": 161,
                    "name": "Mansoura",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653472862GFBqc.webp",
                    "areas": []
                },
                {
                    "id": 162,
                    "name": "Suez",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653472653qVgBw.webp",
                    "areas": [
                        {
                            "id": 484,
                            "name": "Suez",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 485,
                            "name": "El Arbaeen District",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 486,
                            "name": "Attaka",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 487,
                            "name": "El Ganayen District",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 488,
                            "name": "Faisal",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 163,
                    "name": "Alexandria",
                    "currency": null,
                    "country_code": null,
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653472602tO7gp.webp",
                    "areas": [
                        {
                            "id": 349,
                            "name": "El Montazah 1",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 350,
                            "name": "El Montazah 2",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 351,
                            "name": "El Gomrok",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 352,
                            "name": "El-Agamy",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 353,
                            "name": "Amreya 1",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 354,
                            "name": "Amreya 2",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 355,
                            "name": "Borg Al Arab",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 356,
                            "name": "New Borg El-Arab City",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 357,
                            "name": "Al Ibrahimiyyah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 358,
                            "name": "Al Hadrah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 359,
                            "name": "Azarita",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 360,
                            "name": "Shatby",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 361,
                            "name": "Moharam Bek",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 362,
                            "name": "Karmus",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 363,
                            "name": "Al Attarin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                },
                {
                    "id": 164,
                    "name": "Giza",
                    "currency": null,
                    "country_code": "",
                    "iso_code": null,
                    "image": "https://sooqmoon.net/storage/countries/1653385807I4xVX.webp",
                    "areas": [
                        {
                            "id": 312,
                            "name": "Al Munirah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 313,
                            "name": "Kit Kat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 314,
                            "name": "El Warraq",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 315,
                            "name": "Boulaq Ad Dakrour",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 316,
                            "name": "Mohandessin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 317,
                            "name": "El-Malek Faysal",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 318,
                            "name": "Al Haram",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 319,
                            "name": "Imbabah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 320,
                            "name": "Bashtil",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 321,
                            "name": "Ad Doqi",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 322,
                            "name": "Al Agouzah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 323,
                            "name": "El Omraniya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 324,
                            "name": "Al Haram",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 325,
                            "name": "Giza",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 326,
                            "name": "6 October",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 327,
                            "name": "Sheikh Zayed",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 328,
                            "name": "Al Hawamdiya",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 329,
                            "name": "Badrashin",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 330,
                            "name": "El-Saf",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 331,
                            "name": "Atfih",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 332,
                            "name": "Al Ayat",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 333,
                            "name": "Bawiti",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 334,
                            "name": "Monsha'et El Kanater",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 335,
                            "name": "Ausim",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 336,
                            "name": "Kirdasah",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        },
                        {
                            "id": 337,
                            "name": "Abu an Numros",
                            "currency": null,
                            "country_code": null,
                            "iso_code": null,
                            "image": null
                        }
                    ]
                }
            ]
        },
        {
 */
