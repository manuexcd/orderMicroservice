package tfg.microservice.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tfg.microservice.order.dto.ProductDTO;
import tfg.microservice.order.dto.UserDTO;
import tfg.microservice.order.exception.OrderLineNotFoundException;
import tfg.microservice.order.exception.OrderNotFoundException;
import tfg.microservice.order.model.Constants;
import tfg.microservice.order.model.Order;
import tfg.microservice.order.model.OrderLine;
import tfg.microservice.order.service.OrderLineService;
import tfg.microservice.order.service.OrderService;

@RestController
@RequestMapping(value = Constants.PATH_ORDERLINES)
public class OrderLineController {

	@Autowired
	private OrderLineService orderLineService;

	@Autowired
	private OrderService orderService;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${microservices.products.url}")
	private String productUrl;

	@Value("${microservices.users.url}")
	private String userUrl;

	@GetMapping(value = Constants.PARAM_ID)
	public ResponseEntity<OrderLine> getOneOrderLine(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(orderLineService.getOrderLine(id), HttpStatus.OK);
		} catch (OrderLineNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = Constants.PARAM_ID)
	public ResponseEntity<OrderLine> deleteOrderLine(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
		try {
			Order order = orderService.getOrder(orderLineService.getOrderLine(id).getOrder().getId());
			List<String> token = headers.get("Authorization");
			HttpHeaders restTemplateHeaders = new HttpHeaders();
			restTemplateHeaders.set("Authorization", token.get(0));
			HttpEntity<String> entity = new HttpEntity<>(restTemplateHeaders);
			UserDTO userDto = restTemplate
					.exchange(userUrl.concat(String.valueOf(order.getUserId())), HttpMethod.GET, entity, UserDTO.class)
					.getBody();
			List<ProductDTO> listProductDto = new ArrayList<>();
			order.getOrderLines().forEach(line -> listProductDto.add(this.getProduct(headers, line.getProductId())));
			orderLineService.deleteOrderLine(id, userDto.getEmail(), listProductDto);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (OrderLineNotFoundException | OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private ProductDTO getProduct(HttpHeaders headers, Long productId) {
		List<String> token = headers.get(Constants.HEADER_AUTHORIZATION);
		HttpHeaders restTemplateHeaders = new HttpHeaders();
		restTemplateHeaders.set(Constants.HEADER_AUTHORIZATION, token.get(0));
		HttpEntity<String> entity = new HttpEntity<>(restTemplateHeaders);

		return restTemplate
				.exchange(productUrl.concat(String.valueOf(productId)), HttpMethod.GET, entity, ProductDTO.class)
				.getBody();
	}
}
