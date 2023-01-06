package grand.app.moon.domain.categories.entity

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

// List<ItemCategory>? -> BaseResponse

sealed interface ItemRelatedToCategories

@Keep
data class ItemCategory(
	var id: Int?,
	var name: String?,
	var image: String?,
	//var ads_count: Int?,
	//var order_by_no: Int?,
	@SerializedName("sub-categories") var subCategories: List<ItemSubCategory>?,
	var brands: List<ItemSubCategory>?,
) : ItemRelatedToCategories

@Keep
data class ItemSubCategory(
	var id: Int?,
	var name: String?,
	var image: String?,
	//var ads_count: Int?,
	//var order_by_no: Int?,
) : ItemRelatedToCategories
/*
{
    "code": 200,
    "message": "The action ran successfully!",
    "data": [
        {
            "id": 2,
            "name": "سيارات ومركبات",
            "image": "https://souqmoon.com/storage/categories/1660634702LfbkU.webp",
            "ads_count": null,
            "order_by_no": 1,
            "sub-categories": [
                {
                    "id": 14,
                    "name": "مرسيدس",
                    "image": "https://souqmoon.com/storage/categories/1652082742N54KO.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 15,
                    "name": "هوندا",
                    "image": "https://souqmoon.com/storage/categories/1652082826dxsNI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 16,
                    "name": "تويوتا",
                    "image": "https://souqmoon.com/storage/categories/1652082865VEkiP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 17,
                    "name": "نيسان",
                    "image": "https://souqmoon.com/storage/categories/16520829024Qovp.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 18,
                    "name": "بي أم دبليو",
                    "image": "https://souqmoon.com/storage/categories/1652082935isrnw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 19,
                    "name": "فيات",
                    "image": "https://souqmoon.com/storage/categories/1652082962V6CDt.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 20,
                    "name": "لاند روفر",
                    "image": "https://souqmoon.com/storage/categories/1652082997jtNo7.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 21,
                    "name": "فولكس فاجن",
                    "image": "https://souqmoon.com/storage/categories/1652083030j0xl5.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 387,
                    "name": "شيري",
                    "image": "https://souqmoon.com/storage/categories/1652099665bN9az.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 388,
                    "name": "سيارات أخرى",
                    "image": "https://souqmoon.com/storage/categories/1652099830l302G.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 410,
                    "name": "BYD",
                    "image": "https://souqmoon.com/storage/categories/1656245831LdRp1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 544,
                    "name": "قطع غيار سيارات",
                    "image": "https://souqmoon.com/storage/categories/1659528891BoZ68.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 545,
                    "name": "هيونداي",
                    "image": "https://souqmoon.com/storage/categories/1659529257Lpd1h.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 546,
                    "name": "شيفروليه",
                    "image": "https://souqmoon.com/storage/categories/1659529343JyTtb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 547,
                    "name": "كيا",
                    "image": "https://souqmoon.com/storage/categories/1659529401A7IeN.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 549,
                    "name": "دايو",
                    "image": "https://souqmoon.com/storage/categories/1659529488R7OrP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 550,
                    "name": "اوبل",
                    "image": "https://souqmoon.com/storage/categories/1659529578HTw7c.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 551,
                    "name": "بيجو",
                    "image": "https://souqmoon.com/storage/categories/1659529669ZvEx1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 552,
                    "name": "ميتسوبيشي",
                    "image": "https://souqmoon.com/storage/categories/1659529778woY27.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 553,
                    "name": "رينو",
                    "image": "https://souqmoon.com/storage/categories/16595298354Y09H.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 555,
                    "name": "سكودا",
                    "image": "https://souqmoon.com/storage/categories/1659529886x80yP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 556,
                    "name": "سوزوكي",
                    "image": "https://souqmoon.com/storage/categories/1659529947oUmRy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 558,
                    "name": "لادا",
                    "image": "https://souqmoon.com/storage/categories/1659530001IlDdh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 561,
                    "name": "جيلي",
                    "image": "https://souqmoon.com/storage/categories/1659530106tmTga.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 562,
                    "name": "ام جي",
                    "image": "https://souqmoon.com/storage/categories/1659530167ipVPm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 563,
                    "name": "جيب",
                    "image": "https://souqmoon.com/storage/categories/1659530228oEcZ8.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 564,
                    "name": "دايهاتسو",
                    "image": "https://souqmoon.com/storage/categories/1659530357jGaS2.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 571,
                    "name": "بريليانس",
                    "image": "https://souqmoon.com/storage/categories/1659532740g5xFD.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 572,
                    "name": "فورد",
                    "image": "https://souqmoon.com/storage/categories/1659532810g5oTd.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 573,
                    "name": "مازدا",
                    "image": "https://souqmoon.com/storage/categories/1659532890pGkCb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 574,
                    "name": "سيات",
                    "image": "https://souqmoon.com/storage/categories/1659532938FVkl6.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 575,
                    "name": "ايسوزو",
                    "image": "https://souqmoon.com/storage/categories/1659533001bA4aq.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 576,
                    "name": "أودي",
                    "image": "https://souqmoon.com/storage/categories/16595331418OlqR.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 577,
                    "name": "فولفو",
                    "image": "https://souqmoon.com/storage/categories/1659533219dV2E0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 578,
                    "name": "جاك",
                    "image": "https://souqmoon.com/storage/categories/1659533276j70fX.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 579,
                    "name": "شانجان",
                    "image": "https://souqmoon.com/storage/categories/16595333390XDKv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 580,
                    "name": "إنفينيتي",
                    "image": "https://souqmoon.com/storage/categories/1659533392BtVOY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 581,
                    "name": "جي إم سي",
                    "image": "https://souqmoon.com/storage/categories/1659533437toGhA.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 582,
                    "name": "بورش",
                    "image": "https://souqmoon.com/storage/categories/1659533486fDqKL.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 583,
                    "name": "سانغ يونغ",
                    "image": "https://souqmoon.com/storage/categories/1659533550Gd4zj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 584,
                    "name": "هافي",
                    "image": "https://souqmoon.com/storage/categories/1659533598KhLmR.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 585,
                    "name": "زوتي",
                    "image": "https://souqmoon.com/storage/categories/1659533642EjmA9.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 586,
                    "name": "دودج",
                    "image": "https://souqmoon.com/storage/categories/1659533685whIiV.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 587,
                    "name": "ستيروين",
                    "image": "https://souqmoon.com/storage/categories/1659533729dfPEU.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 588,
                    "name": "كرايسلر",
                    "image": "https://souqmoon.com/storage/categories/1659533804h4FMH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 589,
                    "name": "بروتون",
                    "image": "https://souqmoon.com/storage/categories/1659533907oryTp.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 591,
                    "name": "فاو",
                    "image": "https://souqmoon.com/storage/categories/1659534049yqcD0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 592,
                    "name": "فوتون",
                    "image": "https://souqmoon.com/storage/categories/16595341378BOSX.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 598,
                    "name": "ماروتي سوزوكي",
                    "image": "https://souqmoon.com/storage/categories/1659534860K9OFv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 599,
                    "name": "بايك",
                    "image": "https://souqmoon.com/storage/categories/1659534919FbtPy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 600,
                    "name": "هافال",
                    "image": "https://souqmoon.com/storage/categories/1659534970zPprx.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 607,
                    "name": "سوبارو",
                    "image": "https://souqmoon.com/storage/categories/1659535483gMjri.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 609,
                    "name": "كاديلاك",
                    "image": "https://souqmoon.com/storage/categories/1659535598ajnR8.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 611,
                    "name": "لينكولن",
                    "image": "https://souqmoon.com/storage/categories/1659535642kDRwF.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 612,
                    "name": "ميني",
                    "image": "https://souqmoon.com/storage/categories/1659535734BtvjT.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 614,
                    "name": "سايبا",
                    "image": "https://souqmoon.com/storage/categories/1659535890YjyRC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 616,
                    "name": "دي اف ام",
                    "image": "https://souqmoon.com/storage/categories/1659536158KOnEj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 617,
                    "name": "فيسكر",
                    "image": "https://souqmoon.com/storage/categories/1659536220jDL27.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 554,
            "name": "دراجات نارية",
            "image": "https://souqmoon.com/storage/categories/1660644330kH0Op.webp",
            "ads_count": null,
            "order_by_no": 2,
            "sub-categories": [
                {
                    "id": 557,
                    "name": "دراجات",
                    "image": "https://souqmoon.com/storage/categories/1659969963dSIJ2.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 565,
                    "name": "خوذ وملابس",
                    "image": "https://souqmoon.com/storage/categories/1659970135WBjdf.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 568,
                    "name": "قطع غيار دراجات نارية",
                    "image": "https://souqmoon.com/storage/categories/1659970309WaFep.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 569,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659970393xfTty.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 651,
            "name": "موبايل - تابلت",
            "image": "https://souqmoon.com/storage/categories/1660646221GcJZ9.webp",
            "ads_count": null,
            "order_by_no": 3,
            "sub-categories": [
                {
                    "id": 654,
                    "name": "موبايل",
                    "image": "https://souqmoon.com/storage/categories/1659952984D7NXj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 655,
                    "name": "تابلت",
                    "image": "https://souqmoon.com/storage/categories/1659953103tapzh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 659,
                    "name": "ساعات ذكية",
                    "image": "https://souqmoon.com/storage/categories/1659953220WtmgY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 663,
                    "name": "سماعات",
                    "image": "https://souqmoon.com/storage/categories/1659953337N8wWe.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 665,
                    "name": "واقي شاشة وكفرات",
                    "image": "https://souqmoon.com/storage/categories/1659953666gsGRr.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 669,
                    "name": "شواحن وكوابل",
                    "image": "https://souqmoon.com/storage/categories/1659954055fjnSJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 680,
                    "name": "قطع غيار موبايل تابلت",
                    "image": "https://souqmoon.com/storage/categories/1659954606IY0sy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 685,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659954822bJQDS.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 728,
            "name": "كمبيوتر ولابتوب",
            "image": "https://souqmoon.com/storage/categories/1660649000aomPA.webp",
            "ads_count": null,
            "order_by_no": 4,
            "sub-categories": [
                {
                    "id": 735,
                    "name": "لابتوب",
                    "image": "https://souqmoon.com/storage/categories/1659958024wZ9d1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 737,
                    "name": "كمبيوتر",
                    "image": "https://souqmoon.com/storage/categories/1659958081kDhHb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 739,
                    "name": "كمبيوتر العاب",
                    "image": "https://souqmoon.com/storage/categories/16599582145984X.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 741,
                    "name": "شاشات",
                    "image": "https://souqmoon.com/storage/categories/16599582906Kznk.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 747,
                    "name": "طابعات واكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/16599585509JzHq.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 753,
                    "name": "قطع كمبيوتر",
                    "image": "https://souqmoon.com/storage/categories/1659958822deACP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 760,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659959097aIM1o.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 764,
                    "name": "اجهزة تخزين",
                    "image": "https://souqmoon.com/storage/categories/1659959186FKT1R.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 768,
                    "name": "مودم - راوتر",
                    "image": "https://souqmoon.com/storage/categories/1659959369g517k.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 770,
                    "name": "سيرفرات",
                    "image": "https://souqmoon.com/storage/categories/1659959447jOVZb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 772,
                    "name": "بروجيكتور",
                    "image": "https://souqmoon.com/storage/categories/1659959551Icjis.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 778,
                    "name": "برامج",
                    "image": "https://souqmoon.com/storage/categories/165995974064Vm0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 781,
                    "name": "اثاث كمبيوتر",
                    "image": "https://souqmoon.com/storage/categories/1659959849WtjBK.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 518,
            "name": "العاب فيديو وملحقاتها",
            "image": "https://souqmoon.com/storage/categories/16590085170XQPI.webp",
            "ads_count": null,
            "order_by_no": 5,
            "sub-categories": [
                {
                    "id": 519,
                    "name": "أجهزة ألعاب",
                    "image": "https://souqmoon.com/storage/categories/1656849911MYUKm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 627,
                    "name": "العاب فيديو",
                    "image": "https://souqmoon.com/storage/categories/1659951114w35cZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 629,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659951363bcflh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 633,
                    "name": "بطاقات شراء",
                    "image": "https://souqmoon.com/storage/categories/1659951573d2fCl.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 636,
                    "name": "بيع حسابات وشخصيات",
                    "image": "https://souqmoon.com/storage/categories/1659951681tfaRe.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 637,
                    "name": "شخصيات اكشن فيجرز",
                    "image": "https://souqmoon.com/storage/categories/16599517922gnkS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 639,
                    "name": "خدمات صيانة",
                    "image": "https://souqmoon.com/storage/categories/16599518609KbJi.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 451,
            "name": "الكترونيات",
            "image": "https://souqmoon.com/storage/categories/1660651980yFB38.webp",
            "ads_count": null,
            "order_by_no": 6,
            "sub-categories": [
                {
                    "id": 452,
                    "name": "تلفزيون - شاشات",
                    "image": "https://souqmoon.com/storage/categories/16568374469aFdo.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 453,
                    "name": "اجهزة المطبخ الصغيرة",
                    "image": "https://souqmoon.com/storage/categories/16599532523zcyF.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 454,
                    "name": "اكسسوارات وقطع غيار",
                    "image": "https://souqmoon.com/storage/categories/1659953329hIJ0E.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 455,
                    "name": "كاميرات - تصوير",
                    "image": "https://souqmoon.com/storage/categories/1659953425VcEaR.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 456,
                    "name": "ثلاجات - فريزر",
                    "image": "https://souqmoon.com/storage/categories/1659953643x2I76.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 666,
                    "name": "غسالات - نشافات",
                    "image": "https://souqmoon.com/storage/categories/1659953707Mz7nC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 668,
                    "name": "جلايات",
                    "image": "https://souqmoon.com/storage/categories/1659954031mAlgp.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 671,
                    "name": "أفران",
                    "image": "https://souqmoon.com/storage/categories/1659954126Cp5rQ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 673,
                    "name": "ميكرويف",
                    "image": "https://souqmoon.com/storage/categories/1659954199fmWAv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 674,
                    "name": "مكيفات",
                    "image": "https://souqmoon.com/storage/categories/1659954281oKdC4.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 676,
                    "name": "ريسيفر",
                    "image": "https://souqmoon.com/storage/categories/1659954412AkyxC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 677,
                    "name": "صوتيات وفيديو",
                    "image": "https://souqmoon.com/storage/categories/1659954483zcZre.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 679,
                    "name": "اجهزة منزلية",
                    "image": "https://souqmoon.com/storage/categories/16599545272tx3S.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 681,
                    "name": "اجهزة عناية شخصية",
                    "image": "https://souqmoon.com/storage/categories/1659954636SQYyP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 682,
                    "name": "دفايات - صوبات",
                    "image": "https://souqmoon.com/storage/categories/1659954740bPHT0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 683,
                    "name": "سخانات - كيزر - بويلر",
                    "image": "https://souqmoon.com/storage/categories/1659954794sfUab.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 686,
                    "name": "مكانس كهربائية",
                    "image": "https://souqmoon.com/storage/categories/1659954846k2PCE.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 687,
                    "name": "كولر - فلاتر ماء",
                    "image": "https://souqmoon.com/storage/categories/1659954902UZsaC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 689,
                    "name": "أنظمة حماية ومراقبة",
                    "image": "https://souqmoon.com/storage/categories/16599549894oQdU.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 690,
                    "name": "أنظمة حماية ومراقبة",
                    "image": "https://souqmoon.com/storage/categories/1659954989z6lDY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 692,
                    "name": "هواتف وفاكس",
                    "image": "https://souqmoon.com/storage/categories/1659955080A4WtU.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 693,
                    "name": "خدمات صيانة",
                    "image": "https://souqmoon.com/storage/categories/1659955134KQmOY.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 485,
            "name": "عقارات للبيع",
            "image": "https://souqmoon.com/storage/categories/1659007619VwQJL.webp",
            "ads_count": null,
            "order_by_no": 7,
            "sub-categories": [
                {
                    "id": 486,
                    "name": "شقق للبيع",
                    "image": "https://souqmoon.com/storage/categories/16599535100CqMv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 487,
                    "name": "عقارات للبيع على الخريطة",
                    "image": "https://souqmoon.com/storage/categories/1656844367MZicj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 488,
                    "name": "بيوت- منازل للبيع",
                    "image": "https://souqmoon.com/storage/categories/16568444022AHLx.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 489,
                    "name": "اراضي للبيع",
                    "image": "https://souqmoon.com/storage/categories/1659969219TYNCP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 624,
                    "name": "فلل وقصور للبيع",
                    "image": "https://souqmoon.com/storage/categories/16599483230jzPF.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 625,
                    "name": "تجاري للبيع",
                    "image": "https://souqmoon.com/storage/categories/1659950777KrFNJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 628,
                    "name": "عمارات للبيع",
                    "image": "https://souqmoon.com/storage/categories/16599513515mpcJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 648,
                    "name": "مزارع وشاليهات للبيع",
                    "image": "https://souqmoon.com/storage/categories/1659952788WXqyw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 657,
                    "name": "شركات للبيع",
                    "image": "https://souqmoon.com/storage/categories/1659953110R9fD6.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 661,
                    "name": "عقارات اجنبيه للبيع",
                    "image": "https://souqmoon.com/storage/categories/1659953306jKBsH.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 745,
            "name": "عقارات للايجار",
            "image": "https://souqmoon.com/storage/categories/1660649099a1z9N.webp",
            "ads_count": null,
            "order_by_no": 8,
            "sub-categories": [
                {
                    "id": 749,
                    "name": "شقق للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659958648fd1HM.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 751,
                    "name": "تجاري للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659958756cHFvM.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 755,
                    "name": "شقق وأجنحة فندقية",
                    "image": "https://souqmoon.com/storage/categories/1659958866YNjIK.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 758,
                    "name": "غرف ومشاركة سكن",
                    "image": "https://souqmoon.com/storage/categories/1659958947qiFUf.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 762,
                    "name": "فلل -قصور للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659959134pelgJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 766,
                    "name": "بيوت ومنازل للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659959221qOyl5.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 769,
                    "name": "عمارات للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659959379PWjBm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 771,
                    "name": "اراضي للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659959479MXYho.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 773,
                    "name": "مزارع وشاليهات للايجار",
                    "image": "https://souqmoon.com/storage/categories/16599595719T81i.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 776,
                    "name": "عقارات اجنبيه للايجار",
                    "image": "https://souqmoon.com/storage/categories/1659959653gXMnZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 246,
            "name": "المنزل والحديقة",
            "image": "https://souqmoon.com/storage/categories/16606381169Hehq.webp",
            "ads_count": null,
            "order_by_no": 9,
            "sub-categories": [
                {
                    "id": 377,
                    "name": "أثاث غرف جلوس",
                    "image": "https://souqmoon.com/storage/categories/1659955820BGEvJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 378,
                    "name": "أثاث غرف نوم",
                    "image": "https://souqmoon.com/storage/categories/1659955884rcEW1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 379,
                    "name": "أثاث غرف سفرة",
                    "image": "https://souqmoon.com/storage/categories/1659955956Z3Q7v.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 380,
                    "name": "أثاث مكتبي",
                    "image": "https://souqmoon.com/storage/categories/1659956031p0Bs2.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 381,
                    "name": "أثاث خارجي",
                    "image": "https://souqmoon.com/storage/categories/1659956136wQpZX.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 382,
                    "name": "اثاث وغرف نوم اطفال",
                    "image": "https://souqmoon.com/storage/categories/1659956192bQ5A6.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 383,
                    "name": "ديكور - اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659956255ta5gc.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 384,
                    "name": "فرشات ومنسوجات",
                    "image": "https://souqmoon.com/storage/categories/1659956351DEbno.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 385,
                    "name": "الحديقة - نباتات",
                    "image": "https://souqmoon.com/storage/categories/1659956436BO7kS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 386,
                    "name": "مطابخ",
                    "image": "https://souqmoon.com/storage/categories/1659956500AR6Og.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 395,
                    "name": "أدوات المطبخ",
                    "image": "https://souqmoon.com/storage/categories/1659956558ZsaVb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 712,
                    "name": "حمامات",
                    "image": "https://souqmoon.com/storage/categories/1659956615MTYSW.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 713,
                    "name": "بلاط - أرضيات - باركيه",
                    "image": "https://souqmoon.com/storage/categories/165995666898FbY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 718,
                    "name": "أبواب - شباببيك - ألمنيوم",
                    "image": "https://souqmoon.com/storage/categories/1659956809N3dok.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 721,
                    "name": "الإضاءة",
                    "image": "https://souqmoon.com/storage/categories/1659956935NF8dm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 722,
                    "name": "سجاد - موكيت",
                    "image": "https://souqmoon.com/storage/categories/1659956975qgQzk.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 725,
                    "name": "ستائر - برادي",
                    "image": "https://souqmoon.com/storage/categories/1659957090SlYoy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 726,
                    "name": "خدمات تنجيد",
                    "image": "https://souqmoon.com/storage/categories/1659957154451pt.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 729,
                    "name": "خدمات حدائق",
                    "image": "https://souqmoon.com/storage/categories/1659957202ydF7l.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 730,
                    "name": "المنزل والحديقة أخري",
                    "image": "https://souqmoon.com/storage/categories/1659957241S2r4A.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 222,
            "name": "ازياء - موضة نسائية",
            "image": "https://souqmoon.com/storage/categories/1660636695zwCyk.webp",
            "ads_count": null,
            "order_by_no": 10,
            "sub-categories": [
                {
                    "id": 223,
                    "name": "ملابس نسائية",
                    "image": "https://souqmoon.com/storage/categories/1659951094nD65v.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 224,
                    "name": "ملابس رجالية",
                    "image": "https://souqmoon.com/storage/categories/1650801876C5tP3.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 225,
                    "name": "أزياء أطفال",
                    "image": "https://souqmoon.com/storage/categories/1650801952CszqD.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 421,
                    "name": "منتجات العناية",
                    "image": "https://souqmoon.com/storage/categories/1656841316k5c8p.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 539,
                    "name": "محفظة",
                    "image": "https://souqmoon.com/storage/categories/16584043060wt4Q.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 634,
                    "name": "احذية نسائية",
                    "image": "https://souqmoon.com/storage/categories/1659951633oVkiH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 638,
                    "name": "ساعات",
                    "image": "https://souqmoon.com/storage/categories/1659951860j8d1M.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 646,
                    "name": "اكسسوارات - مجوهرات",
                    "image": "https://souqmoon.com/storage/categories/1659952585FgvPf.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 647,
                    "name": "ماركات - حقائب - شنط",
                    "image": "https://souqmoon.com/storage/categories/16599527584mwji.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 653,
                    "name": "عطور - بخور",
                    "image": "https://souqmoon.com/storage/categories/1659952917os4Mi.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 658,
                    "name": "مستحضرات تجميل",
                    "image": "https://souqmoon.com/storage/categories/16599531209f6rn.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 703,
            "name": "ازياء - موضة رجالي",
            "image": "https://souqmoon.com/storage/categories/1660648819B1R9b.webp",
            "ads_count": null,
            "order_by_no": 11,
            "sub-categories": [
                {
                    "id": 706,
                    "name": "ملابس رجالي",
                    "image": "https://souqmoon.com/storage/categories/1659956249DX0xH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 708,
                    "name": "احذية رجالي",
                    "image": "https://souqmoon.com/storage/categories/1659956341VqNyv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 709,
                    "name": "ساعات رجالي",
                    "image": "https://souqmoon.com/storage/categories/1659956452DzcGR.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 711,
                    "name": "اكسسوارات رجالي",
                    "image": "https://souqmoon.com/storage/categories/16599565667Wlhp.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 715,
                    "name": "عطور - بخور رجالي",
                    "image": "https://souqmoon.com/storage/categories/16599566910Sjet.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 716,
                    "name": "عناية شخصية رجالية",
                    "image": "https://souqmoon.com/storage/categories/1659956786qVF3Q.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 793,
            "name": "لوازم الأطفال والألعاب",
            "image": "https://souqmoon.com/storage/categories/1660649430a6f7t.webp",
            "ads_count": null,
            "order_by_no": 12,
            "sub-categories": [
                {
                    "id": 795,
                    "name": "اثاث وغرف نوم اطفال",
                    "image": "https://souqmoon.com/storage/categories/16599607166oatS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 798,
                    "name": "مستلزمات اطفال",
                    "image": "https://souqmoon.com/storage/categories/1659960852afw8X.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 799,
                    "name": "ملابس واحذيه اطفال",
                    "image": "https://souqmoon.com/storage/categories/1659960946zdQO9.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 802,
                    "name": "العاب اطفال",
                    "image": "https://souqmoon.com/storage/categories/165996103394rIY.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 154,
            "name": "طعام وغذاء",
            "image": "https://souqmoon.com/storage/categories/16606354538ckrq.webp",
            "ads_count": null,
            "order_by_no": 13,
            "sub-categories": [
                {
                    "id": 155,
                    "name": "المخابز",
                    "image": "https://souqmoon.com/storage/categories/1652083481smD60.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 157,
                    "name": "عسل",
                    "image": "https://souqmoon.com/storage/categories/1659969264cmjxA.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 161,
                    "name": "الحلويات",
                    "image": "https://souqmoon.com/storage/categories/1659969332Mpx5Z.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 626,
                    "name": "زيوت",
                    "image": "https://souqmoon.com/storage/categories/1659950992G5icp.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 630,
                    "name": "تمور",
                    "image": "https://souqmoon.com/storage/categories/1659969397iJkp7.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 635,
                    "name": "طبخات جاهزة",
                    "image": "https://souqmoon.com/storage/categories/1659969444J2ZTl.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 641,
                    "name": "أجبان",
                    "image": "https://souqmoon.com/storage/categories/1659952042TpQgi.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 642,
                    "name": "فيتامينات ومكملات غذائية",
                    "image": "https://souqmoon.com/storage/categories/16599522262iY0O.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 643,
                    "name": "خضروات وفاكهة",
                    "image": "https://souqmoon.com/storage/categories/1659952308wZWqa.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 644,
                    "name": "لحوم",
                    "image": "https://souqmoon.com/storage/categories/1659952378iq08D.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 645,
                    "name": "طعام - غذاء - أخرى",
                    "image": "https://souqmoon.com/storage/categories/16599525194L2BD.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 535,
            "name": "التعليم والتدريب",
            "image": "https://souqmoon.com/storage/categories/1660650221ydn2F.webp",
            "ads_count": null,
            "order_by_no": 14,
            "sub-categories": [
                {
                    "id": 536,
                    "name": "كورسات اونلاين",
                    "image": "https://souqmoon.com/storage/categories/1657011926KlBcS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 660,
                    "name": "دورات تدريبية",
                    "image": "https://souqmoon.com/storage/categories/1659953276LNOSM.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 664,
                    "name": "دروس خصوصية",
                    "image": "https://souqmoon.com/storage/categories/1659953358Y1pEV.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 667,
                    "name": "أخرى",
                    "image": "https://souqmoon.com/storage/categories/1659953856C8RHs.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 490,
            "name": "الخدمات",
            "image": "https://souqmoon.com/storage/categories/1659007688CEsjy.webp",
            "ads_count": null,
            "order_by_no": 15,
            "sub-categories": [
                {
                    "id": 491,
                    "name": "خدمات تنظيف",
                    "image": "https://souqmoon.com/storage/categories/1659969561XsCEh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 492,
                    "name": "نقل عفش",
                    "image": "https://souqmoon.com/storage/categories/16599697161dNL0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 493,
                    "name": "خدمات الصيانة",
                    "image": "https://souqmoon.com/storage/categories/1656847821kPZ8R.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 672,
                    "name": "خدمات صيانة موبايل وتابلت",
                    "image": "https://souqmoon.com/storage/categories/1659954187G27oB.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 675,
                    "name": "خدمات صيانة كمبيوتر",
                    "image": "https://souqmoon.com/storage/categories/1659954404BLh2O.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 678,
                    "name": "خدمات صيانة العاب فيديو",
                    "image": "https://souqmoon.com/storage/categories/165995452568mtF.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 684,
                    "name": "خدمات صيانة أجهزة كهربائية",
                    "image": "https://souqmoon.com/storage/categories/1659954799K1APa.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 688,
                    "name": "خدمات كهربائية - كهربجي",
                    "image": "https://souqmoon.com/storage/categories/1659954906jU40e.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 691,
                    "name": "حجز تذاكر طيران - سياحة",
                    "image": "https://souqmoon.com/storage/categories/1659955065iaDdb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 694,
                    "name": "خدمات طبية",
                    "image": "https://souqmoon.com/storage/categories/1659955197BQO3z.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 697,
                    "name": "خدمات مناسبات",
                    "image": "https://souqmoon.com/storage/categories/1659955318n9e2v.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 699,
                    "name": "صيانة وإصلاح السيارات",
                    "image": "https://souqmoon.com/storage/categories/1659955464WA2vg.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 701,
                    "name": "خدمات ونش وسطحات",
                    "image": "https://souqmoon.com/storage/categories/1659955723B8i0w.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 707,
                    "name": "بناء و مقاولات",
                    "image": "https://souqmoon.com/storage/categories/1659956340RoYpP.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 710,
                    "name": "خدمات قانونية",
                    "image": "https://souqmoon.com/storage/categories/1659956487F4sPJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 714,
                    "name": "خدمات نقل المياه",
                    "image": "https://souqmoon.com/storage/categories/1659956678SHlzD.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 719,
                    "name": "خدمات نقل المحروقات",
                    "image": "https://souqmoon.com/storage/categories/1659956814RlD6s.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 720,
                    "name": "خدمات توصيل ودليفيري",
                    "image": "https://souqmoon.com/storage/categories/16599569241YqPz.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 724,
                    "name": "خدمات دهان - أصباغ",
                    "image": "https://souqmoon.com/storage/categories/1659957043v8Cmq.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 727,
                    "name": "خدمات سباكة - مواسرجي",
                    "image": "https://souqmoon.com/storage/categories/1659957165Rw14i.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 731,
                    "name": "خدمات حدادة - لحام",
                    "image": "https://souqmoon.com/storage/categories/1659957512Z6hVi.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 732,
                    "name": "خدمات تنجيد",
                    "image": "https://souqmoon.com/storage/categories/1659957735BvZsf.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 733,
                    "name": "خدمات بلاط - باركيه",
                    "image": "https://souqmoon.com/storage/categories/1659957957BITzh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 736,
                    "name": "خدمات نجارة وصيانة أثاث",
                    "image": "https://souqmoon.com/storage/categories/165995806481YVI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 738,
                    "name": "تركيب وصيانة مطابخ",
                    "image": "https://souqmoon.com/storage/categories/1659958134rE9AY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 740,
                    "name": "تركيب وصيانة حمامات",
                    "image": "https://souqmoon.com/storage/categories/1659958223xQzLs.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 742,
                    "name": "خدمات حدائق",
                    "image": "https://souqmoon.com/storage/categories/1659958331Ls4h0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 744,
                    "name": "صيانة أبواب - شبابيك",
                    "image": "https://souqmoon.com/storage/categories/1659958439A0kgv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 746,
                    "name": "خدمات تصميم داخلي",
                    "image": "https://souqmoon.com/storage/categories/1659958515a7UbX.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 748,
                    "name": "خدمات صيانة عامة",
                    "image": "https://souqmoon.com/storage/categories/165995863414wvS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 752,
                    "name": "دعاية وتسويق",
                    "image": "https://souqmoon.com/storage/categories/1659958764fN6K2.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 754,
                    "name": "خدمات طباعة وتصميم",
                    "image": "https://souqmoon.com/storage/categories/1659958851i2KDz.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 757,
                    "name": "خدمات محاسبة ومالية",
                    "image": "https://souqmoon.com/storage/categories/1659958909EBl4T.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 759,
                    "name": "خدمات تجميل",
                    "image": "https://souqmoon.com/storage/categories/1659959019Ll5sh.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 761,
                    "name": "خدمات رعاية منزلية",
                    "image": "https://souqmoon.com/storage/categories/1659959125TD5jJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 765,
                    "name": "خدمات الأعمال",
                    "image": "https://souqmoon.com/storage/categories/1659959203OTVzI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 767,
                    "name": "خدمات أخرى",
                    "image": "https://souqmoon.com/storage/categories/1659959344oJV7m.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 498,
            "name": "الوظائف",
            "image": "https://souqmoon.com/storage/categories/1660644251iFN4K.webp",
            "ads_count": null,
            "order_by_no": 16,
            "sub-categories": [
                {
                    "id": 499,
                    "name": "وظائف أمن وحراسة",
                    "image": "https://souqmoon.com/storage/categories/1659954376iP9Ja.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 790,
                    "name": "صناعة وتجزئة",
                    "image": "https://souqmoon.com/storage/categories/1659960363GlzCW.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 791,
                    "name": "نظافة",
                    "image": "https://souqmoon.com/storage/categories/1659960445d4l52.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 794,
                    "name": "تعليم",
                    "image": "https://souqmoon.com/storage/categories/1659960621DRO36.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 796,
                    "name": "موارد بشرية",
                    "image": "https://souqmoon.com/storage/categories/1659960764S3rA1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 797,
                    "name": "زراعة",
                    "image": "https://souqmoon.com/storage/categories/1659960895MVY5I.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 800,
                    "name": "خدمة عملاء",
                    "image": "https://souqmoon.com/storage/categories/1659960952rZi5S.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 803,
                    "name": "مبيعات",
                    "image": "https://souqmoon.com/storage/categories/1659961108hb8ia.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 804,
                    "name": "هندسة",
                    "image": "https://souqmoon.com/storage/categories/1659961240OT8Ew.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 805,
                    "name": "صحة وجمال",
                    "image": "https://souqmoon.com/storage/categories/16599614777D24L.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 806,
                    "name": "تكنولوجيا المعلومات",
                    "image": "https://souqmoon.com/storage/categories/1659961559NpIo4.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 807,
                    "name": "علوم ورعاية صحية",
                    "image": "https://souqmoon.com/storage/categories/1659961669EU6ZB.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 808,
                    "name": "فنيين وحرفيين",
                    "image": "https://souqmoon.com/storage/categories/1659961822SKbcE.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 809,
                    "name": "إدارة وسكرتارية",
                    "image": "https://souqmoon.com/storage/categories/1659961941ULFGH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 810,
                    "name": "إنتاج وإعلام",
                    "image": "https://souqmoon.com/storage/categories/1659962033OBhkJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 811,
                    "name": "طب",
                    "image": "https://souqmoon.com/storage/categories/1659962157cEwTz.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 812,
                    "name": "فندقة ومطاعم",
                    "image": "https://souqmoon.com/storage/categories/1659962376Yc4Ta.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 813,
                    "name": "سائقين وتوصيل",
                    "image": "https://souqmoon.com/storage/categories/1659962529NBJsZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 814,
                    "name": "سياحة وسفر",
                    "image": "https://souqmoon.com/storage/categories/1659962629zfU4W.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 815,
                    "name": "قانون ومحاماة",
                    "image": "https://souqmoon.com/storage/categories/1659962779Uw5ac.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 816,
                    "name": "تأمين",
                    "image": "https://souqmoon.com/storage/categories/1659962893nDLJa.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 817,
                    "name": "تسويق",
                    "image": "https://souqmoon.com/storage/categories/1659963055aizdj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 818,
                    "name": "مالية ومحاسبة",
                    "image": "https://souqmoon.com/storage/categories/1659963461QZJcx.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 819,
                    "name": "تصميم",
                    "image": "https://souqmoon.com/storage/categories/1659963532nbcgC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 820,
                    "name": "كتابة وترجمة",
                    "image": "https://souqmoon.com/storage/categories/1659963655JONBn.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 821,
                    "name": "سيارات وميكانيك",
                    "image": "https://souqmoon.com/storage/categories/1659963750b5xqw.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 244,
            "name": "حيوانات للبيع",
            "image": "https://souqmoon.com/storage/categories/1660637299D0zEC.webp",
            "ads_count": null,
            "order_by_no": 17,
            "sub-categories": [
                {
                    "id": 335,
                    "name": "اغذية الحيوانات",
                    "image": "https://souqmoon.com/storage/categories/1659959619vNaWC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 337,
                    "name": "طيور",
                    "image": "https://souqmoon.com/storage/categories/1659959490FDzrb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 338,
                    "name": "اسماك",
                    "image": "https://souqmoon.com/storage/categories/1659959508BTxAJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 339,
                    "name": "مستلزمات",
                    "image": "https://souqmoon.com/storage/categories/16599595253ncKm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 543,
                    "name": "كلاب",
                    "image": "https://souqmoon.com/storage/categories/1659969530cS4RZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 560,
                    "name": "قطط",
                    "image": "https://souqmoon.com/storage/categories/1659969552dMPxJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 777,
                    "name": "ببغاء",
                    "image": "https://souqmoon.com/storage/categories/1659959675A96Bx.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 779,
                    "name": "حمام",
                    "image": "https://souqmoon.com/storage/categories/1659959764qVo8f.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 780,
                    "name": "غنم",
                    "image": "https://souqmoon.com/storage/categories/1659959793hkAMj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 782,
                    "name": "دجاج",
                    "image": "https://souqmoon.com/storage/categories/1659959855BUHCs.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 783,
                    "name": "خيل",
                    "image": "https://souqmoon.com/storage/categories/1659959889zMPRl.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 784,
                    "name": "بقر",
                    "image": "https://souqmoon.com/storage/categories/1659959922YLHdw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 785,
                    "name": "سلاحف",
                    "image": "https://souqmoon.com/storage/categories/16599599592DQNt.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 786,
                    "name": "غنم",
                    "image": "https://souqmoon.com/storage/categories/1659959986KnvqH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 787,
                    "name": "ارانب",
                    "image": "https://souqmoon.com/storage/categories/1659960023OA9Wh.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 514,
            "name": "كتب - رياضة - قرطاسية",
            "image": "https://souqmoon.com/storage/categories/1659008280EYUxz.webp",
            "ads_count": null,
            "order_by_no": 18,
            "sub-categories": [
                {
                    "id": 515,
                    "name": "كتب - مجلات",
                    "image": "https://souqmoon.com/storage/categories/1659956480LQdPr.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 717,
                    "name": "تذاكر",
                    "image": "https://souqmoon.com/storage/categories/1659956804ReOwZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 723,
                    "name": "آلات موسيقية",
                    "image": "https://souqmoon.com/storage/categories/1659957017AvMyw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 734,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1659957964rAo2L.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 743,
                    "name": "دراجات هوائية",
                    "image": "https://souqmoon.com/storage/categories/1659958392spS2Y.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 756,
                    "name": "لوازم رحلات وتخييم",
                    "image": "https://souqmoon.com/storage/categories/1659958868JxvLV.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 763,
                    "name": "رياضات - هوايات",
                    "image": "https://souqmoon.com/storage/categories/1659959157tS2BG.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 774,
                    "name": "لوازم مكتبية - قرطاسية",
                    "image": "https://souqmoon.com/storage/categories/16599596384Tlvo.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 788,
                    "name": "نوادر",
                    "image": "https://souqmoon.com/storage/categories/1659960083pUHlo.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 695,
            "name": "شركات ومعدات مهنية",
            "image": "https://souqmoon.com/storage/categories/1660646554HCFoB.webp",
            "ads_count": null,
            "order_by_no": 19,
            "sub-categories": [
                {
                    "id": 698,
                    "name": "معدات طبية ومهنية",
                    "image": "https://souqmoon.com/storage/categories/1659955379a6CRT.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 702,
                    "name": "شركات للبيع",
                    "image": "https://souqmoon.com/storage/categories/16599559143nXyq.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 705,
                    "name": "شركات أخرى",
                    "image": "https://souqmoon.com/storage/categories/1659956118JWmTp.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 481,
            "name": "مستلزمات شخصية",
            "image": "https://souqmoon.com/storage/categories/1660643512oUahT.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 482,
                    "name": "ساعات",
                    "image": "https://souqmoon.com/storage/categories/1656843965NvbmQ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 483,
                    "name": "عطور",
                    "image": "https://souqmoon.com/storage/categories/1656843998sio9M.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 484,
                    "name": "نظارات",
                    "image": "https://souqmoon.com/storage/categories/1656844026lYaJv.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 29,
            "name": "الهدايا",
            "image": "https://souqmoon.com/storage/categories/1660635019vSOn0.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 216,
                    "name": "الزهور",
                    "image": "https://souqmoon.com/storage/categories/16598906264cg8O.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 217,
                    "name": "اكسسوارات",
                    "image": "https://souqmoon.com/storage/categories/1652693904EDQKw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 402,
                    "name": "تحف",
                    "image": "https://souqmoon.com/storage/categories/16526937761w2MA.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 833,
                    "name": "هاند ميد",
                    "image": "https://souqmoon.com/storage/categories/1661076123VZvw2.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 190,
            "name": "الكافيهات",
            "image": "https://souqmoon.com/storage/categories/1660635803gICxJ.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 209,
                    "name": "فطور",
                    "image": "https://souqmoon.com/storage/categories/1652100309Q9CmD.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 210,
                    "name": "حلويات",
                    "image": "https://souqmoon.com/storage/categories/1652100412qZaL0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 211,
                    "name": "قهوة باردة",
                    "image": "https://souqmoon.com/storage/categories/1652100484aXfbk.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 212,
                    "name": "قهوة حارة",
                    "image": "https://souqmoon.com/storage/categories/16521005685qRv4.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 213,
                    "name": "المشروبات",
                    "image": "https://souqmoon.com/storage/categories/1652100632oabpr.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 191,
            "name": "سوبر ماركت",
            "image": "https://souqmoon.com/storage/categories/1660636459tHa1n.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 393,
                    "name": "مراكز تجارية",
                    "image": "https://souqmoon.com/storage/categories/1651014156o98i0.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 394,
                    "name": "اغذية",
                    "image": "https://souqmoon.com/storage/categories/1651014181fDeLG.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 243,
            "name": "الزراعة",
            "image": "https://souqmoon.com/storage/categories/1660636836DPf7z.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 330,
                    "name": "الأعلاف",
                    "image": "https://souqmoon.com/storage/categories/1650929344Ujr5G.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 331,
                    "name": "البذور",
                    "image": "https://souqmoon.com/storage/categories/1650929373OU2Js.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 332,
                    "name": "حبوب",
                    "image": "https://souqmoon.com/storage/categories/1650929400qRSmH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 333,
                    "name": "منتجات زراعية",
                    "image": "https://souqmoon.com/storage/categories/1650929476Le2bX.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 531,
            "name": "الاثاث",
            "image": "https://souqmoon.com/storage/categories/1659008805Gb0t8.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 532,
                    "name": "اثاث  مكتبي",
                    "image": "https://souqmoon.com/storage/categories/1657011088HRZJM.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 533,
                    "name": "غرف اطفال",
                    "image": "https://souqmoon.com/storage/categories/1657011130mgOAJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 534,
                    "name": "غرف نوم",
                    "image": "https://souqmoon.com/storage/categories/16570111658ilAI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 593,
                    "name": "اثاث غرف جلوس",
                    "image": "https://souqmoon.com/storage/categories/1659534407Bv7bx.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 594,
                    "name": "غرف سفرة",
                    "image": "https://souqmoon.com/storage/categories/1659534518agmP3.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 596,
                    "name": "اثاث خارجي",
                    "image": "https://souqmoon.com/storage/categories/16595347001WSak.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 601,
                    "name": "ديكور",
                    "image": "https://souqmoon.com/storage/categories/1659534974Zf3X8.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 247,
            "name": "الطاقة",
            "image": "https://souqmoon.com/storage/categories/1660638316HT8aK.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 344,
                    "name": "الغاز الحيوي",
                    "image": "https://souqmoon.com/storage/categories/16509298710GU2q.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 345,
                    "name": "الغاز الطبيعي",
                    "image": "https://souqmoon.com/storage/categories/1650929900Ii5oa.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 346,
                    "name": "منتجات الطاقة",
                    "image": "https://souqmoon.com/storage/categories/1650929923hginy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 347,
                    "name": "الفحم",
                    "image": "https://souqmoon.com/storage/categories/165092994769Xi1.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 348,
                    "name": "وقود",
                    "image": "https://souqmoon.com/storage/categories/16509299716Y8Iy.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 349,
                    "name": "النفط الخام",
                    "image": "https://souqmoon.com/storage/categories/1650929996ZzkMV.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 494,
            "name": "الرياضة",
            "image": "https://souqmoon.com/storage/categories/1659007834qEvZw.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 495,
                    "name": "معدات رياضية",
                    "image": "https://souqmoon.com/storage/categories/1656848194BZ5et.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 496,
                    "name": "مكملات غذائية",
                    "image": "https://souqmoon.com/storage/categories/1656848215dVARg.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 497,
                    "name": "تجهيز المراكز والصالات الرياضية",
                    "image": "https://souqmoon.com/storage/categories/1656848236azj6o.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 248,
            "name": "البيئة",
            "image": "https://souqmoon.com/storage/categories/1660639665eFOaf.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 350,
                    "name": "اعادة تدوير",
                    "image": "https://souqmoon.com/storage/categories/1650930034jRXwz.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 351,
                    "name": "العرف الصحي",
                    "image": "https://souqmoon.com/storage/categories/1650930061U27pK.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 352,
                    "name": "المنتجات البيئية",
                    "image": "https://souqmoon.com/storage/categories/1650930091SMgGI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 353,
                    "name": "معالجة المياه",
                    "image": "https://souqmoon.com/storage/categories/16509301156KTN8.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 258,
            "name": "السفر والسياحة",
            "image": "https://souqmoon.com/storage/categories/1660640055wx23V.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 275,
                    "name": "الخيام والرحلات",
                    "image": "https://souqmoon.com/storage/categories/1652102787PE0xr.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 398,
                    "name": "الفنادق",
                    "image": "https://souqmoon.com/storage/categories/165216845809XzZ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 399,
                    "name": "استراحات وشاليهات",
                    "image": "https://souqmoon.com/storage/categories/1652168564C8mrA.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 479,
            "name": "المصممين والمصورين",
            "image": "https://souqmoon.com/storage/categories/1659007500e7H46.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 480,
                    "name": "المصممين والمصورين",
                    "image": "https://souqmoon.com/storage/categories/1656843635QUa3m.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 477,
            "name": "الموسيقي",
            "image": "https://souqmoon.com/storage/categories/1659006751yb8Mo.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 478,
                    "name": "موسيقي",
                    "image": "https://souqmoon.com/storage/categories/16568433465ovVn.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 473,
            "name": "المطاعم",
            "image": "https://souqmoon.com/storage/categories/1657204719o2aer.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 474,
                    "name": "مطاعم مصرية",
                    "image": "https://souqmoon.com/storage/categories/1659536147OQ2UD.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 615,
                    "name": "مطاعم صيني",
                    "image": "https://souqmoon.com/storage/categories/1659536035PVG6M.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 618,
                    "name": "مطاعم يابانية",
                    "image": "https://souqmoon.com/storage/categories/1659536237mUk0j.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 619,
                    "name": "مطاعم سورية",
                    "image": "https://souqmoon.com/storage/categories/1659536598W09qb.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 620,
                    "name": "مطاعم مكسيكية",
                    "image": "https://souqmoon.com/storage/categories/1659537034e3icY.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 621,
                    "name": "مطاعم تايلاندي",
                    "image": "https://souqmoon.com/storage/categories/1659537092gBkUt.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 622,
                    "name": "مطاعم عربية",
                    "image": "https://souqmoon.com/storage/categories/1659601761va0Vk.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 623,
                    "name": "مطاعم خليجية",
                    "image": "https://souqmoon.com/storage/categories/1659601869aIWFV.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 467,
            "name": "الجمال والصحة",
            "image": "https://souqmoon.com/storage/categories/1660642441jaG8m.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 468,
                    "name": "منتجات تجميل",
                    "image": "https://souqmoon.com/storage/categories/1659531014pKTfA.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 469,
                    "name": "مراكز تجميل",
                    "image": "https://souqmoon.com/storage/categories/1659530889Q4ZDU.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 470,
                    "name": "عيادة تجميل",
                    "image": "https://souqmoon.com/storage/categories/16595311494L1B8.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 471,
                    "name": "زراعة شعر",
                    "image": "https://souqmoon.com/storage/categories/1659531236nGHZi.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 472,
                    "name": "عياده اسنان",
                    "image": "https://souqmoon.com/storage/categories/1659531374G1wSk.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 461,
            "name": "قطع غيار",
            "image": "https://souqmoon.com/storage/categories/1659006262zbTBZ.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 463,
                    "name": "سيارات",
                    "image": "https://souqmoon.com/storage/categories/16568388263809e.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 464,
                    "name": "الكترونيات",
                    "image": "https://souqmoon.com/storage/categories/1656838846zh8s3.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 465,
                    "name": "اجهزة كهربائية",
                    "image": "https://souqmoon.com/storage/categories/1656838879ySl0f.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 256,
            "name": "خدمات عامة",
            "image": "https://souqmoon.com/storage/categories/1660639794DwZv9.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 406,
                    "name": "مربية اطفال",
                    "image": "https://souqmoon.com/storage/categories/1656244036aM59I.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 407,
                    "name": "شركات نظافة",
                    "image": "https://souqmoon.com/storage/categories/16562443238KZmg.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 408,
                    "name": "معلمة خاصة",
                    "image": "https://souqmoon.com/storage/categories/1656244515zDsEi.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 409,
                    "name": "خادمات",
                    "image": "https://souqmoon.com/storage/categories/1656244586qQyeF.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 433,
            "name": "المعدات",
            "image": "https://souqmoon.com/storage/categories/1660651585tSE0X.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 438,
                    "name": "معدات خرسانة",
                    "image": "https://souqmoon.com/storage/categories/1656588854D5yFC.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 825,
                    "name": "قطع غيار",
                    "image": "https://souqmoon.com/storage/categories/1660469375OyfGI.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 826,
                    "name": "الرافعات الشوكية",
                    "image": "https://souqmoon.com/storage/categories/1660469420Tpj5k.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 827,
                    "name": "المقطورات",
                    "image": "https://souqmoon.com/storage/categories/1660470056zrp70.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 828,
                    "name": "الحافلات",
                    "image": "https://souqmoon.com/storage/categories/16604700832gPq8.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 829,
                    "name": "الرافعات",
                    "image": "https://souqmoon.com/storage/categories/1660470106xC69i.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 830,
                    "name": "قلابات وحفارات",
                    "image": "https://souqmoon.com/storage/categories/1660470135MD1gS.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 831,
                    "name": "معدات اخري",
                    "image": "https://souqmoon.com/storage/categories/1660470165TQK0F.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 832,
                    "name": "الشاحنات",
                    "image": "https://souqmoon.com/storage/categories/1660470190x6TIr.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 432,
            "name": "صالونات",
            "image": "https://souqmoon.com/storage/categories/1659004646WTYej.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 823,
                    "name": "كلاسيك",
                    "image": "https://souqmoon.com/storage/categories/1660468876Nco6f.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 824,
                    "name": "مودرن",
                    "image": "https://souqmoon.com/storage/categories/1660468947xiI1P.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 431,
            "name": "المنزل والمطبخ",
            "image": "https://souqmoon.com/storage/categories/1660650050eU8is.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 436,
                    "name": "ادوات منزلية",
                    "image": "https://souqmoon.com/storage/categories/1656861934TyrjF.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 437,
                    "name": "المطبخ",
                    "image": "https://souqmoon.com/storage/categories/1656861943qr1Hi.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 415,
            "name": "السوبر ماركت",
            "image": "https://souqmoon.com/storage/categories/16590044460OkHG.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 424,
                    "name": "الفواكه",
                    "image": "https://souqmoon.com/storage/categories/1656841084Sr1JD.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 414,
            "name": "بوتيكات",
            "image": "https://souqmoon.com/storage/categories/16590024689WQwe.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 420,
                    "name": "الملابس",
                    "image": "https://souqmoon.com/storage/categories/1656841302YAlsH.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 422,
                    "name": "الأحذية",
                    "image": "https://souqmoon.com/storage/categories/1656841346nAl7X.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 423,
                    "name": "العطور",
                    "image": "https://souqmoon.com/storage/categories/16568413593Uhok.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 538,
                    "name": "شنط",
                    "image": "https://souqmoon.com/storage/categories/1658404250rVZNJ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 540,
                    "name": "ساعات",
                    "image": "https://souqmoon.com/storage/categories/1658404353dOYUw.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 541,
                    "name": "ميكاب",
                    "image": "https://souqmoon.com/storage/categories/1658405218MUYSw.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 413,
            "name": "ارقام لوحات",
            "image": "https://souqmoon.com/storage/categories/1659002411Eyzcr.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 419,
                    "name": "لوحات مميزة",
                    "image": "https://souqmoon.com/storage/categories/1656841439G7gF0.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 358,
            "name": "المعادن",
            "image": "https://souqmoon.com/storage/categories/1659002143VLwh6.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 359,
                    "name": "الالمنيوم",
                    "image": "https://souqmoon.com/storage/categories/1650930424BREtj.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 360,
                    "name": "الجير",
                    "image": "https://souqmoon.com/storage/categories/1650930449GoOJm.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 361,
                    "name": "الحديد",
                    "image": "https://souqmoon.com/storage/categories/1650930473SIomv.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 362,
                    "name": "الخام",
                    "image": "https://souqmoon.com/storage/categories/1650930498Ngp8A.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 363,
                    "name": "الرصاص",
                    "image": "https://souqmoon.com/storage/categories/16509305239zZLr.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 364,
                    "name": "النحاس",
                    "image": "https://souqmoon.com/storage/categories/1650930545DeCcQ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 365,
                    "name": "مستلزمات المعادن",
                    "image": "https://souqmoon.com/storage/categories/1650930573gtWH4.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        },
        {
            "id": 261,
            "name": "صالات رياضية",
            "image": "https://souqmoon.com/storage/categories/1660640258flIKQ.webp",
            "ads_count": null,
            "order_by_no": null,
            "sub-categories": [
                {
                    "id": 266,
                    "name": "رياضة بنات",
                    "image": "https://souqmoon.com/storage/categories/1650885475VkA4L.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 267,
                    "name": "رياضة رجال",
                    "image": "https://souqmoon.com/storage/categories/1650885548lWtqQ.webp",
                    "ads_count": null,
                    "order_by_no": null
                },
                {
                    "id": 268,
                    "name": "اندية",
                    "image": "https://souqmoon.com/storage/categories/1650885704NkvKx.webp",
                    "ads_count": null,
                    "order_by_no": null
                }
            ]
        }
    ]
}
 */
