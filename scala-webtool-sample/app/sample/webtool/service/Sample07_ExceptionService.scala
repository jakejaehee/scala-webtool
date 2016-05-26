package sample.webtool.service

import scala.util.Try

import webtool.Service
import webtool.util.ep.ElasticParam

class Sample07_ExceptionService extends Service {
  def execute(reqEP: ElasticParam): Try[ElasticParam] = {
    Try {
      throw new Exception("Exception test")
    }
  }
}