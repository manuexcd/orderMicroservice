package tfg.miroservice.order.exception;

public class OrderLineNotFoundException extends Exception {

	private static final long serialVersionUID = 6762482809584858763L;

	private static final String MSG = "No order line found";

	public OrderLineNotFoundException() {
		super(MSG);
	}
}
