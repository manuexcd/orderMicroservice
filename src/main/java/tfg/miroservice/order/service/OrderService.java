package tfg.miroservice.order.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.exception.UserNotFoundException;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;

public interface OrderService {

	public Page<Order> getAllOrders(Pageable page);

	public Page<Order> getAllOrdersByOrderStatus(Pageable page);

	public Page<Order> getAllOrdersByDate(Pageable page);

	public Page<Order> getOrdersByUser(Long userId, Pageable page) throws UserNotFoundException;

	public Collection<OrderLine> getOrderLines(Long id) throws OrderNotFoundException;

	public Order getOrder(Long id) throws OrderNotFoundException;

	public Order createTemporalOrder(Long userId, Order order, List<ProductDTO> listProductDto);

	public Order getTemporalOrder(Long userId, Pageable page) throws OrderNotFoundException;

	public Order confirmTemporalOrder(Order order, String userEmail, List<ProductDTO> listProductDto) throws OrderNotFoundException;

	public Order updateOrder(Order order, String userEmail, List<ProductDTO> listProductDto) throws OrderNotFoundException;

	public Order cancelOrder(Long id, String userEmail) throws OrderNotFoundException;

	public void deleteOrder(Long id) throws OrderNotFoundException;
}
