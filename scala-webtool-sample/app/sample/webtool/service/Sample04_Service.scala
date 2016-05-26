package sample.webtool.service

import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.Service
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam

class Sample04_Service extends Service with LazyLogging {
  def execute(reqEP: ElasticParam): Try[ElasticParam] = {
    Try {
      /*
			 * Checking Input ElasticParam
			 */
      logger.trace("Input Parameter ABC: " + reqEP.get("ABC"))
      logger.trace("Input Parameters: " + reqEP.parameters)
      logger.trace("Input Dataset named '1': " + reqEP.getDataset("1"))

      /*
			 * Setting Output ElasticParam
			 */
      val resEP = ElasticParam()

      // Set Dataset named '1'
      val resDS1 = Dataset("1")
      resEP.setDataset(resDS1)

      // Set row1 which is a Row object
      resDS1 += Map(("c1" -> 111), ("c2" -> reqEP.get("ABC").getOrElse("")))

      // Set row2 which is a Map object
      resDS1 += Map(("c1" -> 222), ("c2" -> reqEP.get("ABC").getOrElse("")))

      resEP
    }
  }
}