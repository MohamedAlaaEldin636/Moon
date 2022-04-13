package grand.app.moon.domain.general.paginate

import com.structure.base_mvvm.domain.general.paginate.Links
import java.io.Serializable

open class Paginate(
  val meta: Meta,
  val links: Links,
) : Serializable{
}