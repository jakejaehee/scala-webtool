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

class Sample_TransactionQueryService extends Service {
  def execute(ep: ElasticParam): Try[ElasticParam] = {
    Try {
      /*
			 * Get a db connection
			 */
      val conn: Connection = DB.getConnection("default", false)

      val sqlConn = SqlConn(conn)

      try {
        sqlConn.startTransaction()

        /*
				 * Execute the first query
				 */
        val (errOpt1, dsList1) = sqlConn.query("sample.update_insert", ep.getDataset("1").getOrElse(Dataset("")).rows)

        /*
				 * Execute the second query
				 */
        val (errOpt2, dsList2) = sqlConn.query("sample.update_update", ep.getDataset("2").getOrElse(Dataset("")).rows)

        /*
         * Transaction commits
         */
        sqlConn.commit()

        val resEP = ElasticParam()
        errOpt1.foreach(e => ServiceUtil.setCodeAndMsg(resEP, 999, e.getMessage))
        errOpt2.foreach(e => ServiceUtil.setCodeAndMsg(resEP, 999, e.getMessage))

        var i = 1
        (dsList1 ++ dsList2).foreach { ds =>
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