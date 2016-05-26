package webtool.web.servlet

import webtool.util.ep.ElasticParamReader
import webtool.util.ep.InputSourceMeta
import javax.servlet.http.HttpServletRequest

case class ServletReader(request: HttpServletRequest) extends ElasticParamReader {
  def read(): InputSourceMeta = {
    val text = EPRequestUtil.getQueryString(request)
    val contentType = request.getContentType()
    val encodingOpt = EPRequestUtil.getCharacterEncoding(request)

    InputSourceMeta(text, contentType, encodingOpt, request)
  }

}