/**
 * Copyright 2018 The JoyQueue Authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.chubao.joyqueue.broker.election;

import io.chubao.joyqueue.broker.cluster.ClusterManager;
import io.chubao.joyqueue.domain.TopicName;

import java.util.Set;

/**
 * Created by zhuduohui on 2018/10/19.
 */
public class ClusterManagerStub extends ClusterManager {
    public ClusterManagerStub() {
        super(null, null,null);
    }

    @Override
    public void leaderReport(TopicName topic, int partitionGroup, int leaderBrokerId, Set<Integer> isrId, Integer termId) {
    }
}