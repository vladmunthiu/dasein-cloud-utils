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

import org.dasein.cloud.utils.requester.streamprocessors.exceptions.StreamReadException;
import org.dasein.cloud.utils.requester.streamprocessors.exceptions.StreamWriteException;

import javax.annotation.Nullable;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;

/**
 * @author Vlad Munthiu
 */
public class XmlStreamToObjectProcessor<T> extends StreamProcessor<T> {
    public @Nullable T read(InputStream inputStream, Class<T> classType) throws StreamReadException {
        try {
            JAXBContext context = JAXBContext.newInstance(classType);
            Unmarshaller u = context.createUnmarshaller();
            return (T)u.unmarshal(inputStream);
        } catch (Exception ex) {
            throw new StreamReadException("Error deserializing input stream into object", tryGetString(inputStream), ((ParameterizedType)classType.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
    }

    public @Nullable String write(T object) throws StreamWriteException {
        try {
            StringWriter stringWriter = new StringWriter();
            JAXBContext jc = JAXBContext.newInstance(object.getClass());
            Marshaller m = jc.createMarshaller();
            m.marshal(object, stringWriter);

            return stringWriter.toString();
        }
        catch (Exception ex){
            throw new StreamWriteException("Error serializing object into string", object, ex);
        }
    }
}
