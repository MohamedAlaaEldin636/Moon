package grand.app.moon.presentation.addStore

open class BrowserHelper {
  var lastUrl = ""
  val logoutUrls = mutableListOf<String>().also {
    it.add("souqmoon.com/store/login")
    it.add("souqmoon.com/store/register")
    it.add("souqmoon.com/store/logout")
    it.add("souqmoon.com/store/forget_password")
  }

  fun isUser(): Boolean {
    logoutUrls.forEach {
      if (lastUrl.contains(it)) return true
    }
    return false
  }

  fun isUser(url: String): Boolean{
    logoutUrls.forEach {
      if (url.contains(it)) return true
    }
    return false
  }

  fun lastUrl(): String {
    return this.lastUrl
  }

  fun setLastUr(url: String) {
    this.lastUrl = url
  }
}