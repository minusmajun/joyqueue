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
package io.chubao.joyqueue.model.domain;

public enum MqttProxyThreadType implements EnumItem {

    CONSUME(1,"consume"),
    DELIVERY(2,"delivery");
    private int value;
    private String description;
    MqttProxyThreadType(int value, String description){
       this.value=value;
       this.description=description;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public String description() {
        return description;
    }
}
