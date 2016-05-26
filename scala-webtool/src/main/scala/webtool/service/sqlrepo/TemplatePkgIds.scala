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
class TemplatePkgIds extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      val resEP = ServiceUtil.epWithCodeMessage(0, "Successful")
      SqlRepo.templatePkgIds().foreach { pkgId =>
        resEP.addDatasetRow("1", Record(("id", pkgId), ("idType", "webtool")))
      }
      resEP
    }
  }
}
