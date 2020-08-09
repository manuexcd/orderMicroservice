package tfg.miroservice.order.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.exception.OrderLineNotFoundException;
import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;
import tfg.miroservice.order.repository.OrderLineRepository;

@Service("orderLineManager")
public class OrderLineServiceImpl implements OrderLineService {

	@Autowired
	private OrderLineRepository repository;

	@Autowired
	private OrderService orderService;

	@Override
	public OrderLine getOrderLine(long id) throws OrderLineNotFoundException {
		return repository.findById(id).orElseThrow(OrderLineNotFoundException::new);
	}

	@Override
	public void deleteOrderLine(long id, String userEmail, List<ProductDTO> listProductDto)
			throws OrderLineNotFoundException, OrderNotFoundException {
		if (repository.existsById(id)) {
			OrderLine line = repository.getOne(id);
			Order order = orderService.getOrder(line.getOrder().getId());
			order.getOrderLines().remove(line);
			if(order.getOrderLines().isEmpty())
				orderService.deleteOrder(order.getId());
			else
				orderService.updateOrder(order, userEmail, listProductDto);
			repository.deleteById(id);
		} else {
			throw new OrderLineNotFoundException();
		}
	}
}