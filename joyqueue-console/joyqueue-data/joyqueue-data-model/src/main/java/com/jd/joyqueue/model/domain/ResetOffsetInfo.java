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
package com.jd.joyqueue.model.domain;

import java.util.List;

public class ResetOffsetInfo {
    private Subscribe subscribe;
    private List<PartitionOffset> partitionOffsets;

    public Subscribe getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Subscribe subscribe) {
        this.subscribe = subscribe;
    }

    public List<PartitionOffset> getPartitionOffsets() {
        return partitionOffsets;
    }

    public void setPartitionOffsets(List<PartitionOffset> partitionOffsets) {
        this.partitionOffsets = partitionOffsets;
    }
}