package webtool.util.ep

import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.util.ep.json12.GenFromJSON12
import webtool.util.ep.json12.GenFromJSONP12
import webtool.util.ep.json12.GenToJSON12
import webtool.util.ep.json12.GenToJSONP12
import webtool.util.ep.json13.GenFromJSON13
import webtool.util.ep.json13.GenFromJSONP13
import webtool.util.ep.json13.GenToJSON13
import webtool.util.ep.json13.GenToJSONP13
import webtool.util.ep.urlparams.GenFromURLParams
import webtool.util.ep.xml.GenFromXML
import webtool.util.ep.xml.GenToXML
import play.api.libs.json.JsError
import play.api.libs.json.JsLookupResult.jsLookupResultToJsLookup
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup

object EPUtil extends LazyLogging {
  def detectGenFrom(contentType: String, text: String): Option[GenFrom] = contentType.toLowerCase match {
    case c if c.contains("multipart/form-data") => Some(GenFromURLParams)
    case c if c.contains("application/json") => detectGenFromJSON(text)
    case c if c.contains("text/json") => detectGenFromJSON(text)
    case c if c.contains("application/xml") => Some(GenFromXML)
    case c if c.contains("text/xml") => Some(GenFromXML)
    case c if c.contains("application/x-www-form-urlencoded") =>
      if (text.startsWith("<?xml "))
        Some(GenFromXML)
      else if (text.contains("_jsonpKey_="))
        detectGenFromJSONP(text)
      else
        Some(GenFromURLParams)
    case "" =>
      if (text.contains("_jsonpKey_="))
        detectGenFromJSONP(text)
      else
        Some(GenFromURLParams)
    case _ => None
  }

  private def detectGenFromJSON(bodyText: String): Option[GenFrom] = {
    detectEPTypeInJSON(bodyText).map { epType =>
      logger.trace("epType: " + epType)

      epType.toLowerCase match {
        case "json12" => GenFromJSON12
        case "json13" => GenFromJSON13
        case "jsonp12" => GenFromJSON12
        case "jsonp13" => GenFromJSON13
      }
    }
  }

  private def detectGenFromJSONP(bodyText: String): Option[GenFrom] = {
    detectEPTypeInJSON(bodyText).map { epType =>
      logger.trace("epType: " + epType)

      epType.toLowerCase match {
        case "json12" => GenFromJSONP12
        case "json13" => GenFromJSONP13
        case "jsonp12" => GenFromJSONP12
        case "jsonp13" => GenFromJSONP13
      }
    }
  }

  private def detectEPTypeInJSON(jsonText: String): Option[String] = {
    def epType(q: String): Option[String] = {
      val filter = q + "epType" + q + ":" + q
      jsonText.indexOf(filter) match {
        case -1 => None
        case i =>
          jsonText.indexOf(q, i + filter.size) match {
            case -1 => None
            case i2 => Some(jsonText.substring(i + filter.size, i2))
          }
      }
    }
    epType("%22").orElse(epType("\""))
  }

  def detectGenFrom(json: JsValue): Option[GenFrom] = {
    (json \ "parameters" \ "epType").validate[String] match {
      case JsSuccess("json12", _) => Some(GenFromJSON12)
      case JsSuccess("json13", _) => Some(GenFromJSON13)
      case err @ JsError(_) =>
        logger.error(err.toString)
        None
      case _ => None
    }
  }

  def tryToDetectGenTo(resType: String): Try[GenTo] =
    Try { detectGenTo(resType).getOrElse(throw new Exception("unknown GenTo name")) }

  def detectGenTo(resType: String): Option[GenTo] = resType.toLowerCase match {
    case "json12" => Some(GenToJSON12)
    case "jsonp12" => Some(GenToJSONP12)
    case "json13" => Some(GenToJSON13)
    case "jsonp13" => Some(GenToJSONP13)
    case "platformxml" => Some(GenToXML)
    case _ => None
  }
}