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
package io.chubao.joyqueue.broker.mqtt.handler;

import io.netty.channel.Channel;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import io.netty.handler.codec.mqtt.MqttPubAckMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;

/**
 * @author majun8
 */
public class PublishAckHandler extends Handler implements ExecutorsProvider {
    private static final Logger logger = LoggerFactory.getLogger(PublishAckHandler.class);

    @Override
    public void handleRequest(Channel client, MqttMessage message) throws Exception {
        MqttPubAckMessage pubAckMessage = (MqttPubAckMessage) message;

        mqttProtocolHandler.processPubAck(client, pubAckMessage);
    }

    @Override
    public MqttMessageType type() {
        return MqttMessageType.PUBACK;
    }

    @Override
    public ExecutorService getExecutorService() {
        return mqttContext.getExecutorServiceMap().get(PublishAckHandler.class);
    }
}
