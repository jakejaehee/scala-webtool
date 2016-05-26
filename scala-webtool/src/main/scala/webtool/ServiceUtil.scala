package webtool

import scala.util.Failure
import scala.util.Success
import scala.util.Try

import com.typesafe.scalalogging.LazyLogging

import webtool.util.ep.ElasticParam
import webtool.util.ep.EPUtil
import webtool.util.ep.GenTo
import webtool.util.ep.InputSourceMeta
import webtool.util.ep.json13.GenToJSON13

object ServiceUtil extends LazyLogging {
  def logRequest(inSrc: InputSourceMeta) {
    logger.info("request[" + inSrc.contentType + "]: " + inSrc.text)
  }

  def logResponse(resText: String, cTypeOpt: Option[String]) {
    logger.info("response[" + cTypeOpt.getOrElse("") + "]: " + resText)
  }

  def tryToGetReqEP(inSrc: InputSourceMeta): Try[ElasticParam] =
    Try {
      EPUtil.detectGenFrom(inSrc.contentType, inSrc.text).
        getOrElse(throw new Exception("unknown request format")).
        gen(inSrc.text, inSrc.encodingOpt)
    }

  def tryToGetGenTo(reqEP: ElasticParam): Try[GenTo] =
    ServiceUtil.getResponseType(reqEP).
      orElse(Try { "json13" }).
      flatMap { EPUtil.tryToDetectGenTo }

  val DefaultGenTo = GenToJSON13

  def execService(inSrc: InputSourceMeta, ssMap: Map[String, String]): (Try[Service], String, Option[String]) = {
    tryToGetReqEP(inSrc) match {
      case Success(reqEP) =>
        val svc = ServiceDispatcher.loadService(reqEP)
        svc.foreach { s => s.session ++= ssMap }

        val genTo = tryToGetGenTo(reqEP) match {
          case Success(genTo) => genTo
          case Failure(e) => DefaultGenTo
        }

        svc.flatMap { s => s.execute(reqEP) } match {
          case Success(resEP) => (svc, genTo.gen(resEP), genTo.contentType)
          case Failure(e) => (svc, genTo.gen(ServiceUtil.epWithCodeMessage(999, e.toString)), genTo.contentType)
        }
      case Failure(e) => (new Failure(e), DefaultGenTo.gen(ServiceUtil.epWithCodeMessage(999, e.toString)), DefaultGenTo.contentType)
    }
  }

  /**
   * Http 요청으로부터 파라미터 'service'의 값을 구한다.
   */
  def getServiceName(req: ElasticParam): Try[String] = {
    req.get(ParamKey.KEY_SERVICE) match {
      case Some(svcName) => Success(svcName.asInstanceOf[String])
      case None => Failure(new Exception("missing parameter '" + ParamKey.KEY_SERVICE + "'"))
    }
  }

  /**
   * Http 요청으로부터 파라미터 'service.resType'의 값을 구한다.
   */
  def getResponseType(req: ElasticParam): Try[String] = {
    req.get(ParamKey.KEY_SERVICE_RES_TYPE) match {
      case Some(resType) => Success(resType.asInstanceOf[String])
      case None => Failure(new Exception("missing parameter '" + ParamKey.KEY_SERVICE_RES_TYPE + "'"))
    }
  }

  def getCode(ep: ElasticParam): Int = ep.get("code").getOrElse("0").toString.toInt

  def getMessage(ep: ElasticParam): String = ep.get("message").getOrElse("").toString

  def setCodeAndMsg(ep: ElasticParam, code: Int, message: String) {
    ep += "code" -> code
    ep += "message" -> message
  }
  /*
  def getElasticParam(svcTry: Try[ElasticParam]): ElasticParam = {
    svcTry match {
      case Success(res) => res
      case Failure(e) =>
        logger.error(ExceptionDetail.getDetail(e))
        epWithCodeMessage(999, e.getMessage)
    }
  }
*/
  def epWithCodeMessage(code: Int, message: String): ElasticParam = {
    val ep = ElasticParam() //ElasticParam.empty
    ServiceUtil.setCodeAndMsg(ep, code, message)
    ep
  }
}