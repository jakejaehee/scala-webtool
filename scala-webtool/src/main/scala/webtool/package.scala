import webtool.util.ep.MkString
import webtool.util.ep.json13.JSON13MkString

package object webtool {
  implicit val epMkString: MkString = JSON13MkString

  def currentMethodName(): String = Thread.currentThread.getStackTrace()(2).getMethodName
  def traceOut(callClass: Class[_], methodName: String, id: String, outStr: String) {
    println("[%s.%s] %s -> %s".format(callClass.getName, methodName, id, outStr))
  }
}