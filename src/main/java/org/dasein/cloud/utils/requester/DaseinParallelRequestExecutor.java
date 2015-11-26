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
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dasein.cloud.utils.requester.fluent.ParallelRequester;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by vmunthiu on 6/17/2015.
 */
public class DaseinParallelRequestExecutor<T> extends AbstractDaseinRequestExecutor<T> implements ParallelRequester<T> {
    private ArrayList<HttpUriRequest> httpUriRequests;

    public DaseinParallelRequestExecutor(HttpClientBuilder httpClientBuilder, ArrayList<HttpUriRequest> httpUriRequests, ResponseHandler<T> responseHandler, String httpProxyHost, Integer httpProxyPort){
        super(httpClientBuilder, responseHandler, httpProxyHost, httpProxyPort);
        this.httpUriRequests = httpUriRequests;
    }

    public DaseinParallelRequestExecutor(HttpClientBuilder httpClientBuilder, ArrayList<HttpUriRequest> httpUriRequests, ResponseHandler<T> responseHandler){
        super(httpClientBuilder, responseHandler);
        this.httpUriRequests = httpUriRequests;
    }

    public List<T> execute() throws CloudException {
        final HttpClientBuilder clientBuilder = setProxyIfRequired(httpClientBuilder);

        final CloseableHttpClient httpClient = clientBuilder.build();

        List<T> results = new ArrayList<T>();
        List<Callable<T>> tasks = new ArrayList<Callable<T>>();
        for (final HttpUriRequest httpUriRequest : httpUriRequests) {
            tasks.add(new Callable<T>() {
                @Override
                public T call() throws Exception {
                    return execute(httpClient, httpUriRequest);
                }
            });
        }

        try{
            try {
                ExecutorService executorService = Executors.newFixedThreadPool(httpUriRequests.size());
                List<Future<T>> futures = executorService.invokeAll(tasks);
                for (Future<T> future : futures) {
                    T result = future.get();
                    results.add(result);
                }
                return results;
            } finally {
                httpClient.close();
            }
        } catch (Exception e) {
            throw new CloudException(e.getMessage());
        }
    }
}
