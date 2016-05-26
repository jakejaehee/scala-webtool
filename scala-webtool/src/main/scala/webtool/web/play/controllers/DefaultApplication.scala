package webtool.web.play.controllers

import com.typesafe.scalalogging.LazyLogging

import webtool.ServiceUtil
import webtool.web.play.PlayReader
import play.api.mvc.Action
import play.api.mvc.Controller

class DefaultApplication extends Controller with LazyLogging {

  def index = Action(parse.raw) { request =>
    val inSrc = PlayReader(request).read
    ServiceUtil.logRequest(inSrc)

    val (svc, resText, cTypeOpt) = ServiceUtil.execService(
      inSrc,
      request.session.data.toMap)

    var result = cTypeOpt match {
      case Some(contentType) => Ok(resText).as(contentType)
      case None => Ok(resText)
    }

    svc.foreach { s =>
      if (s.session.isEmpty) {
        result = result.withNewSession
      } else {
        result = result.withSession {
          s.session.deleted.foldLeft(request.session)((ss, k) => ss - k)
        }
        result = result.withSession {
          s.session.all.foldLeft(request.session) { (ss, kv) => ss + kv }
        }
      }
    }

    ServiceUtil.logResponse(resText, cTypeOpt)
    result
  }
}
