package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.util.ep.ElasticParam

class Sample06_ParameterService extends Service {
  def execute(reqEP: ElasticParam): Try[ElasticParam] = {
    Try {
      /*
			 * Setting Output ElasticParam
			 */
      val resEP = ElasticParam()

      resEP += ("col1" -> 1111)
      resEP += ("col2" -> "AAAA")

      resEP
    }
  }
}