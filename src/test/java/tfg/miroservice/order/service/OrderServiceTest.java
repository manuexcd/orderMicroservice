package tfg.miroservice.order.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.exception.UserNotFoundException;
import tfg.miroservice.order.mail.MailSender;
import tfg.miroservice.order.model.Constants;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;
import tfg.miroservice.order.repository.OrderRepository;

@RunWith(MockitoJUnitRunner.class)
public class OrderServiceTest {

	@Mock
	private OrderRepository dao;

	@Mock
	private MailSender mailSender;

	@InjectMocks
	private OrderServiceImpl service;

	private Pageable pageRequest;

	@Test
	public void testGetAllOrders() {
		given(dao.findAll(eq(pageRequest))).willReturn(Page.empty());
		assertNotNull(service.getAllOrders(pageRequest));
	}

	@Test
	public void testGetAllOrdersByOrderStatus() {
		given(dao.findAllByOrderByOrderStatus(pageRequest)).willReturn(Page.empty());
		assertNotNull(service.getAllOrdersByOrderStatus(pageRequest));
	}

	@Test
	public void testGetAllOrdersByDate() {
		given(dao.findAllByOrderByDate(pageRequest)).willReturn(Page.empty());
		assertNotNull(service.getAllOrdersByDate(pageRequest));
	}

	@Test
	public void testGetOrderById() throws OrderNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.of(new Order()));
		assertNotNull(service.getOrder(anyLong()));
	}

	@Test
	public void testGetOrdersByUser() throws UserNotFoundException {
		given(dao.findByUserId(anyLong(), any())).willReturn(Page.empty());
		assertNotNull(service.getOrdersByUser(anyLong(), any()));
	}

	@Test
	public void testGetOrderLines() throws OrderNotFoundException {
		Order order = new Order();
		order.setOrderLines(Arrays.asList(new OrderLine()));
		given(dao.findById(anyLong())).willReturn(Optional.of(order));
		assertNotNull(service.getOrderLines(anyLong()));
	}

	@Test(expected = OrderNotFoundException.class)
	public void testGetOrderLinesException() throws OrderNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.ofNullable(null));
		assertNull(service.getOrderLines(anyLong()));
	}

	@Test
	public void testGetTemporalOrder() throws OrderNotFoundException {
		Order order = new Order();
		order.setOrderLines(Arrays.asList(new OrderLine()));
		Page<Order> page = new PageImpl<>((Arrays.asList(order)));
		given(dao.findByUserId(anyLong(), any())).willReturn(page);
		assertNotNull(service.getTemporalOrder(anyLong(), any()));
	}

	@Test(expected = OrderNotFoundException.class)
	public void testGetEmptyTemporalOrder() throws OrderNotFoundException {
		given(dao.findByUserId(anyLong(), any())).willReturn(Page.empty());
		assertNotNull(service.getTemporalOrder(anyLong(), any()));
	}

	@Test
	public void testCreateTemporalOrder() {
		Order order = new Order();
		OrderLine orderLine = new OrderLine();
		order.setOrderLines(Arrays.asList(orderLine));
		given(dao.save(order)).willReturn(order);
		assertNotNull(service.createTemporalOrder(Long.valueOf(1), order, Collections.emptyList()));
	}

	@Test
	public void testUpdateOrder() throws OrderNotFoundException {
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setProductId(Long.valueOf(1));
		line.setQuantity(2);
		List<OrderLine> list = new ArrayList<>();
		list.add(line);
		order.setUserId(Long.valueOf(1));
		order.setOrderStatus(Constants.ORDER_STATUS_RECEIVED);
		order.setOrderLines(list);
		given(dao.existsById(any())).willReturn(Boolean.TRUE);
		given(dao.save(order)).willReturn(order);
		assertNotNull(service.updateOrder(order, "", Collections.emptyList()));
	}

	@Test(expected = OrderNotFoundException.class)
	public void testUpdateOrderNotFound() throws OrderNotFoundException {
		Order order = new Order();
		service.updateOrder(order, "", Collections.emptyList());
	}

	@Test
	public void testCancelOrder() throws OrderNotFoundException {
		Order order = new Order();
		order.setUserId(Long.valueOf(1));
		order.setOrderStatus(Constants.ORDER_STATUS_RECEIVED);
		given(dao.findById(anyLong())).willReturn(Optional.of(order));
		given(dao.save(order)).willReturn(order);
		assertNotNull(service.cancelOrder(Long.valueOf(1), ""));
	}

	@Test(expected = OrderNotFoundException.class)
	public void testCancelNullOrder() throws OrderNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.empty());
		service.cancelOrder(Long.valueOf(1), "");
	}

	@Test
	public void testCancelOrderBadStatus() throws OrderNotFoundException {
		Order order = new Order();
		order.setUserId(Long.valueOf(1));
		order.setOrderStatus(Constants.ORDER_STATUS_CANCELLED);
		given(dao.findById(anyLong())).willReturn(Optional.of(order));
		given(dao.save(order)).willReturn(order);
		assertNotNull(service.cancelOrder(Long.valueOf(1), ""));
	}

	@Test
	public void testConfirmTemporalOrder() throws OrderNotFoundException {
		Order order = new Order();
		OrderLine orderLine = new OrderLine();
		orderLine.setOrder(order);
		orderLine.setProductId(Long.valueOf(1));
		orderLine.setQuantity(2);
		order.setUserId(Long.valueOf(1));
		order.setOrderStatus(Constants.ORDER_STATUS_RECEIVED);
		order.setOrderLines(Arrays.asList(orderLine));
		given(dao.existsById(any())).willReturn(Boolean.TRUE);
		given(dao.save(order)).willReturn(order);
		assertNotNull(service.confirmTemporalOrder(order, "", Collections.emptyList()));
	}

	@Test(expected = OrderNotFoundException.class)
	public void testConfirmTemporalOrderNotFound() throws OrderNotFoundException {
		Order order = new Order();
		assertNull(service.confirmTemporalOrder(order, "", Collections.emptyList()));
	}

	@Test
	public void testDeleteOrder() throws OrderNotFoundException {
		Order order = new Order();
		dao.save(order);
		given(dao.existsById(order.getId())).willReturn(Boolean.TRUE);
		service.deleteOrder(order.getId());
		verify(dao, times(1)).deleteById(any());
	}

	@Test(expected = OrderNotFoundException.class)
	public void testDeleteOrderNotFound() throws OrderNotFoundException {
		service.deleteOrder(Long.valueOf(1));
	}
}