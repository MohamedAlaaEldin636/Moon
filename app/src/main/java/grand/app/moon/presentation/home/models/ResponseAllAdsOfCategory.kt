package grand.app.moon.presentation.home.models

import grand.app.moon.helpers.paging.MABasePaging

// todo rest of model isa.
data class ResponseAllAdsOfCategory(
	var advertisements: MABasePaging<ItemAdvertisementInResponseHome>?
)
