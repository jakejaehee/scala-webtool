package webtool.util.ep.json12

import java.net.URLDecoder
import webtool.epMkString
import webtool.util.ep.ColumnInfo
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenFrom
import play.api.libs.json.JsBoolean
import play.api.libs.json.JsNumber
import play.api.libs.json.JsString
import play.api.libs.json.JsSuccess
import play.api.libs.json.JsValue
import play.api.libs.json.JsValue.jsValueToJsLookup
import play.api.libs.json.Json
import play.api.libs.json.Reads
import webtool.Key
import webtool.DefaultVal

object GenFromJSON12 extends GenFrom {
  def gen(text: String, encodingOpt: Option[String]): ElasticParam =
    GenFromJSON12.gen(Json.parse(URLDecoder.decode(text, encodingOpt.getOrElse(DefaultVal.Charset))))

  def gen(json: JsValue): ElasticParam = {
    val ep: ElasticParam = ElasticParam() //ElasticParam.empty
    ep ++= (json \ "parameters").as[Map[String, Any]]
    ep.datasets = (json \ "datasets").as[Map[String, Dataset]]
    ep
  }

  implicit val mapReads = new Reads[Map[String, Any]] {
    def reads(json: JsValue) =
      JsSuccess(
        value = json.as[Map[String, JsValue]].mapValues { v =>
          v match {
            case s: JsString  => s.as[String]
            case n: JsNumber  => n.as[Double]
            case b: JsBoolean => b.as[Boolean]
            case _            => v
          }
        })
  }

  implicit val columnInfoReads = new Reads[ColumnInfo] {
    def reads(json: JsValue) = {
      JsSuccess(ColumnInfo(
        (json \ "id").as[String],
        (json \ "text").asOpt[String].getOrElse((json \ "id").as[String]),
        (json \ "type").asOpt[String].getOrElse("String"),
        (json \ "size").asOpt[Int].getOrElse(0)))
    }
  }

  implicit val datasetReads = new Reads[Map[String, Dataset]] {
    def reads(json: JsValue) =
      JsSuccess(
        json.as[Map[String, JsValue]].map { v =>
          v._1 -> {
          	val d = Dataset(v._1, (v._2 \ "colInfos").as[List[ColumnInfo]])
          	d.rows = (v._2 \ "rows").as[List[Map[String, Any]]]
          	d
          }
        })
  }
}
