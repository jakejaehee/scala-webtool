package webtool.util.web

import java.net.URLConnection

import scala.collection.JavaConversions._

import com.typesafe.scalalogging.LazyLogging

object CookieMgr extends LazyLogging {
  private var cookiesMap = Map[String, List[Cookie]]()

  def getCookieBase(uri: String): String = {
    uri match {
      case null | "" => "/"
      case _ =>
        uri.lastIndexOf('/') match {
          case -1 | 0 => "/"
          case i => uri.substring(0, i)
        }
    }
  }

  def setCookie(cookie: Cookie) {
    cookiesMap.get(cookie.path) match {
      case Some(cookies) => cookiesMap += cookie.path -> (cookie :: cookies)
      case None => cookiesMap += cookie.path -> List(cookie)
    }
  }

  def setCookie(conn: URLConnection) {
    conn.getHeaderFields.lift("Set-Cookie") match {
      case Some(values) =>
        val cookie = new Cookie()
        values foreach {
          _.split(";").foreach { nvStr =>
            cookie.setAttribute(nvStr)
            logger.trace("Set-Cookie token: {0}", nvStr);
          }
        }
        setCookie(cookie)
      case None =>
    }
  }

  def getCookieString(uri: String): String = {
    val sb = new StringBuilder()
    cookiesMap.foreach {
      case (path, cookies) =>
        if (uri.startsWith(path)) {
          sb ++= cookies.mkString
        }
    }
    sb.toString
  }
}
