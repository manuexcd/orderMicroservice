package tfg.miroservice.order.mapper;

import org.springframework.stereotype.Component;

import tfg.miroservice.order.dto.OrderDTO;
import tfg.miroservice.order.model.Order;

@Component
public interface OrderMapper extends GenericMapper<Order, OrderDTO> {

}
