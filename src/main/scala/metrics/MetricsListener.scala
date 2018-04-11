package metrics

import akka.actor.{Actor, ActorLogging}
import akka.cluster.Cluster
import akka.cluster.metrics.StandardMetrics.{Cpu, HeapMemory}
import akka.cluster.metrics.{ClusterMetricsChanged, ClusterMetricsExtension, NodeMetrics}

/**
  *
  * Created by ZJK on 2017/8/25.
  */
class MetricsListener extends Actor with ActorLogging{

  private val selfAddress = Cluster(context.system).selfAddress

  private val extension = ClusterMetricsExtension(context.system)


  override def preStart(): Unit = extension.subscribe(self)

  override def postStop(): Unit = extension.unsubscribe(self)

  override def receive: Receive = {
    case ClusterMetricsChanged(clusterMetrics) => {
      clusterMetrics.filter(_.address == selfAddress) foreach {
        nodeMetrics => {
          logCpu(nodeMetrics)
          logHeap(nodeMetrics)
        }
      }
    }
    case _ => ""
  }

  def logHeap(nodeMetrics: NodeMetrics): Unit = nodeMetrics match {
    case HeapMemory(address, timestamp, used, committed, max) =>
//      log.info("{} Used heap: {} MB", selfAddress.port.get,used.doubleValue / 1024 / 1024)
    case _ => // No heap info.
  }

  def logCpu(nodeMetrics: NodeMetrics): Unit = nodeMetrics match {
    case Cpu(address, timestamp, Some(systemLoadAverage), cpuCombined, cpuStolen, processors) =>
      log.info("{} Load: {} ({} processors)", selfAddress.port.get, systemLoadAverage, processors)
    case Cpu(address,timestamp,systemLoadAverage,cpuCombined,cpuStolen,processors) => {
      log.info("{},{},{}", address, systemLoadAverage,processors)
    }
    case _ => // No cpu info.
  }
}



















