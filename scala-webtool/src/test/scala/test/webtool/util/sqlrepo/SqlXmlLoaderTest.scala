package test.webtool.util.sqlrepo

import java.io.File
import scala.util.Failure
import scala.util.Success
import webtool.util.GenXML
import webtool.util.sqlrepo.SqlXmlLoader

object SqlXmlLoaderTest {
  def main(args: Array[String]) {
    val p = "D:\\Dev\\workspace\\webtool\\esConfig\\sqlrepo\\sample\\select_isEmpty.xml"
    GenXML.toString(new File(p), None) match {
      case Success(xmlStr) => 
        val sqlTry = new SqlXmlLoader().loadString(xmlStr)
        sqlTry.foreach { sql => print(sql.toString()) }
      case Failure(e) => 
        print(e)
    }
  }
}