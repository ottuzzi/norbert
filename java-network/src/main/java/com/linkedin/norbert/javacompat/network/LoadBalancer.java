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
package com.linkedin.norbert.javacompat.network;

import com.linkedin.norbert.javacompat.cluster.Node;

/**
 * A <code>LoadBalancer</code> handles calculating the next <code>Node</code> a request should be routed to.
 */
public interface LoadBalancer {
  /**
   * Returns the next <code>Node</code> a request should be routed to.
   *
   * @return the <code>Node</code> to route the next request to or null if there are no <code>Node</code>s available
   */
  Node nextNode();

  /**
   * Returns the next <code>Node</code> that fulfill the capability a request should be routed to.
   *
   * @param capability A Long that representing the minimal capability of the node that's serving the request
   * @return the <code>Node</code> to route the next request to or null if there are no <code>Node</code>'s available
   */
  Node nextNode(Long capability);

  /**
   * Returns the next <code>Node</code> that fulfill the capability a request should be routed to.
   *
   * @param capability A Long that representing the minimal capability of the node that's serving the request
   * @param persistentCapability A Long that represents the minimal persistent capability of the node 
   * @return the <code>Node</code> to route the next request to or null if there are no <code>Node</code>'s available
   */
  Node nextNode(Long capability, Long persistentCapability);
}
