package org.dasein.cloud.utils.requester.streamprocessors.exceptions;

/**
 * Created by vmunthiu on 12/1/2015.
 */
public class StreamWriteException extends Exception {
    private Object objectToWrite;

    public StreamWriteException(String message, Object objectToWrite) {
        super(message);
        this.objectToWrite = objectToWrite;
    }

    public StreamWriteException(String message, Object objectToWrite, Throwable cause) {
        super(message, cause);
        this.objectToWrite = objectToWrite;
    }

    public Object getObjectToWrite() {
        return objectToWrite;
    }
}
