package webtool.service.sqlrepo

import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.Service
import webtool.ServiceUtil
import webtool.util.ep.ElasticParam
import webtool.util.sqlrepo.SqlRepo

/**
 * URL Parameter: sqlId(Full SQL ID)
 */
class RemoveSql extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      ifMissingThrow("sqlId", req).foreach(throw _)

      val fullSqlId = req.get("sqlId").getOrElse("").toString

      if (SqlRepo.removeSql(fullSqlId))
        ServiceUtil.epWithCodeMessage(0, "Successful")
      else
        throw new Exception("failed to remove sql")
    }
  }
}
