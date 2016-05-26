package scalacheck.elasticparam

import java.io.File

import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.AnyOperators
import org.scalacheck.Prop.forAll
import org.scalacheck.Properties

import webtool.Configurator

object ConfiguratorTest extends Properties("Configurator") {
  //	val parent = "C:/Dev/mars_workspace/webtool-sample-0.1/esConfig"
  val parent = "../webtool-sample-0.1/esConfig"
  val genFile = Gen.const(new File(parent + File.separator + "webtool.xml"))

  property("init") = forAll(genFile) {
    (file) =>
      Configurator.init(file, None)

      val xml = Configurator.getXml

      Prop.all(
        Configurator.Charset ?= Some("UTF-8"),
        (xml \ "sqlRepo").text ?= "sqlrepo")
  }
}