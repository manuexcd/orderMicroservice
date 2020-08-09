package tfg.microservice.order.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tfg.microservice.order.dto.ProductDTO;
import tfg.microservice.order.exception.OrderNotFoundException;
import tfg.microservice.order.model.Order;
import tfg.microservice.order.model.OrderLine;

public interface OrderService {

	public Page<Order> getAllOrders(Pageable page);

	public Page<Order> getAllOrdersByOrderStatus(Pageable page);

	public Page<Order> getAllOrdersByDateAsc(Pageable page);

	public Page<Order> getAllOrdersByDateDesc(Pageable page);

	public Page<Order> getOrdersByUser(Long userId, Pageable page);

	public Page<Order> getOrdersByUserDateAsc(Long userId, Pageable page);

	public Page<Order> getOrdersByUserDateDesc(Long userId, Pageable page);

	public Page<Order> getOrdersByParam(String param, Pageable page);

	public Page<Order> getOrdersByParamAndUser(String param, long userId, Pageable page);

	public Collection<OrderLine> getOrderLines(Long id) throws OrderNotFoundException;

	public Order getOrder(Long id) throws OrderNotFoundException;

	public Order createTemporalOrder(Long userId, Order order, List<ProductDTO> listProductDto);

	public Order getTemporalOrder(Long userId) throws OrderNotFoundException;

	public Order confirmTemporalOrder(Order order, String userEmail, List<ProductDTO> listProductDto)
			throws OrderNotFoundException;

	public Order updateOrder(Order order, String userEmail, List<ProductDTO> listProductDto)
			throws OrderNotFoundException;

	public Order cancelOrder(Long id, String userEmail) throws OrderNotFoundException;

	public void deleteOrder(Long id) throws OrderNotFoundException;
}
