package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.epMkString
import webtool.service.QueryService
import webtool.service.sqlrepo.AllIdsFromDisk
import webtool.service.sqlrepo.AllPkgIdsFromDisk
import webtool.service.sqlrepo.GetXML
import webtool.service.sqlrepo.RemoveSql
import webtool.service.sqlrepo.RenamePkg
import webtool.service.sqlrepo.SaveSql
import webtool.service.sqlrepo.TemplatePkgIds
import webtool.service.sqlrepo.TemplateSqlIds
import webtool.util.ep.ElasticParam

class LoginService extends Service {
  def execute(req: ElasticParam): Try[ElasticParam] = {
    Try {
      ifMissingThrow("id", req).foreach(throw _)
      ifMissingThrow("pw", req).foreach(throw _)

      val id = req.get("id").getOrElse("").toString
      val pw = req.get("pw").getOrElse("").toString

      session.setAccessibleService(classOf[QueryService])
      session.setAccessibleService(classOf[AllIdsFromDisk])
      session.setAccessibleService(classOf[AllPkgIdsFromDisk])
      session.setAccessibleService(classOf[GetXML])
      session.setAccessibleService(classOf[RemoveSql])
      session.setAccessibleService(classOf[RenamePkg])
      session.setAccessibleService(classOf[SaveSql])
      session.setAccessibleService(classOf[TemplatePkgIds])
      session.setAccessibleService(classOf[TemplateSqlIds])
      session.setAccessibleService(classOf[TextFileReaderService])

      val res = ElasticParam()
      res += "code" -> 0
      res += "message" -> "logged in successfully"
      res ++= session.all
      res
    }
  }
}