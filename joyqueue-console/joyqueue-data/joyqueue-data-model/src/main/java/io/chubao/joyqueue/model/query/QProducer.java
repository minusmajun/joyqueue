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
package io.chubao.joyqueue.model.query;

import io.chubao.joyqueue.model.QKeyword;
import io.chubao.joyqueue.model.domain.Identity;
import io.chubao.joyqueue.model.domain.Topic;

import java.util.List;

public class QProducer extends QKeyword implements QIdentity {
    private Topic topic;
    private Identity app;
    private List<String> appList;

    public QProducer() {

    }

    public QProducer(Topic topic) {
        this.topic = topic;
    }

    public QProducer(Identity app) {
        this.app = app;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Identity getApp() {
        return app;
    }

    public void setApp(Identity app) {
        this.app = app;
    }

    public List<String> getAppList() {
        return appList;
    }

    public void setAppList(List<String> appList) {
        this.appList = appList;
    }
}
