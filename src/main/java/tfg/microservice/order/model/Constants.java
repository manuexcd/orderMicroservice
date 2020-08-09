package tfg.microservice.order.model;

public final class Constants {
	private Constants() {
	}

	public static final String PAGINATION_DEFAULT_PAGE = "0";
	public static final String PAGINATION_DEFAULT_SIZE = "5";

	public static final String PATH_PRODUCTS = "/products";
	public static final String PATH_USERS = "/users";
	public static final String PATH_ORDERS = "/orders";
	public static final String PATH_ORDERLINES = "/lines";
	public static final String PATH_STATUS = "/status";
	public static final String PATH_TEMPORAL = "/temporal";
	public static final String PATH_CANCEL = "/cancel";
	public static final String PATH_DATE_ASC = "/dateasc";
	public static final String PATH_DATE_DESC = "/datedesc";
	public static final String PATH_PARAM = "/param";

	public static final String PARAM_ID = "/{id}";
	public static final String PARAM_ORDER_ID = "/{orderId}";
	public static final String PARAM_USER_ID = "/{userId}";
	public static final String PARAM = "/{param}";

	public static final String ORDER_STATUS_TEMPORAL = "TEMPORAL";
	public static final String ORDER_STATUS_RECEIVED = "RECEIVED";
	public static final String ORDER_STATUS_IN_PROGRESS = "IN_PROGRESS";
	public static final String ORDER_STATUS_IN_DELIVERY = "IN_DELIVERY";
	public static final String ORDER_STATUS_DELIVERED = "DELIVERED";
	public static final String ORDER_STATUS_CANCELLED = "CANCELLED";

	public static final String SUBJECT_ORDER_CONFIRMED = "Pedido confirmado";
	public static final String SUBJECT_ORDER_UPDATED = "Pedido modificado";
	public static final String SUBJECT_ORDER_CANCELLED = "Pedido cancelado";

	public static final String TEMPLATE_PARAM_ORDER_ID = "orderId";
	public static final String TEMPLATE_PARAM_ORDER_STATUS = "orderStatus";
	public static final String TEMPLATE_PARAM_ORDER_PRICE = "orderPrice";
	public static final String TEMPLATE_PARAM_ORDER_LINES = "orderLines";

	public static final String TEMPLATE_ORDER_CONFIRMED = "Se ha confirmado el pedido {{orderId}}.\nImporte del pedido: {{orderPrice}} €.";
	public static final String TEMPLATE_ORDER_UPDATED = "Se ha modificado el pedido {{orderId}}. Estado del pedido: {{orderStatus}}.";
	public static final String TEMPLATE_ORDER_CANCELLED = "Se ha cancelado el pedido {{orderId}}.";
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
}
