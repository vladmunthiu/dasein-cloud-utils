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

package org.dasein.cloud.utils.requester;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dasein.cloud.utils.requester.fluent.Requester;

/**
 * @author Vlad Munthiu
 */

public class DaseinRequestExecutor<T> extends AbstractDaseinRequestExecutor<T> implements Requester<T> {
    private HttpUriRequest httpUriRequest;

    public DaseinRequestExecutor(HttpClientBuilder httpClientBuilder, HttpUriRequest httpUriRequest, ResponseHandler<T> responseHandler, String httpProxyHost, Integer httpProxyPort){
        super(httpClientBuilder, responseHandler, httpProxyHost, httpProxyPort);
        this.httpUriRequest = httpUriRequest;
    }

    public DaseinRequestExecutor(HttpClientBuilder httpClientBuilder, HttpUriRequest httpUriRequest, ResponseHandler<T> responseHandler){
        super(httpClientBuilder, responseHandler);
        this.httpUriRequest = httpUriRequest;
    }

    public T execute() throws DaseinRequestException {
        return execute(this.httpUriRequest);
    }
}
