package webtool.util.ep

trait ElasticParamReader {
  def read(): InputSourceMeta
}

sealed case class InputSourceMeta(
  text: String,
  contentType: String,
  encodingOpt: Option[String],
  source: Any)
