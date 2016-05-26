package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam

class Sample05_DatasetService extends Service {
  def execute(reqEP: ElasticParam): Try[ElasticParam] = {
    Try {
      val row1 = Map(("col1" -> 1111), ("col2" -> "AAAA"))
      val row2 = Map(("col1" -> None), ("col2" -> "BBBB"))
      val row3 = Map(("col1" -> 3333), ("col2" -> "BBBB"))
      val list = List(row1, row2, row3)

      /*
			 * Setting Output ElasticParam
			 */
      val resEP = ElasticParam()

      // Set Dataset named '1'
      val resDS1 = Dataset("1")
      resEP.setDataset(resDS1)

      resDS1 ++= list

      resEP
    }
  }
}