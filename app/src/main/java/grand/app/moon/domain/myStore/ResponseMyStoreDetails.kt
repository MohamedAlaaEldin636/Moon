package grand.app.moon.domain.myStore

data class ResponseMyStoreDetails(
	var backgroundImage: String?,
	var profileImage: String?,
	var storeName: String?,
	var userName: String?,
	var cityId: Int?,
	var areaId: Int?,
	var latitude: String?,
	var longitude: String?,
	var address: String?,
	var description: String?,
	var email: String?,
	var websiteLink: String?,
	var contactPhone: String?,
	var whatsAppPhone: String?,
	var taxNumber: String?,
)
/*
val backgroundImage = MutableLiveData<GlideImageViaXmlModel>()

	val profileImage = MutableLiveData<GlideImageViaXmlModel>()

	val storeName = MutableLiveData("")

	val userName = MutableLiveData("")

	val cities = emptyList<Country>()
	val selectedCity = MutableLiveData<Country>()

	val regions = emptyList<Country>()
	val selectedRegion = MutableLiveData<Country>()

	private val locationData = MutableLiveData<LocationData?>()
	val address = locationData.map { it?.address }

	val description = MutableLiveData("")
 */
