package webtool.util.ep

trait MkString {
  def mkString(ep: ElasticParam): String
  
  def mkStringFromParameters(m: Map[String, Any]): String

  def mkString(d: Dataset): String

  def mkString(c: ColumnInfo): String

  def mkString(m: Map[String, Any]): String
  
  def EPTypeName: String
}
