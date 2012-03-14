/*
 * Copyright 2009-2010 LinkedIn, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.linkedin.norbert
package javacompat
package network

import com.linkedin.norbert.network.partitioned.loadbalancer.{DefaultPartitionedLoadBalancerFactory => SConsistentHashPartitionedLoadBalancerFactory}
import EndpointConversions._
import cluster.Node
import com.linkedin.norbert.network.common.{Endpoint => SEndpoint}
import java.util.{Set => JSet}

abstract class ConsistentHashPartitionedLoadBalancerFactory[PartitionedId](numPartitions: Int, serveRequestsIfPartitionMissing: Boolean = true) extends PartitionedLoadBalancerFactory[PartitionedId] {
  def this(numPartitions: Int) = this(numPartitions, true)

  val underlying = new SConsistentHashPartitionedLoadBalancerFactory[PartitionedId](serveRequestsIfPartitionMissing) {
    protected def calculateHash(id: PartitionedId) = hashPartitionedId(id)

    protected def getNumPartitions(endpoints: Set[SEndpoint]) = {
      if (numPartitions == -1) {
        endpoints.flatMap(_.getNode.getPartitionIds).size
      } else {
        numPartitions
      }
    }
  }

  def newLoadBalancer(endpoints: JSet[Endpoint]): PartitionedLoadBalancer[PartitionedId] = new PartitionedLoadBalancer[PartitionedId] {
    val loadBalancer = underlying.newLoadBalancer(endpoints)

    def nextNode(id: PartitionedId) = loadBalancer.nextNode(id).getOrElse(null)

    def nodesForOneReplica(id: PartitionedId) = {
      val map = loadBalancer.nodesForOneReplica(id)
      val jMap = new java.util.HashMap[Node, JSet[java.lang.Integer]]()
      map.foreach { case (node, partitions) =>
        val set = new java.util.HashSet[java.lang.Integer]
        partitions.foreach(set.add(_))
        jMap.put(node, set)
      }
      jMap
    }
  }

  protected def hashPartitionedId(id : PartitionedId) : Int
}