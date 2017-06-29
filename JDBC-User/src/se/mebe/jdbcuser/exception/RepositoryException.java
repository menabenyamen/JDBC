package se.mebe.jdbcuser.exception;

public class RepositoryException extends Exception {

	private static final long serialVersionUID = 4757551175137109475L;

	public RepositoryException(String message, Throwable cause) {
		super(message, cause);
	}

	public RepositoryException(Throwable cause) {
		super(cause);
	}

	public RepositoryException(String message) {
		super(message);
	}

}
