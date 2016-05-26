package webtool.util.ep.json12

import webtool.Configurator
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenTo

object GenToJSONP12 extends GenTo {
  def gen(ep: ElasticParam): String = ep.toString(JSONP12MkString)

  def contentType = Configurator.Charset match {
    case Some(c) => Some("text/javascript; charset=" + c)
    case None    => Some("text/javascript")
  }
}