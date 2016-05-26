package webtool.web.play

import com.typesafe.scalalogging.LazyLogging

import webtool.DefaultVal
import webtool.Configurator
import webtool.util.DataValid
import webtool.util.ep.ElasticParamReader
import webtool.util.ep.InputSourceMeta
import play.api.mvc.RawBuffer
import play.api.mvc.Request

case class PlayReader(request: Request[RawBuffer]) extends ElasticParamReader with LazyLogging {

  def read(): InputSourceMeta = {
    val contentType = request.contentType.getOrElse("")
    val encodingOpt = request.charset.orElse(Configurator.Charset)

    // Play 2.5
    // val bytes = request.body.initialData.asByteBuffer.array()
    // Play 2.4
    val bytes = request.body.asBytes(10000L).getOrElse(Array.empty[Byte])
    val text = if (DataValid.isNotEmpty(request.rawQueryString))
      request.rawQueryString
    else
      new String(bytes, encodingOpt.getOrElse(DefaultVal.Charset))

    logger.trace(s"method: ${request.method}")
    logger.trace(s"contentType: ${request.contentType.getOrElse("")}")
    logger.trace(s"encoding: ${request.charset}")
    logger.trace(s"request.body.toString(): ${request.body.toString()}")

    InputSourceMeta(text, contentType, encodingOpt, request)
  }
}