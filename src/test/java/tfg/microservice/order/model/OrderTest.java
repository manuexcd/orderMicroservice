package tfg.microservice.order.model;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import tfg.microservice.order.model.Order;
import tfg.microservice.order.model.OrderLine;

@RunWith(MockitoJUnitRunner.class)
public class OrderTest {

	@Test
	public void updatePrice() {
		Order order = new Order();
		OrderLine orderLine = new OrderLine();
		orderLine.setQuantity(0);
		order.setOrderLines(Arrays.asList(orderLine));
		assertTrue(order.getTotalPrice() == 0);
	}
}
