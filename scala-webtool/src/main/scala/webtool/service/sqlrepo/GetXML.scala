package webtool.service.sqlrepo

import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import webtool.Service
import webtool.ServiceUtil
import webtool.epMkString
import webtool.util.ep.ElasticParam
import webtool.util.ep.Record
import webtool.util.sqlrepo.SqlRepo
import webtool.util.TextFileUtil
import java.io.File
import scala.util.Failure
import scala.util.Success

/**
 * URL Parameter: sqlId(Full SQL ID)
 */
class GetXML extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      ifMissingThrow("sqlId", req).foreach(throw _)

      val fullSqlId = req.get("sqlId").getOrElse("").toString
      val xmlPath = SqlRepo.filePath(SqlRepo.pkgAndIdFromFullSqlId(fullSqlId))
      val encodingOpt = TextFileUtil.detectEncodingOfFile(new File(xmlPath))

      TextFileUtil.textFrom(new File(xmlPath), encodingOpt) match {
        case Success(xmlText) =>
          val resEP = ServiceUtil.epWithCodeMessage(0, "Successful")
          resEP.addDatasetRow("1", Record(("sqlId", fullSqlId), ("xml", xmlText)))
          resEP
        case Failure(e) => throw e
      }
    }
  }
}
