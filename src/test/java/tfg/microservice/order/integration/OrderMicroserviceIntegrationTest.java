package tfg.microservice.order.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import tfg.microservice.order.dto.ProductDTO;
import tfg.microservice.order.dto.UserDTO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMicroserviceIntegrationTest {

	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	private String productUrl = "http://localhost:8089/products/";
	private String userUrl = "http://localhost:8088/users/";

	@Test
	public void testProductMicroserviceOk() throws Exception {
		String productName = "Producto 1";

		ResponseEntity<ProductDTO> response = testRestTemplate.getForEntity(productUrl.concat(String.valueOf(1L)),
				ProductDTO.class);
		ProductDTO product = response.getBody();
		HttpStatus status = response.getStatusCode();

		assertEquals(HttpStatus.OK, status);
		assertEquals(1L, product.getId());
		assertEquals(productName, product.getName());
	}

	@Test
	public void testProductMicroserviceNotFound() throws Exception {
		HttpStatus status = testRestTemplate.getForEntity(productUrl.concat(String.valueOf(100L)), ProductDTO.class)
				.getStatusCode();

		assertEquals(HttpStatus.NOT_FOUND, status);
	}

	@Test
	public void testUserMicroservice() throws Exception {
		HttpStatus status = testRestTemplate.getForEntity(userUrl.concat(String.valueOf(1L)), UserDTO.class)
				.getStatusCode();

		assertTrue(status.equals(HttpStatus.FORBIDDEN));
	}
}
