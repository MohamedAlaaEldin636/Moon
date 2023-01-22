package grand.app.moon.domain.shop

import com.google.gson.annotations.SerializedName

data class ResponseWorkingHour(
	var from: String? = null,
	var to: String? = null,
	@SerializedName("status") var enabled: Boolean? = null,
) {



}
/*
"data": [
        {
            "from": "09:20",
            "to": "21:20",
            "status": true
        }
 */
