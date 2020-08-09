package tfg.microservice.order.service;

import java.util.List;

import tfg.microservice.order.dto.ProductDTO;
import tfg.microservice.order.exception.OrderLineNotFoundException;
import tfg.microservice.order.exception.OrderNotFoundException;
import tfg.microservice.order.model.OrderLine;

public interface OrderLineService {

	public OrderLine getOrderLine(long id) throws OrderLineNotFoundException;

	public void deleteOrderLine(long id, String userEmail, List<ProductDTO> listProductDto)
			throws OrderLineNotFoundException, OrderNotFoundException;
}
