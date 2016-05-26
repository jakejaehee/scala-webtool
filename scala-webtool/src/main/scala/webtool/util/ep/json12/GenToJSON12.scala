package webtool.util.ep.json12

import webtool.Configurator
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenTo

object GenToJSON12 extends GenTo {
  def gen(ep: ElasticParam): String = ep.toString(JSON12MkString)

  def contentType: Option[String] = Configurator.Charset match {
    case Some(c) => Some("text/json; charset=" + c)
    case None    => Some("text/json")
  }
}