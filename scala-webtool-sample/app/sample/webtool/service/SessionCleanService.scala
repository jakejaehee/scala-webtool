package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.util.ep.ElasticParam

class SessionCleanService extends Service {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      session.clear()

      val res = ElasticParam()
      res ++= session.all
      res
    }
  }
}