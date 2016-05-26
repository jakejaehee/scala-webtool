package actors

import scala.concurrent.duration.DurationInt
import akka.actor.Actor
import akka.actor.ActorLogging
import akka.actor.ActorSystem
import akka.actor.actorRef2Scala
import akka.cluster.Cluster
import akka.cluster.ddata.DistributedData
import akka.cluster.ddata.GSet
import akka.cluster.ddata.GSetKey
import akka.cluster.ddata.Replicator.Changed
import akka.cluster.ddata.Replicator.Get
import akka.cluster.ddata.Replicator.GetFailure
import akka.cluster.ddata.Replicator.GetSuccess
import akka.cluster.ddata.Replicator.NotFound
import akka.cluster.ddata.Replicator.ReadMajority
import akka.cluster.ddata.Replicator.Subscribe
import akka.event.LoggingReceive
import webtool.util.ep.{Dataset, ElasticParam, Record}
import webtool.util.web.HttpClient

object AActor extends App {
  val system = ActorSystem("ClusterSystem")
}

class AActor extends Actor with ActorLogging {
  import AActor._

  val TopicsKey = GSetKey[ElasticParam]("topics")
  val replicator = DistributedData(system).replicator
  implicit val node = Cluster(system)

  private def getFromDD =
    replicator ! Get(TopicsKey, ReadMajority(timeout = 5.seconds))

  private def subscribeDD =
    replicator ! Subscribe(TopicsKey, self)

  getFromDD

  def receive = LoggingReceive {
    case g @ GetSuccess(key, req) if key == TopicsKey =>
      val data = g.get(TopicsKey).elements.toSeq
      subscribeDD
      context become afterTopics
    case NotFound(_, _) =>
      subscribeDD
      context become afterTopics
    case GetFailure(key, req) if key == TopicsKey =>
      getFromDD
  }

  def afterTopics = LoggingReceive {
    case c @ Changed(key) if key == TopicsKey =>
      c.get[GSet[ElasticParam]](TopicsKey).elements.foreach { ep =>
        ep.get("type").foreach {
            case "wms" =>
              val hc = HttpClient("localhost", 9091, "/", Some("UTF-8"))
              val params = ep.get("action") match {
                case Some("in") => Map[String, Any]("service"->"wms.es.service.WmsInService")
                case Some("out") => Map[String, Any]("service"->"wms.es.service.WmsOutService")
                case _ => Map[String, Any]()
              }
              // Map("S1COL_POSITION"->"S1", "S1COL_ITEM"->"P1", "S1COL_WORKER"->"jake", "S1COL_MODE"->"IN" | "OUT")
              val record = ep.getDataset("1").getOrElse(Dataset("1")).headOrElse(Record[Any]())
              hc.reqAndGetRes("POST", params, record)
            case "performance" =>
            case _ =>
        }
      }
  }
}