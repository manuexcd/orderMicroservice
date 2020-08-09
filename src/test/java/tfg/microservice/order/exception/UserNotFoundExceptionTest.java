package tfg.microservice.order.exception;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import tfg.microservice.order.exception.UserNotFoundException;

@RunWith(MockitoJUnitRunner.class)
public class UserNotFoundExceptionTest {

	@Test
	public void userNotFoundException() {
		assertNotNull(new UserNotFoundException());
	}
}
