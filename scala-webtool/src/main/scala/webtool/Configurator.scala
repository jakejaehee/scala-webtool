package webtool

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.xml.Elem

import com.typesafe.scalalogging.LazyLogging

import webtool.util.DataValid
import webtool.util.ExceptionDetail
import webtool.util.FilePathUtil
import webtool.util.GenXML
import webtool.util.XmlEnv
import webtool.util.sqlrepo.SqlRepo

object Configurator extends LazyLogging {
  private var _xml: Elem = <Config></Config>
  private var appRootPath: Option[String] = None
  private var xmlDirOpt: Option[String] = None
  private var charsetOpt: Option[String] = None

  def init(xmlFile: File, appRootDir: Option[File]) {
    logger.info("************** webtool config file: " + xmlFile.getAbsolutePath)

    appRootPath = appRootDir.map(_.getAbsolutePath)
    xmlDirOpt = Some(FilePathUtil.getBasePath(xmlFile))

    GenXML.loadFile(xmlFile, None) match {
      case Success(xml) =>
        _xml = xml

        (_xml \\ "xmlEnv" \ "variable").foreach { node =>
          XmlEnv += (node \ "@name").text -> (node \ "@value").text.replace("&", "&amp;")
        }

        (_xml \\ "charset").foreach { node =>
          if (DataValid.isNotEmpty(node.text))
            charsetOpt = Some(node.text)
        }

        XmlEnv.iterator.foreach { kv => logger.trace("XmlEnv(" + kv._1 + "): " + kv._2) }
        logger.trace("charsetOpt: " + charsetOpt)

        (_xml \\ "sqlRepo").foreach { node =>
          SqlRepo.init(xmlDirOpt.get + File.separatorChar + node.text)
        }
      case Failure(e) => logger.error("file: " + xmlFile.getAbsolutePath + "\n" + ExceptionDetail.getDetail(e))
    }
  }

  def AppRootPath = appRootPath

  def Charset = charsetOpt

  def getXml: Elem = _xml
}
