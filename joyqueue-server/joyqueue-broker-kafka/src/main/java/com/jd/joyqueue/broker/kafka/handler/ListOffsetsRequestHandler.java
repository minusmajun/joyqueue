/**
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
package com.jd.joyqueue.broker.kafka.handler;


import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jd.joyqueue.broker.BrokerContext;
import com.jd.joyqueue.broker.BrokerContextAware;
import com.jd.joyqueue.broker.cluster.ClusterManager;
import com.jd.joyqueue.broker.kafka.KafkaCommandType;
import com.jd.joyqueue.broker.kafka.KafkaErrorCode;
import com.jd.joyqueue.broker.kafka.command.ListOffsetsRequest;
import com.jd.joyqueue.broker.kafka.command.ListOffsetsResponse;
import com.jd.joyqueue.domain.PartitionGroup;
import com.jd.joyqueue.domain.TopicName;
import com.jd.joyqueue.network.transport.Transport;
import com.jd.joyqueue.network.transport.command.Command;
import com.jd.joyqueue.store.PartitionGroupStore;
import com.jd.joyqueue.store.StoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * ListOffsetsRequestHandler
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/11/5
 */
public class ListOffsetsRequestHandler extends AbstractKafkaCommandHandler implements BrokerContextAware {

    protected static final Logger logger = LoggerFactory.getLogger(ListOffsetsRequestHandler.class);

    private static final long EARLIEST_TIMESTAMP = -2L;
    private static final long LATEST_TIMESTAMP = -1L;

    private ClusterManager clusterManager;
    private StoreService storeService;

    @Override
    public void setBrokerContext(BrokerContext brokerContext) {
        this.clusterManager = brokerContext.getClusterManager();
        this.storeService = brokerContext.getStoreService();
    }

    @Override
    public Command handle(Transport transport, Command command) {
        ListOffsetsRequest request = (ListOffsetsRequest) command.getPayload();
        Map<String, List<ListOffsetsRequest.PartitionOffsetRequest>> partitionRequestMap = request.getPartitionRequests();
        Map<String, List<ListOffsetsResponse.PartitionOffsetResponse>> partitionResponseMap = Maps.newHashMapWithExpectedSize(partitionRequestMap.size());

        for (Map.Entry<String, List<ListOffsetsRequest.PartitionOffsetRequest>> entry : partitionRequestMap.entrySet()) {
            TopicName topicName = TopicName.parse(entry.getKey());
            List<ListOffsetsResponse.PartitionOffsetResponse> partitionResponses = Lists.newArrayListWithCapacity(entry.getValue().size());
            for (ListOffsetsRequest.PartitionOffsetRequest partitionOffsetRequest : entry.getValue()) {
                ListOffsetsResponse.PartitionOffsetResponse partitionOffsetResponse = getOffsetByTimestamp(topicName, partitionOffsetRequest.getPartition(), partitionOffsetRequest.getTime());
                partitionResponses.add(partitionOffsetResponse);
            }
            partitionResponseMap.put(entry.getKey(), partitionResponses);
        }

        // TODO 临时日志
        logger.info("list offset, request: {}, response: {}", partitionRequestMap, partitionResponseMap);
        ListOffsetsResponse response = new ListOffsetsResponse(partitionResponseMap);
        return new Command(response);
    }

    private ListOffsetsResponse.PartitionOffsetResponse getOffsetByTimestamp(TopicName topic, int partition, long timestamp) {
        try {
            PartitionGroup partitionGroup = clusterManager.getPartitionGroup(topic, (short) partition);
            if (partitionGroup == null) {
                logger.error("list offset error, partitionGroup not exist, topic: {}, partition: {}", topic, partition);
                return new ListOffsetsResponse.PartitionOffsetResponse(partition, KafkaErrorCode.NOT_LEADER_FOR_PARTITION.getCode(), timestamp, -1);
            }

            long offset = 0L;
            PartitionGroupStore partitionGroupStore = storeService.getStore(topic.getFullName(), partitionGroup.getGroup());

            if (timestamp == LATEST_TIMESTAMP) {
                // 得到partition的最大位置
                offset = partitionGroupStore.getRightIndex((short) partition);
            } else if (timestamp == EARLIEST_TIMESTAMP) {
                offset = partitionGroupStore.getLeftIndex((short) partition);
            } else {
                offset = partitionGroupStore.getIndex((short) partition, timestamp);
            }

            return new ListOffsetsResponse.PartitionOffsetResponse(partition, KafkaErrorCode.NONE.getCode(), timestamp, offset);
        } catch (Exception e) {
            logger.error("list offset exception, topic: {}, partition: {}", topic, partition, e);
            short errorCode = KafkaErrorCode.exceptionFor(e);
            return new ListOffsetsResponse.PartitionOffsetResponse(partition, errorCode, timestamp, -1);
        }
    }

    @Override
    public int type() {
        return KafkaCommandType.LIST_OFFSETS.getCode();
    }
}
