package webtool.service.sqlrepo

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.Service
import webtool.util.ep.ElasticParam
import webtool.util.sqlrepo.SqlRepo

/**
 * URL Parameter: src(Package ID), dst(Package ID)
 */
class RenamePkg extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      val src = req.get("src").map(_.toString)
      val dst = req.get("dst").map(_.toString)
      if (SqlRepo.renamePackage(src, dst)) {
        new AllIdsFromDisk().execute(req) match {
          case Success(ep) => ep
          case Failure(e)  => throw e
        }
      } else {
        throw new Exception("failed to rename webtool")
      }
    }
  }
}
