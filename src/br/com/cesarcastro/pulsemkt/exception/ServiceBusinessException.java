package br.com.cesarcastro.pulsemkt.exception;

public class ServiceBusinessException extends Exception{

	private static final long serialVersionUID = 1L;
	
    public ServiceBusinessException(String message) {
        super(message);
    }

    public ServiceBusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public ServiceBusinessException(Throwable cause) {
        super(cause);
    }

}
