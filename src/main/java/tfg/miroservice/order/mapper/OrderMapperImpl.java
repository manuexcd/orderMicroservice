package tfg.miroservice.order.mapper;

import org.springframework.stereotype.Component;

import tfg.miroservice.order.dto.OrderDTO;
import tfg.miroservice.order.model.Order;

@Component
public class OrderMapperImpl extends GenericMapperImpl<Order, OrderDTO> implements OrderMapper {

	@Override
	public Class<Order> getClazz() {
		return Order.class;
	}

	@Override
	public Class<OrderDTO> getDtoClazz() {
		return OrderDTO.class;
	}
}
