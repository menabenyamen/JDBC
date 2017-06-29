package se.mebe.jdbcuser.exception;

public class ServiceException extends Exception {

	private static final long serialVersionUID = 1L;
	
//	public UserException(String message, Throwable cause) {
//		super(message, cause);
//	}

	public ServiceException(Throwable cause) {
		super(cause);
	}

	public ServiceException(String message) {
		super(message);
	}


}

