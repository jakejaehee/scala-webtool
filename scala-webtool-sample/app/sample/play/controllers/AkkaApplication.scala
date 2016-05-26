package sample.play.controllers

import scala.concurrent.Await
import scala.concurrent.duration.DurationInt
import scala.util.Failure
import scala.util.Success

import com.typesafe.scalalogging.LazyLogging

import actors.PreActor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.pattern.ask
import akka.routing.FromConfig
import akka.util.Timeout
import webtool.ServiceUtil
import webtool.util.ep.ElasticParam
import webtool.web.play.PlayReader
import javax.inject.Inject
import javax.inject.Singleton
import play.api.mvc.Action
import play.api.mvc.Controller

@Singleton
class AkkaApplication @Inject() (system: ActorSystem) extends Controller with LazyLogging {

  /*
   * o The ask pattern needs to be imported, and then this provides a ? operator on the actor.
   * o The return type of the ask is a Future[Any], usually the first thing you will want to do 
   *   after asking actor is map that to the type you are expecting, using the mapTo method.
   * o An implicit timeout is needed in scope - the ask pattern must have a timeout. If the 
   *   actor takes longer than that to respond, the returned future will be completed with a 
   *   timeout error.
   */
  private val router = system.actorOf(FromConfig.props(Props[PreActor]), "serviceRouter")
  implicit private val timeout = Timeout(5 seconds)

  def index = Action(parse.raw) { request =>
    import webtool.ServiceUtil._

    val inSrc = PlayReader(request).read
    logRequest(inSrc)

    val (resText, cTypeOpt) = tryToGetReqEP(inSrc) match {
      case Success(reqEP) =>
        val future = router ? reqEP
        val resEP = Await.result(future, timeout.duration).asInstanceOf[ElasticParam]

        val genTo = tryToGetGenTo(reqEP) match {
          case Success(genTo) => genTo
          case Failure(e) => DefaultGenTo
        }
        (genTo.gen(resEP), genTo.contentType)

      case Failure(e) => (DefaultGenTo.gen(epWithCodeMessage(999, e.toString)),
        DefaultGenTo.contentType)
    }

    var result = cTypeOpt match {
      case Some(contentType) => Ok(resText).as(contentType)
      case None => Ok(resText)
    }

    logResponse(resText, cTypeOpt)
    result
  }
}
