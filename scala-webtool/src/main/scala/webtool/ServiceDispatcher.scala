package webtool

import scala.util.Try

import webtool.util.ReflectionUtil
import webtool.util.ep.ElasticParam

object ServiceDispatcher {

  def loadService(svcName: String): Try[Service] = {
    Try {
      ReflectionUtil.newInstance(svcName, Nil).asInstanceOf[Service]
    }.map { svc =>
      svc.setScopeOfRequest(Service.ScopeKey_ReqTime -> System.currentTimeMillis())
      svc
    }
  }

  def loadService(req: ElasticParam): Try[Service] =
    ServiceUtil.getServiceName(req).flatMap(loadService(_))
}
