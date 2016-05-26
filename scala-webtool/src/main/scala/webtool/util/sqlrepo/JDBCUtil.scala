package webtool.util.sqlrepo

import java.sql.ResultSet
import java.sql.ResultSetMetaData

import webtool.epMkString
import webtool.util.DataTypeUtil
import webtool.util.DataValid
import webtool.util.ep.Record
import webtool.util.sqlrepo.sql.ColMapper

object JDBCUtil {
  def toMap(rs: ResultSet, md: ResultSetMetaData, colMapperOpt: Option[ColMapper]): Option[Record[Any]] = {
    if (rs == null) return None

    val colCnt = md.getColumnCount()

    val row = Record.empty[Any]
    for (sqlColIndex <- 1 to colCnt) {
      val value = rs.getObject(sqlColIndex)
      val sqlType = md.getColumnType(sqlColIndex)
      val sqlColName = md.getColumnName(sqlColIndex)

      colMapperOpt.flatMap {
        colMapper => if (DataValid.isNotEmpty(sqlColName)) colMapper(sqlColName) else colMapper(sqlColIndex)
      }.map {
        cm =>
          cm.sqlType = sqlType
          row += cm.javaColName -> DataTypeUtil.valueByTypeName(value, cm.javaTypeName)
          cm
      }.orElse {
        row += sqlColName -> value
        None
      }
    }

    Some(row)
  }
}