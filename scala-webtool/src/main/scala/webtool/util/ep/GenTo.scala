package webtool.util.ep

trait GenTo {
  def gen(ep: ElasticParam): String
  def contentType: Option[String]
}