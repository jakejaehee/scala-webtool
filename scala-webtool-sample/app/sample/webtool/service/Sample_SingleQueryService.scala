package sample.webtool.service

import java.sql.Connection

import scala.util.Try

import webtool.Service
import webtool.ServiceUtil
import webtool.epMkString
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam
import webtool.util.sqlrepo.SqlConn
import play.api.Play.current
import play.api.db.DB

class Sample_SingleQueryService extends Service {
  def execute(ep: ElasticParam): Try[ElasticParam] = {
    Try {
      /*
       * Get a db connection
       */
      val conn: Connection = DB.getConnection("default", true)
      val sqlConn = SqlConn(conn)

      try {
        /*
         * Execute a query
         */
        val dataset = ep.getDataset("1").getOrElse(Dataset(""))
        val (errOpt, dsList) = sqlConn.query("sample.select_isEmpty", dataset.rows)

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
        /*
         * Close db connection. It actually dose not close the real
         * connection. It just put the connection back into the pool.
         */
        sqlConn.close()
      }
    }
  }
}