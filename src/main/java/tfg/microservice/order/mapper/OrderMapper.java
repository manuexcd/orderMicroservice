package tfg.microservice.order.mapper;

import org.springframework.stereotype.Component;

import tfg.microservice.order.dto.OrderDTO;
import tfg.microservice.order.model.Order;

@Component
public interface OrderMapper extends GenericMapper<Order, OrderDTO> {

}
