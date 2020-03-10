package tfg.miroservice.order.exception;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = -4328489130495899086L;

	private static final String MSG = "User not found";

	public UserNotFoundException() {
		super(MSG);
	}
}
