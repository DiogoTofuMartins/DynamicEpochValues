package org.acme.rest.client.rest.exception;

import java.io.Serializable;

public class BadRequestException extends RuntimeException implements Serializable {

    private static final long serialVersionUID = 1L;

    public BadRequestException() {
        super();
    }
    public BadRequestException(String msg) {
        super(msg);
    }
    public BadRequestException(String msg, Exception e)  {
        super(msg, e);
    }
}
