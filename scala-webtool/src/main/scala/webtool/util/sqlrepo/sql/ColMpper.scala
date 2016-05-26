package webtool.util.sqlrepo.sql

import webtool.epMkString
import webtool.util.DataValid
import webtool.util.ep.Record

case class ColMapper(colMaps: Record[ColMap]) {
  var map = if (DataValid.isEmpty(colMaps)) Record[ColMap]() else colMaps

  def +=(colMap: ColMap) {
    if (colMap.sqlColIndex == -1)
      map += colMap.sqlColName -> colMap
    else
      map += colMap.sqlColIndex.toString -> colMap
  }

  def apply(sqlColName: String): Option[ColMap] = map.get(sqlColName)

  def apply(sqlColIndex: Int): Option[ColMap] = map.get(sqlColIndex.toString)
}
