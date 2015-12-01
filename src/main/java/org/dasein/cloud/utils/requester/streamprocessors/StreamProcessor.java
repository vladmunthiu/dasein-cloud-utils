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

package org.dasein.cloud.utils.requester.streamprocessors;

import org.apache.commons.io.IOUtils;
import org.dasein.cloud.utils.requester.streamprocessors.exceptions.StreamReadException;
import org.dasein.cloud.utils.requester.streamprocessors.exceptions.StreamWriteException;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * @author Vlad Munthiu
 */
public abstract class StreamProcessor<T> {
     @Nullable public abstract T read(InputStream inputStream, Class<T> classType) throws StreamReadException;
     @Nullable public abstract String write(T object) throws StreamWriteException;

     protected String getString(InputStream inputStream) throws IOException {
         StringWriter stringWriter = new StringWriter();
         IOUtils.copy(inputStream, stringWriter);
         return stringWriter.toString();
     }

     protected String tryGetString(InputStream inputStream) {
         try {
             return getString(inputStream);
         } catch (IOException ex) {
             return null;
         }
     }
}
