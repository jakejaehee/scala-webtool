package webtool.util.ep.xml

import webtool.util.ep.ElasticParam
import webtool.util.ep.GenTo
import webtool.Configurator

object GenToXML extends GenTo {
  def gen(ep: ElasticParam): String = ep.toString(XMLMkString)

  def contentType: Option[String] = Configurator.Charset match {
    case Some(c) => Some("text/xml; charset=" + c)
    case None    => Some("text/xml")
  }
}