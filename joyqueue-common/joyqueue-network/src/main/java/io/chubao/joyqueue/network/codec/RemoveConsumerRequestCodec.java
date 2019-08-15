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
package io.chubao.joyqueue.network.codec;

import com.google.common.collect.Lists;
import io.chubao.joyqueue.network.command.JoyQueueCommandType;
import io.chubao.joyqueue.network.command.RemoveConsumerRequest;
import io.chubao.joyqueue.network.serializer.Serializer;
import io.chubao.joyqueue.network.transport.codec.JoyQueueHeader;
import io.chubao.joyqueue.network.transport.codec.PayloadCodec;
import io.chubao.joyqueue.network.transport.command.Type;
import io.netty.buffer.ByteBuf;

import java.util.List;


/**
 * RemoveConsumerRequestCodec
 *
 * author: gaohaoxiang
 * date: 2018/12/10
 */
public class RemoveConsumerRequestCodec implements PayloadCodec<JoyQueueHeader, RemoveConsumerRequest>, Type {

    @Override
    public RemoveConsumerRequest decode(JoyQueueHeader header, ByteBuf buffer) throws Exception {
        RemoveConsumerRequest removeConsumerRequest = new RemoveConsumerRequest();

        short topicSize = buffer.readShort();
        List<String> topics = Lists.newArrayListWithCapacity(topicSize);
        for (int i = 0; i < topicSize; i++) {
            topics.add(Serializer.readString(buffer, Serializer.SHORT_SIZE));
        }

        removeConsumerRequest.setTopics(topics);
        removeConsumerRequest.setApp(Serializer.readString(buffer, Serializer.SHORT_SIZE));
        return removeConsumerRequest;
    }

    @Override
    public void encode(RemoveConsumerRequest payload, ByteBuf buffer) throws Exception {
        buffer.writeShort(payload.getTopics().size());
        for (String topic : payload.getTopics()) {
            Serializer.write(topic, buffer, Serializer.SHORT_SIZE);
        }
        Serializer.write(payload.getApp(), buffer, Serializer.SHORT_SIZE);
    }

    @Override
    public int type() {
        return JoyQueueCommandType.REMOVE_CONSUMER_REQUEST.getCode();
    }
}