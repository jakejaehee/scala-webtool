package webtool.util.ep.xml

import scala.util.Failure
import scala.util.Success
import scala.xml.Elem

import com.typesafe.scalalogging.LazyLogging

import webtool.ServiceUtil
import webtool.epMkString
import webtool.util.ExceptionDetail
import webtool.util.GenXML
import webtool.util.ep.ColumnInfo
import webtool.util.ep.Dataset
import webtool.util.ep.DatasetUtil
import webtool.util.ep.ElasticParam
import webtool.util.ep.GenFrom
import webtool.util.ep.Record

object GenFromXML extends GenFrom with LazyLogging {

  val VAL_DEFAULT_NULL = "${NULL}"

  def gen(xml: Elem): ElasticParam = {
    val ep = ElasticParam()
    val paramList = xml \ "Root" \ "Parameters" \ "Parameter"
    paramList foreach { x =>
      val id = x \@ "id"
      val value = x.text
      ep += id -> value
    }

    val dsList = xml \ "Root" \ "Dataset"
    dsList.foreach { dsNode =>
      val dataSetId = dsNode \@ "id"

      val webDS = Dataset(dataSetId)
      ep.setDataset(webDS)

      // Parses ColumnInfo node and declares ColumnInfos
      var colMap = Map[String, ColumnInfo]()

      dsNode \ "ColumnInfo" \ "Column" foreach { cNode =>
        val id = cNode \@ "id"
        val col = ColumnInfo(id, id, cNode \@ "type", (cNode \@ "size").toInt)
        webDS.addColInfo(col)
        colMap += id -> col
      }

      // Parses Rows node
      dsNode \ "Rows" \ "Row" foreach { rNode =>
        // Adds a record to the Dataset.
        val wRow = Record.empty[Any]
        webDS += wRow

        rNode \ "Col" foreach { x =>
          for {
            ci <- colMap.get(x \@ "id")
          } yield DatasetUtil.setColTo(wRow, ci.id, ci.typeClass, x.text, VAL_DEFAULT_NULL)
        }
      }
    }
    ep
  }

  def gen(text: String, encodingOpt: Option[String]): ElasticParam = {
    GenXML.loadString(text) match {
      case Success(xml) => gen(xml)
      case Failure(e) =>
        logger.error("xml: " + text + "\n" + ExceptionDetail.getDetail(e))
        ServiceUtil.epWithCodeMessage(999, e.getMessage)
    }
  }
}