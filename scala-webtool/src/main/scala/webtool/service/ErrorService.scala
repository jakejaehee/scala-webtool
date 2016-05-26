package webtool.service

import scala.util.Failure
import scala.util.Try
import webtool.util.ep.ElasticParam
import webtool.Service

class ErrorService extends Service {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Failure(new Exception("error: 999"))
  }
}