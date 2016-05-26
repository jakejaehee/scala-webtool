package scalacheck.elasticparam.util.ep

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.BooleanOperators
import org.scalacheck.Prop.AnyOperators
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.propBoolean
import org.scalacheck.Properties
import webtool.util.ep.ColumnInfo
import webtool.util.ep.Dataset
import webtool.util.ep.ElasticParam
import scalacheck.arbColumnInfo
import scalacheck.arbDataset
import scalacheck.arbElasticParam
import scalacheck.genAlphaUpperString
import scalacheck.genElasticParam
import org.scalacheck.Arbitrary

object ElasticParamTest extends Properties("ElasticParam") {
	private val genUpperKeyMap = Gen.mapOf (
		for {
			k <- genAlphaUpperString
			//v <- arbitrary[AnyVal]
		} yield (k -> new Object)
	)
	
	property("++=") = forAll (genElasticParam, genUpperKeyMap) {
		(ep, map) =>
			ep ++= map
			
			s"ep.parameters : $ep.parameters" |: map == ep.parameters
  }
	
	implicit val arbAny: Arbitrary[Any] = Arbitrary(Gen.const(new Object))
	
	property("+=") = forAll { (ep: ElasticParam, param: (String, Any)) =>
		ep += param
		
		Map(param) == ep.parameters
  }
	
	property("-=") = forAll { (ep: ElasticParam, param: (String, Any)) =>
		ep += param
		ep -= param._1
		
		ep.parameters.size ?= 0
  }
	
	property("get") = forAll { (ep: ElasticParam, param: (String, Any)) =>
		ep += param
		
		ep.get(param._1).get ?= param._2
  }
	
	property("contains") = forAll { (ep: ElasticParam, param: (String, Any)) =>
		ep += param
		
		ep.contains(param._1)
  }
	
	property("isEmpty") = forAll { (ep: ElasticParam) =>
		ep += ("a" -> List())
		ep += ("b" -> Array())
		ep += ("c" -> "")
		ep += ("d" -> "   ")
		
		Prop.all(
			ep.isEmpty("a"),
			ep.isEmpty("b"),
			ep.isEmpty("c"),
			ep.isEmpty("d"),
			ep.isEmpty("111")
		)
  }
	
//	val genDatasetList = Gen.listOfN(10, genDataset)
	
	property("datasetsToList") = forAll { 
		(ep: ElasticParam, datasets: List[Dataset]) =>
			ep.datasets_=(datasets)
			val dsList = ep.datasetsToList
			
			Prop.atLeastOne(
				Prop.lzy(datasets.size == dsList.size),
				Prop.lzy(datasets.forall { x1 => dsList.exists { x2 => x1.name == x2.name } })
			)
  }
	
	property("datasets_=(datasets: Map[String, Dataset])") = forAll { 
		(ep: ElasticParam, datasets: Map[String, Dataset]) =>
			ep.datasets_=(datasets)
			
			ep.datasets.size ?= datasets.size
  }
	
	property("setDatasetRows") = forAll //(genElasticParam, Gen.alphaStr, Gen.listOfN(10, genMap))
	{ 
		(ep: ElasticParam, dsName: String, rows: List[Map[String, Any]]) =>
			ep.setDatasetRows(dsName, rows)
			
			val ds = ep.getDataset(dsName)
			
			val rowsCount = rows.size
			
			Prop.all(
				ds != None,
				ds.get.rows.size == rowsCount
			)
  }
	
	property("addDatasetRows") = forAll { 
		(ep: ElasticParam, dsName: String, rows: List[Map[String, Any]]) =>
			ep.addDatasetRows(dsName, rows)
			
			val ds = ep.getDataset(dsName)
			
			val rowsCount = rows.size
			
			Prop.all(
				"ds is None" |: ds != None,
				s"ds.get.rows.size is ${ds.get.rows.size}, rowsCount is ${rowsCount}" |: ds.get.rows.size == rowsCount
			)
  }

	property("addDatasetRow") = forAll { 
		(ep: ElasticParam, dsName: String, row: Map[String, Any]) =>
			ep.addDatasetRow(dsName, row)
			
			ep.getDataset(dsName).map {
				_.rows.exists { r => r == row }
			}.getOrElse(false)
  }
	
	property("addDatasetColumn") = forAll { 
		(ep: ElasticParam, dsName: String, colInfo: ColumnInfo, value: Any) =>
			ep.addDatasetColumn(dsName, colInfo, value)
			
			ep.getDataset(dsName).map {
				_.colInfos.exists { x => x == colInfo }
			}.getOrElse(false)
  }
}