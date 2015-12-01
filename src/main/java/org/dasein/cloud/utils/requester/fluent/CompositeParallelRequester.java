/**
 * Copyright (C) 2009-2015 Dell, Inc.
 * See annotations for authorship information
 *
 * ====================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ====================================================================
 */

package org.dasein.cloud.utils.requester.fluent;

import org.dasein.cloud.utils.requester.DaseinParallelRequestExecutor;
import org.dasein.cloud.utils.requester.DaseinRequestException;
import org.dasein.cloud.utils.requester.DriverToCoreMapper;
import org.json.JSONObject;
import org.w3c.dom.Document;

import java.util.List;

/**
 * Created by vmunthiu on 6/17/2015.
 */
public interface CompositeParallelRequester extends ParallelRequester<String> {
    <T> ParallelRequester<T> withXmlProcessor(Class<T> classType);
    <T, V> ParallelRequester<V> withXmlProcessor(DriverToCoreMapper<T, V> mapper, Class<T> classType);
    <T> ParallelRequester<T> withJsonProcessor(Class<T> classType);
    <T, V> ParallelRequester<V> withJsonProcessor(DriverToCoreMapper<T, V> mapper, Class<T> classType);
    <T> DaseinParallelRequestExecutor<Document> withDocumentProcessor();
    <T> DaseinParallelRequestExecutor<JSONObject> withJSONObjectProcessor();
    List<String> execute() throws DaseinRequestException;
}
