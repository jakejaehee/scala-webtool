package scalacheck.elasticparam.util.ep

import org.scalacheck.Gen
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Prop
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

import webtool.epMkString
import webtool.util.ep.ElasticParam
import webtool.util.ep.Record
import scalacheck._

   
object RecordTest extends Properties("Record"){
  
  property("apply") = forAll {
    (s: String, t: AnyVal) => 
      val rec = Record(Map(s->t))
      
      rec(s) == t
  }
  
  property("++=") = forAll (genRecord, Gen.alphaStr, Gen.posNum[Int]){
    (rec: Record[Any], s: String, t: AnyVal) =>
      rec ++= Map(s->t)
      
      rec(s) == t
  }
  
  property("+=") = forAll (genRecord, Gen.alphaStr, Gen.posNum[Int]){
    (rec: Record[Any], s: String, t: AnyVal) =>
      rec.+=(s, t)
      
      rec(s) == t
  }
  
  property("get") = forAll {
    (s: String, t: AnyVal) =>
      val rec = Record(Map(s->t))
      rec.get(s) == Some(t)
  }
  
}