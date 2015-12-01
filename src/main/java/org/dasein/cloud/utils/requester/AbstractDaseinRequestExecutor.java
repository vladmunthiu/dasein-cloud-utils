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

import org.apache.http.HttpHost;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.DefaultProxyRoutePlanner;

/**
 * Created by vmunthiu on 6/17/2015.
 */
public abstract class AbstractDaseinRequestExecutor<T> {
    protected HttpClientBuilder httpClientBuilder;
    private ResponseHandler<T> responseHandler;
    private HttpProxyConfig httpProxyConfig;

    protected AbstractDaseinRequestExecutor(HttpClientBuilder httpClientBuilder, ResponseHandler<T> responseHandler, String httpProxyHost, Integer httpProxyPort) {
        this.httpProxyConfig = new HttpProxyConfig(httpProxyHost, httpProxyPort);
        this.httpClientBuilder = httpClientBuilder;
        this.responseHandler = responseHandler;
    }

    protected AbstractDaseinRequestExecutor(HttpClientBuilder httpClientBuilder, ResponseHandler<T> responseHandler){
        this.httpClientBuilder = httpClientBuilder;
        this.responseHandler = responseHandler;
    }

    protected T execute(HttpUriRequest httpUriRequest) throws DaseinRequestException {
        httpClientBuilder = setProxyIfRequired(httpClientBuilder);

        try {
            CloseableHttpClient httpClient = this.httpClientBuilder.build();
            try {
                return httpClient.execute(httpUriRequest, this.responseHandler);
            }
            finally{
                httpClient.close();
            }
        } catch (Exception e){
            throw new DaseinRequestException(e.getMessage(), e);
        }
    }

    protected T execute(CloseableHttpClient httpClient, HttpUriRequest httpUriRequest) throws DaseinRequestException {
        try {
            return httpClient.execute(httpUriRequest, this.responseHandler);
        } catch (Exception e){
            throw new DaseinRequestException(e.getMessage(), e);
        }
    }

    protected HttpClientBuilder setProxyIfRequired(HttpClientBuilder httpClientBuilder)
    {
        if(this.httpProxyConfig != null){
            HttpHost proxy = new HttpHost(httpProxyConfig.getHost(), httpProxyConfig.getPort());
            DefaultProxyRoutePlanner routePlanner = new DefaultProxyRoutePlanner(proxy);
            httpClientBuilder.setRoutePlanner(routePlanner);
        }

        return httpClientBuilder;
    }


}
