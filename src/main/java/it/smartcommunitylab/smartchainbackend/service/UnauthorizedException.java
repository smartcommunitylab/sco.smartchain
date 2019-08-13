package it.smartcommunitylab.smartchainbackend.service;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 8244358557337822791L;

    public UnauthorizedException() {
        super();
    }

    public UnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnauthorizedException(String message) {
        super(message);
    }

    public UnauthorizedException(Throwable cause) {
        super(cause);
    }

}
