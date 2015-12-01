package org.dasein.cloud.utils.requester.streamprocessors.exceptions;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by vmunthiu on 12/1/2015.
 */
public class StreamReadException extends IOException {
    private String stringToProcess;
    private Type classType;

    public StreamReadException(String message, String stringToProcess, Type classType) {
        super(message);
        this.stringToProcess = stringToProcess;
        this.classType = classType;
    }

    public StreamReadException(String message, String stringToProcess, Type classType, Throwable cause) {
        super(message, cause);
        this.stringToProcess = stringToProcess;
        this.classType = classType;
    }

    public String getStringToProcess() {
        return stringToProcess;
    }

    public Type getClassType() {
        return classType;
    }
}
