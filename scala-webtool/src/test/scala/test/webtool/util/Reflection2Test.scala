package test.webtool.util

import webtool.util.ReflectionUtil

object Reflection2Test {
  def main(args: Array[String]) {
    println("[" + ReflectionUtil.newInstance(classOf[String], Nil) + "]")
  }
}
