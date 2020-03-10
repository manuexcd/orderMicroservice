package tfg.miroservice.order.exception;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineNotFoundExceptionTest {

	@Test
	public void orderLineNotFoundException() {
		assertNotNull(new OrderLineNotFoundException());
	}
}