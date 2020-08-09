package tfg.microservice.order;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import tfg.microservice.order.OrderApplication;

@SpringBootTest
class OrderApplicationTests {

	@Test
	public void main() {
		OrderApplication.main(new String[] {});
		assertTrue(true);
	}

	@Test
	void contextLoads() {
		assertTrue(true);
	}
}
