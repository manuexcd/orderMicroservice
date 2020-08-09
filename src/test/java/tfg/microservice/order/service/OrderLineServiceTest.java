package tfg.microservice.order.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import tfg.microservice.order.exception.OrderLineNotFoundException;
import tfg.microservice.order.exception.OrderNotFoundException;
import tfg.microservice.order.model.Order;
import tfg.microservice.order.model.OrderLine;
import tfg.microservice.order.repository.OrderLineRepository;
import tfg.microservice.order.service.OrderLineServiceImpl;
import tfg.microservice.order.service.OrderServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineServiceTest {

	@Mock
	private OrderLineRepository dao;

	@Mock
	private OrderServiceImpl orderService;

	@InjectMocks
	private OrderLineServiceImpl service;

	@Test
	public void testGetOrderLineById() throws OrderLineNotFoundException {
		given(dao.findById(anyLong())).willReturn(Optional.of(new OrderLine()));
		assertNotNull(service.getOrderLine(anyLong()));
	}

	@Test(expected = OrderLineNotFoundException.class)
	public void testOrderLineNotFound() throws OrderLineNotFoundException {
		assertNotNull(service.getOrderLine(anyLong()));
	}

	@Test
	public void testDeleteOrderLineAndOrder() throws OrderLineNotFoundException, OrderNotFoundException {
		Order order = new Order();
		order.setId(Long.valueOf(1));
		OrderLine orderLine = new OrderLine();
		orderLine.setOrder(order);
		List<OrderLine> list = new ArrayList<>();
		list.add(orderLine);
		order.setOrderLines(list);
		dao.save(orderLine);
		Long id = Long.valueOf(1);
		given(dao.existsById(id)).willReturn(true);
		given(dao.getOne(id)).willReturn(orderLine);
		given(orderService.getOrder(anyLong())).willReturn(order);
		service.deleteOrderLine(id, "", Collections.emptyList());
		given(dao.existsById(id)).willReturn(false);
		assertFalse(dao.existsById(id));
	}

	@Test
	public void testDeleteOrderLine() throws OrderLineNotFoundException, OrderNotFoundException {
		Order order = new Order();
		order.setId(Long.valueOf(1));
		OrderLine orderLine = new OrderLine();
		orderLine.setOrder(order);
		OrderLine orderLine2 = new OrderLine();
		orderLine2.setOrder(order);
		List<OrderLine> list = new ArrayList<>();
		list.add(orderLine);
		list.add(orderLine2);
		order.setOrderLines(list);
		dao.save(orderLine);
		Long id = Long.valueOf(1);
		given(dao.existsById(id)).willReturn(true);
		given(dao.getOne(id)).willReturn(orderLine);
		given(orderService.getOrder(anyLong())).willReturn(order);
		service.deleteOrderLine(id, "", Collections.emptyList());
		given(dao.existsById(id)).willReturn(false);
		assertFalse(dao.existsById(id));
	}

	@Test(expected = OrderLineNotFoundException.class)
	public void testDeleteOrderLineNotFound() throws OrderLineNotFoundException, OrderNotFoundException {
		given(dao.existsById(anyLong())).willReturn(false);
		service.deleteOrderLine(Long.valueOf(1), "", Collections.emptyList());
	}
}
