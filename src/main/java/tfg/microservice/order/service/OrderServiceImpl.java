package tfg.microservice.order.service;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import tfg.microservice.order.dto.ProductDTO;
import tfg.microservice.order.exception.OrderNotFoundException;
import tfg.microservice.order.mail.MailSender;
import tfg.microservice.order.model.Constants;
import tfg.microservice.order.model.Order;
import tfg.microservice.order.model.OrderLine;
import tfg.microservice.order.repository.OrderRepository;

@Service("orderManager")
public class OrderServiceImpl implements OrderService {

	@Autowired
	private OrderRepository orders;

	@Autowired
	private MailSender mailSender;

	@Override
	public Page<Order> getAllOrders(Pageable page) {
		return orders.findAll(page);
	}

	@Override
	public Page<Order> getAllOrdersByOrderStatus(Pageable page) {
		return orders.findAllByOrderByOrderStatus(page);
	}

	@Override
	public Page<Order> getAllOrdersByDateAsc(Pageable page) {
		return orders.findAllByOrderByDate(page);
	}

	@Override
	public Page<Order> getAllOrdersByDateDesc(Pageable page) {
		return orders.findAllByOrderByDateDesc(page);
	}

	@Override
	public Order getOrder(Long id) throws OrderNotFoundException {
		return orders.findById(id).orElseThrow(OrderNotFoundException::new);
	}

	@Override
	public Page<Order> getOrdersByUser(Long userId, Pageable page) {
		return orders.findByUserId(userId, page);
	}

	@Override
	public Page<Order> getOrdersByUserDateAsc(Long userId, Pageable page) {
		return orders.findByUserIdOrderByDate(userId, page);
	}

	@Override
	public Page<Order> getOrdersByUserDateDesc(Long userId, Pageable page) {
		return orders.findByUserIdOrderByDateDesc(userId, page);
	}

	@Override
	public Page<Order> getOrdersByParam(String param, Pageable page) {
		return orders.findByParam(param, page);
	}

	@Override
	public Page<Order> getOrdersByParamAndUser(String param, long userId, Pageable page) {
		return orders.findByParamAndUser(param, userId, page);
	}

	@Override
	public Collection<OrderLine> getOrderLines(Long id) throws OrderNotFoundException {
		return orders.findById(id).map(Order::getOrderLines).orElseThrow(OrderNotFoundException::new);
	}

	@Override
	public Order getTemporalOrder(Long userId) throws OrderNotFoundException {
		List<Order> list = orders.findByUserId(userId).stream()
				.filter(order -> order.getOrderStatus().equals(Constants.ORDER_STATUS_TEMPORAL))
				.collect(Collectors.toList());
		if (!list.isEmpty())
			return list.get(0);
		else
			throw new OrderNotFoundException();
	}

	@Override
	public Order createTemporalOrder(Long userId, Order order, List<ProductDTO> listProductDto) {
		order.setUserId(userId);
		order.setDate(new Timestamp(System.currentTimeMillis()));
		order.setOrderStatus(Constants.ORDER_STATUS_TEMPORAL);
		order.getOrderLines().stream().forEach(line -> line.setOrder(order));
		order.setTotalPrice(this.calcOrderPrice(order, listProductDto));
		return orders.save(order);
	}

	@Override
	public Order confirmTemporalOrder(Order order, String userEmail, List<ProductDTO> listProductDto)
			throws OrderNotFoundException {
		if (orders.existsById(order.getId())) {
			order.setDate(new Timestamp(System.currentTimeMillis()));
			order.setOrderStatus(Constants.ORDER_STATUS_RECEIVED);
			order.getOrderLines().stream().forEach(line -> line.setOrder(order));
			order.setTotalPrice(this.calcOrderPrice(order, listProductDto));
			Map<Object, Object> params = new HashMap<>();
			params.put(Constants.TEMPLATE_PARAM_ORDER_ID, order.getId());
			params.put(Constants.TEMPLATE_PARAM_ORDER_PRICE, order.getTotalPrice());
			params.put(Constants.TEMPLATE_PARAM_ORDER_LINES, order.getOrderLines());
			mailSender.sendEmail(userEmail, Constants.SUBJECT_ORDER_CONFIRMED, Constants.TEMPLATE_ORDER_CONFIRMED,
					params);
			return orders.save(order);
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public Order updateOrder(Order order, String userEmail, List<ProductDTO> listProductDto)
			throws OrderNotFoundException {
		if (orders.existsById(order.getId())) {
			order.getOrderLines().stream().forEach(line -> line.setOrder(order));
			order.setTotalPrice(this.calcOrderPrice(order, listProductDto));
			if (!order.getOrderStatus().equals(Constants.ORDER_STATUS_TEMPORAL)) {
				Map<Object, Object> params = new HashMap<>();
				params.put(Constants.TEMPLATE_PARAM_ORDER_ID, order.getId());
				params.put(Constants.TEMPLATE_PARAM_ORDER_STATUS, order.getOrderStatus());
				mailSender.sendEmail(userEmail, Constants.SUBJECT_ORDER_UPDATED, Constants.TEMPLATE_ORDER_UPDATED,
						params);
			}
			return orders.save(order);
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public Order cancelOrder(Long id, String userEmail) throws OrderNotFoundException {
		Optional<Order> optional = orders.findById(id);
		if (optional.isPresent()) {
			Order order = optional.get();
			if (order.getOrderStatus().equals(Constants.ORDER_STATUS_RECEIVED)) {
				order.setOrderStatus(Constants.ORDER_STATUS_CANCELLED);
				Map<Object, Object> params = new HashMap<>();
				params.put(Constants.TEMPLATE_PARAM_ORDER_ID, order.getId());
				mailSender.sendEmail(userEmail, Constants.SUBJECT_ORDER_CANCELLED, Constants.TEMPLATE_ORDER_CANCELLED,
						params);
			}
			return orders.save(order);
		} else {
			throw new OrderNotFoundException();
		}
	}

	@Override
	public void deleteOrder(Long id) throws OrderNotFoundException {
		if (orders.existsById(id))
			orders.deleteById(id);
		else
			throw new OrderNotFoundException();
	}

	private Double calcOrderPrice(Order order, List<ProductDTO> listProductDto) {
		Double totalPrice = Double.valueOf(0);
		for (OrderLine line : order.getOrderLines()) {
			for (ProductDTO productDto : listProductDto) {
				if (line.getProductId().equals(productDto.getId()))
					totalPrice += line.getQuantity() * productDto.getPrice();
			}
		}
		return totalPrice;
	}
}
