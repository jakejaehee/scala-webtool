package webtool.util.ep

trait GenFrom {
  def gen(text: String, encodingOp: Option[String]): ElasticParam
}