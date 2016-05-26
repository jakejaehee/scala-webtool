

import webtool.util.ReflectionUtil

object ReflectionUtilTest {
  def main (args: Array[String]) {
    val className = "sample.webtool.service.TextFileReaderService"
    val clazz = Class.forName(className)
    println(clazz)
    
    val o = ReflectionUtil.newInstance(className, Nil)
    println(o)
  }
}