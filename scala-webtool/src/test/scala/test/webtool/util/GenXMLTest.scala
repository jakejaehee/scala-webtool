package test.webtool.util

import webtool.util.GenXML

object GenXMLTest {
  def main(args: Array[String]) {
    val x = GenXML 
    GenXML.addXmlEnv(Map("a" -> "A", "b" -> "B"))
    GenXML.addXmlEnv(Map("aa" -> "A", "b" -> "C"))
    println(x)
  }
}