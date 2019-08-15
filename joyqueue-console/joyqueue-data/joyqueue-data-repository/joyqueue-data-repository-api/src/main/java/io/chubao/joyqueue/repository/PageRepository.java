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
package io.chubao.joyqueue.repository;

import io.chubao.joyqueue.model.domain.BaseModel;
import io.chubao.joyqueue.model.exception.RepositoryException;
import io.chubao.joyqueue.model.ListQuery;
import io.chubao.joyqueue.model.PageResult;
import io.chubao.joyqueue.model.QPageQuery;
import io.chubao.joyqueue.model.Query;

import java.util.List;

/**
 * 仓库公共接口（带分页）
 * Created by chenyanying3 on 18-10-15.
 */
public interface PageRepository<M extends BaseModel, Q extends Query> extends Repository<M> {

    /**
     * 分页查询
     *
     * @param query 分页查询条件
     * @return 分页数据
     * @throws RepositoryException
     */
    PageResult<M> findByQuery(QPageQuery<Q> query) throws RepositoryException;


    /**
     * 查询
     * @param query
     * @return
     */
    List<M> findByQuery(ListQuery query);
}
