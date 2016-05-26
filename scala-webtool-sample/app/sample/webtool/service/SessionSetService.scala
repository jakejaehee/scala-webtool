package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.util.ep.ElasticParam

class SessionSetService extends Service {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      session ++= req.parameters.filterKeys { k =>
        !k.toLowerCase.startsWith("service")
      }.map(kv => kv._1 -> kv._2.toString)

      val res = ElasticParam()
      res ++= session.all
      res
    }
  }
}