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
import akka.cluster.ddata.Replicator.FlushChanges
import akka.cluster.ddata.Replicator.Get
import akka.cluster.ddata.Replicator.GetFailure
import akka.cluster.ddata.Replicator.GetSuccess
import akka.cluster.ddata.Replicator.NotFound
import akka.cluster.ddata.Replicator.ReadMajority
import akka.cluster.ddata.Replicator.Subscribe
import akka.cluster.ddata.Replicator.Update
import akka.cluster.ddata.Replicator.WriteLocal
import akka.event.LoggingReceive
import webtool.util.ep.ElasticParam

object PreActor extends App {
  val system = ActorSystem("ClusterSystem")
}

class PreActor extends Actor with ActorLogging {
  import PreActor._

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
      val data = c.get[GSet[ElasticParam]](TopicsKey).elements.toSeq

    case ep: ElasticParam =>
      log.info(s"recv reqEP: $ep")

      replicator ! Update(TopicsKey, GSet.empty[ElasticParam], WriteLocal) {
        // set => set + ep
        set => GSet.empty[ElasticParam] + ep
      }
      replicator ! FlushChanges

      sender() ! ep
      log.info(s"sent resEP: $ep")
  }
}