package grand.app.moon.domain.general.paginate

import java.io.Serializable

data class Meta (
  val path:String="",
  val per_page:Int=0,
  val total:Int=0,
  val last_page:Int=0,
  val from:Int=0,
  val to:Int=0,
  val current_page:Int=0,
  ): Serializable