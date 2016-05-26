package test.webtool.util.ep

import webtool.util.ep.{ColumnInfo, Dataset, ElasticParam, Record}
import webtool.util.ep.json12.JSON12MkString
import play.api.libs.json.Json

object ElasticParamTest {
  def main(args: Array[String]) {
    test4
  }

  def test4 = {
    val ds = Dataset("1")
    ds += Record[Any]("a"->"AA")
    println(ds.headOrElse(Record[Any]()))
    println(ds.indexOrElse(1, Record[Any]()))
  }

  def test3 = {
    println(Dataset("1").rows match {
      case h :: t => h
      case Nil => Map[String, Any]()
    })
  }

  def test2 = {
    implicit val ms = TestMkString

    val ep: ElasticParam = ElasticParam()
    val parameters = Map("a" -> "AA", "b" -> "BB")
    val colInfos = List(ColumnInfo("c1", "c1", "String", 0), ColumnInfo("c2", "c2", "String", 0))
    val rows = List(Map("c1" -> "111", "c2" -> "222"), Map("c1" -> "1112", "c2" -> "2222"))
    val ds = Dataset("1", colInfos)
    val dss = Map("ds1" -> ds, "ds2" -> ds)
    ep ++= parameters
    ep.datasets = dss
    println(ep)

    println(ep.toString(JSON12MkString))
  }

  def test1 = {
    implicit val ms = TestMkString

    val ep: ElasticParam = ElasticParam()
    val parameters = Map("a" -> "AA", "b" -> "BB")
    val colInfos = List(ColumnInfo("c1", "c1", "String", 0), ColumnInfo("c2", "c2", "String", 0))
    val rows = List(Map("c1" -> "111", "c2" -> "222"), Map("c1" -> "1112", "c2" -> "2222"))
    val ds = Dataset("1", colInfos)
    val dss = Map("ds1" -> ds, "ds2" -> ds)
    ep ++= parameters
    ep.datasets = dss
    println(ep)

    val ms2 = JSON12MkString
    val ep2 = ElasticParam()(ms2)
    val parameters2 = Map("a" -> "AA", "b" -> "BB")
    val colInfos2 = List(ColumnInfo("c1", null, "String", 0)(ms2), ColumnInfo("c2", "c2", "String", 0)(ms2))
    val rows2 = List(Map("c1" -> null, "c2" -> "222"), Map("c1" -> "1112", "c2" -> "2222"))
    val ds2 = Dataset("1", colInfos2)(ms2)
    val dss2 = Map("ds1" -> ds2, "ds2" -> ds2)
    ep2 ++= parameters2
    ep2.datasets = dss2
    println(Json.prettyPrint(Json.parse(ep2.toString)))
  }
}