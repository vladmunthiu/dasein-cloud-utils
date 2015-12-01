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
import org.w3c.dom.Document;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.InputStream;
import java.io.StringWriter;
import java.lang.reflect.ParameterizedType;

/**
 * @author Vlad Munthiu
 */
public class StreamToDocumentProcessor extends StreamProcessor<Document> {
    @Nullable
    @Override
    public Document read(InputStream inputStream, Class<Document> classType) throws StreamReadException {
        try {
            try {
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                return documentBuilder.parse(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (Exception e) {
            throw new StreamReadException("Error deserializing input stream into object", tryGetString(inputStream), ((ParameterizedType)classType.getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
        }
    }

    @Nullable
    @Override
    public String write(Document document) throws StreamWriteException {
        try {
            StringWriter stringWriter = new StringWriter();
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");

            transformer.transform(new DOMSource(document), new StreamResult(stringWriter));
            return stringWriter.toString();
        }
        catch (Exception ex) {
            throw new StreamWriteException("Error serializing object into string", document, ex);
        }
    }
}
