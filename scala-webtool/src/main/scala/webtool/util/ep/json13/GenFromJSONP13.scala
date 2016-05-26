package webtool.util.ep.json13

import java.net.URLDecoder

import webtool.DefaultVal
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenFrom
import webtool.util.ep.json12.GenFromJSONP12
import play.api.libs.json.Json

object GenFromJSONP13 extends GenFrom {
  def gen(inText: String, encodingOpt: Option[String]): ElasticParam = {
    val jsonStrDecoded = GenFromJSONP12.parseJsonp(inText)
    val jsonStrEncoded = URLDecoder.decode(jsonStrDecoded, encodingOpt.getOrElse(DefaultVal.Charset))
    val json = Json.parse(jsonStrEncoded)
    val ep = GenFromJSON13.gen(json)
    ep += ("_jsonpKey_", GenFromJSONP12.parseJsonpKey(inText, encodingOpt))
    ep
  }
}