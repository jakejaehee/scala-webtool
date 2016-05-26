package webtool.util.ep.json13

import webtool.Configurator
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenTo

object GenToJSON13 extends GenTo {
  def gen(ep: ElasticParam): String = ep.toString(JSON13MkString)

  def contentType: Option[String] = Configurator.Charset match {
    case Some(c) => Some("text/json; charset=" + c)
    case None    => Some("text/json")
  }
}