package test.webtool.util

import webtool.util.ReflectionUtil

object ReflectionUtilTest {
  def main(args: Array[String]) {
    val b = newInstanceTest("elastic.web.service.EchoService", Nil)
    println(b)
  }
  
  def newInstanceTest(className: String, classArgs: List[AnyRef]): Any =
  	ReflectionUtil.newInstance(className, classArgs)
}