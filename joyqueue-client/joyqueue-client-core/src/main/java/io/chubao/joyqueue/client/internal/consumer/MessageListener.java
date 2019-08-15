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
package io.chubao.joyqueue.client.internal.consumer;

import io.chubao.joyqueue.client.internal.consumer.domain.ConsumeMessage;

/**
 * MessageListener
 *
 * author: gaohaoxiang
 * date: 2018/12/25
 */
public interface MessageListener extends BaseMessageListener {

    void onMessage(ConsumeMessage message);
}