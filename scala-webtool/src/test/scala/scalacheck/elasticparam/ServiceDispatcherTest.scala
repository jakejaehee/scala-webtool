package scalacheck.elasticparam

import org.scalacheck.Properties
import org.scalacheck.Gen
import org.scalacheck.Prop.forAll
import org.scalacheck.Prop.AnyOperators
import webtool.ServiceDispatcher
import webtool.util.ep.ElasticParam
import webtool.ParamKey

object ServiceDispatcherTest extends Properties("ServiceDispatcher") {
	val genServiceName = Gen.oneOf(
			"webtool.service.sqlrepo.AllIdsFromDisk",
			"webtool.service.sqlrepo.AllPkgIdsFromDisk",
			"webtool.service.sqlrepo.GetXML",
			"webtool.service.sqlrepo.RemoveSql",
			"webtool.service.sqlrepo.RenamePkg",
			"webtool.service.sqlrepo.SaveSql",
			"webtool.service.sqlrepo.TemplatePkgIds",
			"webtool.service.sqlrepo.TemplateSqlIds",
			"webtool.service.EchoService",
			"webtool.service.ErrorService",
			"webtool.service.QueryService"
	)
	
  property("loadService") = forAll (genServiceName) { 
		(serviceName) =>
			val ep = ElasticParam()
			ep += (ParamKey.KEY_SERVICE, serviceName)
			
			ServiceDispatcher.loadService(serviceName).get ?= ServiceDispatcher.loadService(ep).get
  }
}