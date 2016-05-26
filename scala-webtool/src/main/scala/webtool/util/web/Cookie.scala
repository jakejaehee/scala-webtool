package webtool.util.web

import webtool.util.URLParamsUtil

object Cookie {
  val EXPIRES = "EXPIRES"
  val PATH = "PATH"
  val DOMAIN = "DOMAIN"
  val SECURE = "SECURE"
}

class Cookie {
  import Cookie._

  // Set-Cookie: NAME=VALUE; expires=DATE; path=PATH; domain=DOMAIN_NAME;
  // secure

  // Cookie: CUSTOMER=WILE_E_COYOTE; PART_NUMBER=ROCKET_LAUNCHER_0001;
  // SHIPPING=FEDEX

  private var map = Map[String, String]()
  private var _path: String = null
  private var _expires: String = null
  private var _domain: String = null
  private var _secure = false

  def setAttribute(nvStr: String) {
    URLParamsUtil.queryStringToMap(nvStr, None).foreach {
      case (n, v) =>
        n match {
          case EXPIRES => _expires = v(0)
          case PATH => _path = v(0)
          case DOMAIN => _domain = v(0)
          case SECURE => _secure = true
          case _ => this.map += n -> v(0)
        }
    }
  }

  def path = _path
  def expires = _expires
  def domain = _domain
  def secure = _secure

  def toNVString: String = {
    val sb = new StringBuilder()
    map.foreach { case (n, v) => sb ++= n + "=" + v + "; " }
    sb.toString
  }

  override def toString = toNVString
}