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

import io.chubao.joyqueue.network.transport.codec.Codec;
import io.chubao.joyqueue.network.transport.codec.CodecFactory;
import io.chubao.joyqueue.network.transport.codec.support.JoyQueueCodec;

/**
 * JoyQueueCodecFactory
 *
 * author: gaohaoxiang
 * date: 2018/11/28
 */
public class JoyQueueCodecFactory implements CodecFactory {

    @Override
    public Codec getCodec() {
        return new JoyQueueCodec();
    }
}