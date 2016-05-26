package webtool.util

import java.io.File

import scala.util.Failure
import scala.util.Success
import scala.util.Try
import scala.xml.Elem
import scala.xml.XML

object GenXML {
  private val envs: scala.collection.mutable.Map[String, String] =
    scala.collection.mutable.Map.empty[String, String]

  def addXmlEnv(xmlEnvs: Map[String, String]) { envs ++= xmlEnvs }

  def addXmlEnv(kv: (String, String)) { envs += kv }

  def loadFile(fpath: String, encOp: Option[String]): Try[Elem] = loadFile(new File(fpath), encOp)

  def loadFile(file: File, encOp: Option[String]): Try[Elem] = {
    toString(file, encOp) match {
      case Success(s) => loadString(s)
      case Failure(e) => Failure(e)
    }
  }

  def toString(file: File, encOp: Option[String]): Try[String] = {
    Try {
      TextFileUtil.textFrom(file, encOp) match {
        case Success(t) =>
          val text = ReplaceableText(t)
          XmlEnv.iterator.foreach(text.setValue(_))
          text.toString
        case Failure(e) => throw e
      }
    }
  }

  def loadString(str: String): Try[Elem] = {
    Try {
      val text = ReplaceableText(str)
      XmlEnv.iterator.foreach(text.setValue(_))
      XML.loadString(text.toString)
    }
  }

  override def toString(): String = if (envs != null) envs.toString else ""
}
