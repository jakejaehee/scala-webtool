package webtool.util.ep.json12

import java.net.URLDecoder

import webtool.DefaultVal
import webtool.util.URLParamsUtil
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenFrom
import play.api.libs.json.Json

object GenFromJSONP12 extends GenFrom {
  def gen(inText: String, encodingOpt: Option[String]): ElasticParam = {
    val jsonStrDecoded = parseJsonp(inText)
    val jsonStrEncoded = URLDecoder.decode(jsonStrDecoded, encodingOpt.getOrElse(DefaultVal.Charset))
    val json = Json.parse(jsonStrEncoded)
    val ep = GenFromJSON12.gen(json)
    ep += ("_jsonpKey_", parseJsonpKey(inText, encodingOpt))
    ep
  }

  def parseJsonp(qStr: String): String = {
    val jsonFilter = """.*&(.+)&.*""".r
    qStr match {
      case jsonFilter(a) => a
      case _ => ""
    }
  }

  def parseJsonpKey(inText: String, encodingOpt: Option[String]): String = {
    val params = URLParamsUtil.queryStringToMap(inText, encodingOpt)
    params.getOrElse("_jsonpKey_", Seq(""))(0)
  }
}