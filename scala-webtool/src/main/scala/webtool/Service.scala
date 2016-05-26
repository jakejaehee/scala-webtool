package webtool

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.util.ep.ElasticParam
import webtool.util.ep.Record

object Service {
  val ScopeKey_ReqTime = "reqTime"
}

abstract class Service extends LazyLogging {

  class Session {
    private val ss = Record.empty[String]
    private var del = Set.empty[String]

    def contains(key: String): Boolean = ss.contains(key)
    def get(key: String): Option[String] = ss.get(key)
    def all = ss
    def +=(kv: (String, String)) { ss += kv }
    def ++=(m: Map[String, String]) { ss ++= m }
    def -=(key: String) {
      ss -= key
      del += key
    }
    def clear() { ss.clear() }

    def size = ss.size
    def isEmpty = ss.size == 0
    def deleted = del

    def setAccessibleService[T <: Service](svcClass: Class[T]) {
      +=(("_SVC_" + svcClass.getName) -> "true")
    }

    def setNotAccessibleService[T <: Service](svcClass: Class[T]) {
      -=("_SVC_" + svcClass.getName)
    }

    def isAccessibleService[T <: Service](svcClass: Class[T]): Boolean = {
      get("_SVC_" + svcClass.getName) match {
        case Some(b) => b.toBoolean
        case _       => false
      }
    }

    def isNotAccessibleService[T <: Service](svcClass: Class[T]): Boolean =
      !isAccessibleService(svcClass)
  }

  val session = new Session()

  private val scopeOfRequest = Record.empty[Any]

  def getScopeOfRequest(key: String): Option[Any] = scopeOfRequest.get(key)
  def setScopeOfRequest(kv: (String, Any)) { scopeOfRequest += kv }
  def delScopeOfRequest(key: String) { scopeOfRequest -= key }

  def execute(req: ElasticParam): Try[ElasticParam]

  def ifMissingThrow(paramName: String, ep: ElasticParam): Option[Throwable] = {
    if (ep.isNotEmpty(paramName)) None
    else Some(new Exception("parameter '" + paramName + "' is required"))
  }

  def ifNotAccessibleThrow(): Option[Throwable] = {
    if (!session.isNotAccessibleService(getClass)) None
    else Some(new Exception("Access denied to " + getClass.getName))
  }

  def ifFailThrow(t: Try[_]): Option[Throwable] = {
    t match {
      case Success(a) => None
      case Failure(e) => Some(e)
    }
  }
}