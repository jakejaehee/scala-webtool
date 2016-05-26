package webtool.util.sqlrepo.sql

import scala.collection.mutable.MutableList

import webtool.util.StringUtil
import webtool.util.ep.ColumnInfo

trait MultiPart {
  private val partList = MutableList[Any]()
  private var prepend: String = ""

  def setPrepend(prepend: String) = this.prepend = prepend

  def getPrepend = prepend

  def +=(part: Any): MultiPart = {
    partList += part
    this
  }

  def filterSubPart(f: Any => Boolean): MutableList[Any] = partList.filter(f)

  def getSubParts = partList

  def getSubPartsCount = partList.size

  def getSubPart(idx: Int) = partList.get(idx)

  def visiable(record: Map[String, Any]): Boolean

  def getCols(record: Map[String, Any]): Array[ColumnInfo] =
    if (!visiable(record))
      return Array[ColumnInfo]()
    else
      partList.foldLeft(Array[ColumnInfo]())((b, a) => b ++ getColsFrom(a, record))

  private def getColsFrom(sub: Any, record: Map[String, Any]): Array[ColumnInfo] =
    sub match {
      case a: MultiPart => a.getCols(record)
      case a: TextPart  => a.cols
      case _            => Array()
    }

  def toText(record: Map[String, Any], preparedStmt: Boolean): String =
    if (!visiable(record))
      return ""
    else
      partList.foldLeft("") { (b, part) =>
        b + (toTextFrom(part, record, preparedStmt) match {
          case "" => ""
          case s  => if (StringUtil.isBlank(b)) s else "\n" + s
        })
      }

  private def toTextFrom(part: Any, record: Map[String, Any], preparedStmt: Boolean): String = {
    part match {
      case a: MultiPart => a.toText(record, preparedStmt)
      case a: TextPart  => a.text(record, preparedStmt)
      case a: String    => a
      case _            => ""
    }
  }

  override def toString = toText(Map(), false)
}
