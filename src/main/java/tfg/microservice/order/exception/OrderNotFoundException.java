package tfg.microservice.order.exception;

public class OrderNotFoundException extends Exception {

	private static final long serialVersionUID = -5128791542128687323L;

	private static final String MSG = "Order not found";

	public OrderNotFoundException() {
		super(MSG);
	}
}
