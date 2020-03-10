package tfg.miroservice.order.service;

import java.util.List;

import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.exception.OrderLineNotFoundException;
import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.model.OrderLine;

public interface OrderLineService {

	public OrderLine getOrderLine(long id) throws OrderLineNotFoundException;

	public void deleteOrderLine(long id, String userEmail, List<ProductDTO> listProductDto)
			throws OrderLineNotFoundException, OrderNotFoundException;
}
