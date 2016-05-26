package webtool.service.sqlrepo

import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.Service
import webtool.ServiceUtil
import webtool.epMkString
import webtool.util.ep.ElasticParam
import webtool.util.ep.Record
import webtool.util.sqlrepo.SqlRepo

/**
 * URL Parameter: NOT REQUIRED
 */
class AllIdsFromDisk extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      val resEP = ServiceUtil.epWithCodeMessage(0, "Successful")
      SqlRepo.allIdsFromDisk().foreach {
        case ((pkgId, idSet)) =>
          resEP.addDatasetRow("1", Record(("id", pkgId), ("idType", "webtool")))
          idSet.foreach { id =>
            resEP.addDatasetRow("1", Record(("id", id), ("idType", "sql")))
          }
      }
      resEP
    }
  }
}
