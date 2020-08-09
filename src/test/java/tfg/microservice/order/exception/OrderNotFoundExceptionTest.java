package tfg.microservice.order.exception;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import tfg.microservice.order.exception.OrderNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class OrderNotFoundExceptionTest {

	@Test
	public void orderNotFoundException() {
		assertNotNull(new OrderNotFoundException());
	}
}
