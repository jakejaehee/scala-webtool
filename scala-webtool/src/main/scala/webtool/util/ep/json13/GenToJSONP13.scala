package webtool.util.ep.json13

import webtool.Configurator
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenTo

object GenToJSONP13 extends GenTo {
  def gen(ep: ElasticParam): String = ep.toString(JSONP13MkString)

  def contentType: Option[String] = Configurator.Charset match {
    case Some(c) => Some("text/javascript; charset=" + c)
    case None    => Some("text/javascript")
  }
}