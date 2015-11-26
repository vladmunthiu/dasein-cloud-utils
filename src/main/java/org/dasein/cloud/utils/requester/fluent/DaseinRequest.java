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

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.HttpClientBuilder;
import org.dasein.cloud.utils.requester.*;
import org.dasein.cloud.utils.requester.streamprocessors.*;
import org.json.JSONObject;
import org.w3c.dom.Document;

import javax.annotation.Nonnull;

/**
 * DaseinRequest class is a wrapper for Apache HTTP client. It unifies Dasein's REST calls to the clouds APIs.
 *
 * <pre>
 * <code>
 *     String result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).execute();
 *     Document resultAsDocument = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).withDocumentProcessor().execute();
 * </code>
 * </pre>
 *
 * @author Vlad Munthiu
* */

public class DaseinRequest implements CompositeRequester {
    private HttpClientBuilder httpClientBuilder;
    private HttpUriRequest httpUriRequestBuilder;
    private HttpProxyConfig httpProxyConfig;

    public DaseinRequest(@Nonnull HttpClientBuilder httpClientBuilder, @Nonnull HttpUriRequest httpUriRequestBuilder, @Nonnull String httpProxyHost, @Nonnull Integer httpProxyPort){
        if(httpClientBuilder == null)
            throw new IllegalArgumentException("Parameter httpClientBuilder cannot be null.");

        if(httpUriRequestBuilder == null)
            throw new IllegalArgumentException("Parameter httpUriRequestBuilder cannot be null");

        this.httpClientBuilder = httpClientBuilder;
        this.httpUriRequestBuilder = httpUriRequestBuilder;
        this.httpProxyConfig = new HttpProxyConfig(httpProxyHost, httpProxyPort);
    }
    /**
     * Constructs a new DaseinRequest instance, ready to execute http calls to a specified Uri.
     *
     * @param httpClientBuilder HTTP client builder
     * @param httpUriRequestBuilder HTTP URI request builder
    **/
    public DaseinRequest(@Nonnull HttpClientBuilder httpClientBuilder, @Nonnull HttpUriRequest httpUriRequestBuilder){
        if(httpClientBuilder == null)
            throw new IllegalArgumentException("Parameter httpClientBuilder cannot be null.");

        if(httpUriRequestBuilder == null)
            throw new IllegalArgumentException("Parameter httpUriRequestBuilder cannot be null");

        this.httpClientBuilder = httpClientBuilder;
        this.httpUriRequestBuilder = httpUriRequestBuilder;
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a XML stream processor that, once the HTTP request has been
     * finished, will perform a deserialization of the XML response into the specified type T.
     *
     * <pre>
     *     DaseinDriverType result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).withXmlProcessor(DaseinDriverType.class).execute();
     * </pre>
     *
     * @param classType the type of the expected model
     * @return an instance of the classType type representing the response XML
    **/
    @Override
    public <T> Requester<T> withXmlProcessor(@Nonnull Class<T> classType) {
        if(classType == null)
            throw new IllegalArgumentException("Parameter classType cannot be null");

        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<T>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<T>(new XmlStreamToObjectProcessor(), classType));
        } else {
            return new DaseinRequestExecutor<T>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<T>(new XmlStreamToObjectProcessor(), classType), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a XML stream processor that, once the HTTP request has been
     * finished, will perform a deserialization of the XML response into the specified type T. A valid instance of a
     * DriverToCoreMapper should be passed in, so that a mapping from a driver model type ( T ) to a Dasein Core
     * model( V ) to be performed after the response is received.
     *
     * <pre>
     *     DaseinCoreType result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder)
     *                  .withXmlProcessor(new DriverToCoreMapper&lt;DaseinDriverType, DaseinCoreType&gt;() {
     *                           &#64;Override
     *                           public DaseinCoreType mapFrom(DaseinDriverType entity) {
     *                                  //map entity to a new instance of DaseinCoreType
     *                           }
     *                      DaseinDriverType.class).execute();
     * </pre>
     *
     * @param mapper an implementation of {@link DriverToCoreMapper} interface
     * @param classType the type of the expected model
     * @return an instance of the V type which should be a Dasien Core type.
     **/
    @Override
    public <T, V> Requester<V> withXmlProcessor(@Nonnull DriverToCoreMapper<T, V> mapper, @Nonnull Class<T> classType) {
        if(mapper == null)
            throw new IllegalArgumentException("Parameter mapper cannot be null");

        if(classType == null)
            throw new IllegalArgumentException("Parameter classType cannot be null");

        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<V>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandlerWithMapper<T, V>(new XmlStreamToObjectProcessor(), mapper, classType));
        } else {
            return new DaseinRequestExecutor<V>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandlerWithMapper<T, V>(new XmlStreamToObjectProcessor(), mapper, classType), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a JSON stream processor that, once the HTTP request has been
     * finished will perform a deserialization of the JSON response into the specified type T.
     *
     * <pre>
     *     DaseinDriverType result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).withJsonProcessor(DaseinDriverType.class).execute();
     * </pre>
     *
     * @param classType the type of the expected model
     * @return an instance of the classType type representing the response JSON
     **/
    @Override
    public <T> Requester<T> withJsonProcessor(@Nonnull Class<T> classType) {
        if(classType == null)
            throw new IllegalArgumentException("Parameter classType cannot be null");

        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<T>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<T>(new JsonStreamToObjectProcessor(), classType));
        } else {
            return new DaseinRequestExecutor<T>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<T>(new JsonStreamToObjectProcessor(), classType), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a JSON stream processor that, once the HTTP request has been
     * finished, will perform a deserialization of the JSON response into the specified type T. A valid instance of a
     * DriverToCoreMapper should be passed in, so that a mapping from a driver model type ( T ) to a Dasein Core
     * model( V ) to be performed after the response is received.
     *
     * <pre>
     *     DaseinCoreType result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder)
     *                  .withJsonProcessor(new DriverToCoreMapper&lt;DaseinDriverType, DaseinCoreType&gt;() {
     *                           &#64;Override
     *                           public DaseinCoreType mapFrom(DaseinDriverType entity) {
     *                                  //map entity to a new instance of DaseinCoreType
     *                           }
     *                      DaseinDriverType.class).execute();
     * </pre>
     *
     * @param mapper an implementation of {@link org.dasein.cloud.util.requester.DriverToCoreMapper} interface
     * @param classType the type of the expected model
     * @return an instance of the V type which should be a Dasien Core type.
     **/
    @Override
    public <T, V> Requester<V> withJsonProcessor(@Nonnull DriverToCoreMapper<T, V> mapper, @Nonnull Class<T> classType) {
        if(mapper == null)
            throw new IllegalArgumentException("Parameter mapper cannot be null");

        if(classType == null)
            throw new IllegalArgumentException("Parameter classType cannot be null");

        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<V>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandlerWithMapper<T, V>(new JsonStreamToObjectProcessor(), mapper, classType));
        } else {
            return new DaseinRequestExecutor<V>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandlerWithMapper<T, V>(new JsonStreamToObjectProcessor(), mapper, classType), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a stream processor that, once the HTTP request has been
     * finished, will try to parse the response stream into a valid XML Document object.
     *
     * <pre>
     *     Document documentResult = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).withDocumentProcessor().execute();
     * </pre>
    **/
    @Override
    public <T> DaseinRequestExecutor<Document> withDocumentProcessor() {
        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<Document>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<Document>(new StreamToDocumentProcessor(), Document.class));
        } else {
            return new DaseinRequestExecutor<Document>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<Document>(new StreamToDocumentProcessor(), Document.class), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Constructs a instance of a DaseinRequestExecutor with a stream processor that, once the HTTP request has been
     * finished, will try to parse the response stream into a valid JSONObject object.
     *
     * <pre>
     *     JSONObject jsonResult = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).withJSONObjectProcessor().execute();
     * </pre>
     **/
    @Override
    public <T> DaseinRequestExecutor<JSONObject> withJSONObjectProcessor() {
        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<JSONObject>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<JSONObject>(new StreamToJSONObjectProcessor(), JSONObject.class));
        } else {
            return new DaseinRequestExecutor<JSONObject>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<JSONObject>(new StreamToJSONObjectProcessor(), JSONObject.class), httpProxyConfig.getHost(), httpProxyConfig.getPort());
        }
    }

    /**
     * Executes a HTTP request using a string processor for the response.
     *
     * <pre>
     *     String result = new DaseinRequest(cloudProvider, httpClientBuilder, httpUriRequestBuilder).execute();
     * </pre>
     *
     * @return a string representing the response of the current HTTP call.
    **/
    @Override
    public String execute() throws CloudException {
        if(httpProxyConfig == null) {
            return new DaseinRequestExecutor<String>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<String>(new StreamToStringProcessor(), String.class)).execute();
        } else {
            return new DaseinRequestExecutor<String>(this.httpClientBuilder, this.httpUriRequestBuilder,
                    new DaseinResponseHandler<String>(new StreamToStringProcessor(), String.class), httpProxyConfig.getHost(), httpProxyConfig.getPort()).execute();
        }
    }
}
