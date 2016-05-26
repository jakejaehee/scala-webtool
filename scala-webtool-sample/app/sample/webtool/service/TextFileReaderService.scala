package sample.webtool.service

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import webtool.Configurator
import webtool.Service
import webtool.ServiceUtil
import webtool.epMkString
import webtool.util.TextFileUtil
import webtool.util.ep.ElasticParam

class TextFileReaderService extends Service {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      //ifNotAccessibleThrow.foreach(throw _)
      ifMissingThrow("path", req).foreach(throw _)

      val path = req.get("path").get
      val encodingOpt = req.get("encoding").map(_.toString)

      val res = ElasticParam()

      TextFileUtil.textFrom(
        new File(Configurator.AppRootPath.getOrElse("") + File.separator + path),
        encodingOpt) match {
          case Success(text) => res += "text" -> text
          case Failure(e)    => ServiceUtil.setCodeAndMsg(res, 999, e.getMessage)
        }

      res
    }
  }
}