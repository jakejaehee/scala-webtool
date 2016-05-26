package test.webtool.util.ep

import java.io.File
import webtool.util.ep.EPUtil
import webtool.util.ep.FileReader

object DetectFormatTest {
  def main(args: Array[String]) {
    var file = new File("C:/Dev/scala_sdk_workspace/elastic-scala-1.5.1/테스트/ep.json")
    var meta = FileReader(file, None).read()

    println(meta.text)
    println(EPUtil.detectGenFrom(meta.contentType, meta.text))

    file = new File("C:/Dev/mars_workspace/elastic-scala-1.5.1/pom.xml")
    meta = FileReader(file, None).read()

    println(meta.text)
    println(EPUtil.detectGenFrom(meta.contentType, meta.text))
  }
}