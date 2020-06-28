package tfg.miroservice.order.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import tfg.miroservice.order.dto.OrderDTO;
import tfg.miroservice.order.dto.OrderLineDTO;
import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.dto.UserDTO;
import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.exception.UserNotFoundException;
import tfg.miroservice.order.mapper.OrderLineMapper;
import tfg.miroservice.order.mapper.OrderMapper;
import tfg.miroservice.order.model.Constants;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;
import tfg.miroservice.order.service.OrderService;

@RestController
@RequestMapping(value = Constants.PATH_ORDERS)
public class OrderController {

	@Autowired
	private OrderService orderManager;

	@Autowired
	private OrderMapper mapper;

	@Autowired
	private OrderLineMapper orderLineMapper;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${microservices.products.url}")
	private String productUrl;

	@Value("${microservices.users.url}")
	private String userUrl;

	@GetMapping
	public ResponseEntity<Page<OrderDTO>> getAllOrders(@RequestParam Long userId,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) throws UserNotFoundException {
		if (userId != null)
			return new ResponseEntity<>(
					mapper.mapEntityPageToDtoPage(orderManager.getOrdersByUser(userId, PageRequest.of(page, pageSize))),
					HttpStatus.OK);
		else
			return new ResponseEntity<>(
					mapper.mapEntityPageToDtoPage(orderManager.getAllOrdersByDate(PageRequest.of(page, pageSize))),
					HttpStatus.OK);
	}

	@GetMapping(value = Constants.PATH_STATUS)
	public ResponseEntity<Page<OrderDTO>> getAllOrdersByOrderStatus(
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) {
		return new ResponseEntity<>(
				mapper.mapEntityPageToDtoPage(orderManager.getAllOrdersByOrderStatus(PageRequest.of(page, pageSize))),
				HttpStatus.OK);
	}

	@GetMapping(value = Constants.PARAM_ID + Constants.PATH_ORDERLINES)
	public ResponseEntity<List<OrderLineDTO>> getOrderLines(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(
					orderLineMapper.mapEntityListToDtoList((List<OrderLine>) orderManager.getOrderLines(id)),
					HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = Constants.PARAM_ID)
	public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
		try {
			return new ResponseEntity<>(mapper.mapEntityToDto(orderManager.getOrder(id)), HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = Constants.PATH_TEMPORAL)
	public ResponseEntity<OrderDTO> getTemporalOrder(@RequestParam Long userId,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_PAGE) int page,
			@RequestParam(defaultValue = Constants.PAGINATION_DEFAULT_SIZE) int pageSize) {
		try {
			return new ResponseEntity<>(
					mapper.mapEntityToDto(orderManager.getTemporalOrder(userId, PageRequest.of(page, pageSize))),
					HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
	}

	@PostMapping
	public ResponseEntity<OrderDTO> createTemporalOrder(@RequestHeader HttpHeaders headers, @RequestParam Long userId,
			@RequestBody OrderDTO dto) {
		List<ProductDTO> listProductDto = new ArrayList<>();
		dto.getOrderLines().forEach(lineDto -> listProductDto.add(this.getProduct(headers, lineDto.getProductId())));
		return new ResponseEntity<>(
				mapper.mapEntityToDto(
						orderManager.createTemporalOrder(userId, mapper.mapDtoToEntity(dto), listProductDto)),
				HttpStatus.OK);
	}

	@PutMapping
	public ResponseEntity<OrderDTO> updateOrder(@RequestHeader HttpHeaders headers, @RequestBody OrderDTO dto) {
		try {
			List<ProductDTO> listProductDto = new ArrayList<>();
			dto.getOrderLines()
					.forEach(lineDto -> listProductDto.add(this.getProduct(headers, lineDto.getProductId())));
			return new ResponseEntity<>(mapper.mapEntityToDto(orderManager.updateOrder(mapper.mapDtoToEntity(dto),
					this.getUserEmail(headers, dto.getUserId()), listProductDto)), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = Constants.PATH_TEMPORAL)
	public ResponseEntity<OrderDTO> confirmTemporalOrder(@RequestHeader HttpHeaders headers,
			@RequestBody OrderDTO dto) {
		try {
			List<ProductDTO> listProductDto = new ArrayList<>();
			dto.getOrderLines().forEach(lineDto -> {
				ProductDTO product = this.getProduct(headers, lineDto.getProductId());
				listProductDto.add(product);
				if (product.getStockAvailable() >= lineDto.getQuantity())
					product.setStockAvailable(product.getStockAvailable() - lineDto.getQuantity());
				else
					product.setStockAvailable(0);
				HttpEntity<ProductDTO> entityPut = new HttpEntity<>(product, headers);
				restTemplate.exchange(productUrl, HttpMethod.PUT, entityPut, ProductDTO.class);
			});

			return new ResponseEntity<>(
					mapper.mapEntityToDto(orderManager.confirmTemporalOrder(orderManager.getOrder(dto.getId()),
							this.getUserEmail(headers, dto.getUserId()), listProductDto)),
					HttpStatus.CREATED);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = Constants.PARAM_ORDER_ID + Constants.PATH_CANCEL)
	public ResponseEntity<OrderDTO> cancelOrder(@RequestHeader HttpHeaders headers, @PathVariable Long orderId) {
		try {
			Order order = orderManager.getOrder(orderId);
			return new ResponseEntity<>(
					mapper.mapEntityToDto(
							orderManager.cancelOrder(orderId, this.getUserEmail(headers, order.getUserId()))),
					HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = Constants.PARAM_ID)
	public ResponseEntity<OrderDTO> deleteOrder(@PathVariable Long id) {
		try {
			orderManager.deleteOrder(id);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (OrderNotFoundException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private String getUserEmail(HttpHeaders headers, Long userId) {
		List<String> token = headers.get(Constants.HEADER_AUTHORIZATION);
		HttpHeaders restTemplateHeaders = new HttpHeaders();
		restTemplateHeaders.set(Constants.HEADER_AUTHORIZATION, token.get(0));
		HttpEntity<String> entity = new HttpEntity<>(restTemplateHeaders);
		UserDTO userDto = restTemplate
				.exchange(userUrl.concat(String.valueOf(userId)), HttpMethod.GET, entity, UserDTO.class).getBody();

		return userDto.getEmail();
	}

	private ProductDTO getProduct(HttpHeaders headers, Long productId) {
		List<String> token = headers.get(Constants.HEADER_AUTHORIZATION);
		HttpHeaders restTemplateHeaders = new HttpHeaders();
		restTemplateHeaders.set(Constants.HEADER_AUTHORIZATION, token.get(0));
		HttpEntity<String> entity = new HttpEntity<>(restTemplateHeaders);

		System.out.println(headers.toString());

		return restTemplate
				.exchange(productUrl.concat(String.valueOf(productId)), HttpMethod.GET, entity, ProductDTO.class)
				.getBody();
	}
}