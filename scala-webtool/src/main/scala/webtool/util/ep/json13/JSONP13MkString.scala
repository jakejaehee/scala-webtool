package webtool.util.ep.json13

import webtool.util.ep.ColumnInfo
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam
import webtool.util.ep.MkString

object JSONP13MkString extends MkString {
  def mkString(ep: ElasticParam): String =
    ep.get("_jsonpKey_").getOrElse("") + "(" + JSON13MkString.mkString(ep) + ");"

  def EPTypeName = JSON13MkString.EPTypeName

  def mkStringFromParameters(m: Map[String, Any]): String = JSON13MkString.mkStringFromParameters(m)

  def mkString(d: Dataset): String = JSON13MkString.mkString(d)

  def mkString(c: ColumnInfo): String = JSON13MkString.mkString(c)

  def mkString(m: Map[String, Any]): String = JSON13MkString.mkString(m)
}