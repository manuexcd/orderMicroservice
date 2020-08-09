package tfg.microservice.order.mapper;

import org.springframework.stereotype.Component;

import tfg.microservice.order.dto.OrderLineDTO;
import tfg.microservice.order.model.OrderLine;

@Component
public class OrderLineMapperImpl extends GenericMapperImpl<OrderLine, OrderLineDTO> implements OrderLineMapper {

	@Override
	public Class<OrderLine> getClazz() {
		return OrderLine.class;
	}

	@Override
	public Class<OrderLineDTO> getDtoClazz() {
		return OrderLineDTO.class;
	}
}
