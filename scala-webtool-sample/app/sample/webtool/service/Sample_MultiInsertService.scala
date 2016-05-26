package sample.webtool.service

import java.sql.Connection
import scala.util.Try
import webtool.Service
import webtool.ServiceUtil
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam
import webtool.util.sqlrepo.SqlConn
import play.api.Play.current
import play.api.db.DB
import webtool.util.ep.Record

class Sample_MultiInsertService extends Service {
  def execute(ep: ElasticParam): Try[ElasticParam] = {
    Try {
      val loop = ep.get("loop").getOrElse("1").toString.toInt
      val transaction = ep.get("tx").getOrElse("false").toString.toBoolean

      val conn: Connection = DB.getConnection("default", !transaction)
      val sqlConn = SqlConn(conn)

      val dataset: Dataset = ep.getDataset("1").get
      val record: Record[Any] = dataset.rows(0)
      record += "emp_no" -> 1
      
      for (emp_no <- 2 to loop) {
        val r = new Record[Any]
        r ++= record
        r += "emp_no" -> emp_no
        r += "emp_name" -> (r.get("emp_name").getOrElse("kang") + emp_no.toString)
        dataset += r
      }

      try {
        val (errOpt, dsList) = sqlConn.query("sample.update_insert", dataset.rows)

        if (transaction && errOpt == None)
          sqlConn.commit()

        val resEP = ElasticParam()
        errOpt.foreach(e => ServiceUtil.setCodeAndMsg(resEP, 999, e.getMessage))

        var i = 1
        dsList.foreach { ds =>
          ds.name = i.toString
          resEP.setDataset(ds)
          i += 1
        }
        resEP
      } finally {
        sqlConn.close()
      }
    }
  }
}