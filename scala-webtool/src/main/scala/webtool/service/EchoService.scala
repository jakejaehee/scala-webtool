package webtool.service

import scala.util.Success
import scala.util.Try
import com.typesafe.scalalogging.LazyLogging
import webtool.util.ep.ElasticParam
import webtool.Service

class EchoService extends Service with LazyLogging {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Success(req)
  }
}