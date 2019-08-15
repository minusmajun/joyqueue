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
package io.openmessaging.joyqueue.consumer.support;

import io.openmessaging.consumer.BatchMessageListener;
import io.openmessaging.consumer.MessageReceipt;

/**
 * BatchMessageListenerContextAdapter
 *
 * author: gaohaoxiang
 * date: 2019/2/20
 */
public class BatchMessageListenerContextAdapter implements BatchMessageListener.Context {

    private boolean ack = false;

    @Override
    public void success(MessageReceipt... messages) {
    }

    @Override
    public void ack() {
        ack = true;
    }

    public boolean isAck() {
        return ack;
    }
}