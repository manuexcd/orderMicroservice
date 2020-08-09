package tfg.miroservice.order.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import tfg.miroservice.order.dto.OrderDTO;
import tfg.miroservice.order.dto.OrderLineDTO;
import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.dto.UserDTO;
import tfg.miroservice.order.exception.OrderNotFoundException;
import tfg.miroservice.order.exception.UserNotFoundException;
import tfg.miroservice.order.mapper.OrderLineMapper;
import tfg.miroservice.order.mapper.OrderMapper;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;
import tfg.miroservice.order.service.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class OrderControllerTest {

	private MockMvc mvc;

	@Mock
	private OrderService service;

	@Mock
	private OrderMapper mapper;

	@Mock
	private OrderLineMapper orderLineMapper;

	@InjectMocks
	private OrderController controller;

	@Mock
	private RestTemplate restTemplate;

	private Pageable pageRequest;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testGetAllOrders() throws Exception {
		mvc.perform(get("/orders?userId=").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersByUser() throws Exception {
		mvc.perform(get("/orders?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersDateAsc() throws Exception {
		mvc.perform(get("/orders/dateasc?userId=").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersByUserDateAsc() throws Exception {
		mvc.perform(get("/orders/dateasc?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersDateDesc() throws Exception {
		mvc.perform(get("/orders/datedesc?userId=").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersByUserDateDesc() throws Exception {
		mvc.perform(get("/orders/datedesc?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersParam() throws Exception {
		mvc.perform(get("/orders/param/aaa?userId=").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersByUserParam() throws Exception {
		mvc.perform(get("/orders/param/aaa?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetAllOrdersByOrderStatus() throws Exception {
		mvc.perform(get("/orders/status").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetOrderLines() throws Exception {
		OrderLine orderLine = new OrderLine();
		OrderLineDTO orderLineDto = new OrderLineDTO();
		given(service.getOrderLines(anyLong())).willReturn(Arrays.asList(orderLine));
		given(orderLineMapper.mapEntityListToDtoList(any())).willReturn(Arrays.asList(orderLineDto));
		mvc.perform(get("/orders/1/lines").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetOrderLinesNotFound() throws Exception {
		given(service.getOrderLines(anyLong())).willThrow(new OrderNotFoundException());
		mvc.perform(get("/orders/1/lines").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testGetOrder() throws Exception {
		Order order = new Order();
		given(service.getOrder(anyLong())).willReturn(order);
		mvc.perform(get("/orders/1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetOrderNotFound() throws Exception {
		given(service.getOrder(anyLong())).willThrow(new OrderNotFoundException());
		mvc.perform(get("/orders/1").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testGetTemporalOrder() throws Exception {
		mvc.perform(get("/orders/temporal?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testGetTemporalOrderException() throws Exception {
		given(service.getTemporalOrder(anyLong())).willThrow(OrderNotFoundException.class);
		mvc.perform(get("/orders/temporal?userId=1").contentType(APPLICATION_JSON)).andExpect(status().isNoContent());
	}

	@Test
	public void testDeleteOrder() throws Exception {
		mvc.perform(delete("/orders/1").contentType(APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void testDeleteOrderNotFound() throws Exception {
		Mockito.doThrow(new OrderNotFoundException()).when(service).deleteOrder(anyLong());
		mvc.perform(delete("/orders/1").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testCreateTemporalOrder() throws Exception {
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setId(Long.valueOf(1));
		line.setProductId(Long.valueOf(1));
		line.setQuantity(1);
		line.setOrder(order);
		order.setId(Long.valueOf(1));
		order.setUserId(Long.valueOf(1));
		order.setOrderLines(Arrays.asList(line));
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		productDto.setStockAvailable(Integer.valueOf(100));
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		OrderDTO orderDto = new OrderDTO();
		OrderLineDTO lineDto = new OrderLineDTO();
		lineDto.setId(Long.valueOf(1));
		lineDto.setProductId(Long.valueOf(1));
		lineDto.setQuantity(1);
		orderDto.setId(Long.valueOf(1));
		orderDto.setUserId(Long.valueOf(1));
		orderDto.setOrderLines(Arrays.asList(lineDto));
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ < order.getOrderLines().size())
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		ObjectMapper obj = new ObjectMapper();
		mvc.perform(post("/orders?userId=1").header("Authorization", "token").content(obj.writeValueAsString(orderDto))
				.contentType(APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testUpdateOrder() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setId(Long.valueOf(1));
		line.setProductId(Long.valueOf(1));
		line.setQuantity(1);
		line.setOrder(order);
		order.setId(Long.valueOf(1));
		order.setUserId(Long.valueOf(1));
		order.setOrderLines(Arrays.asList(line));
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		productDto.setStockAvailable(Integer.valueOf(100));
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		OrderDTO orderDto = new OrderDTO();
		OrderLineDTO lineDto = new OrderLineDTO();
		lineDto.setId(Long.valueOf(1));
		lineDto.setProductId(Long.valueOf(1));
		lineDto.setQuantity(1);
		orderDto.setId(Long.valueOf(1));
		orderDto.setUserId(Long.valueOf(1));
		orderDto.setOrderLines(Arrays.asList(lineDto));
		given(mapper.mapDtoToEntity(any())).willReturn(order);
		given(mapper.mapEntityToDto(any())).willReturn(orderDto);
		given(service.updateOrder(any(), anyString(), any())).willReturn(order);
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ < order.getOrderLines().size())
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		ObjectMapper obj = new ObjectMapper();
		mvc.perform(put("/orders").header("Authorization", "token").content(obj.writeValueAsString(orderDto))
				.contentType(APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testUpdateOrderException() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		ObjectMapper obj = new ObjectMapper();
		mvc.perform(put("/orders").content(obj.writeValueAsString(new OrderDTO())).contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testConfirmTemporalOrder() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setId(Long.valueOf(1));
		line.setProductId(Long.valueOf(1));
		line.setQuantity(1);
		line.setOrder(order);
		order.setId(Long.valueOf(1));
		order.setUserId(Long.valueOf(1));
		order.setOrderLines(Arrays.asList(line));
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		productDto.setStockAvailable(Integer.valueOf(100));
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		OrderDTO orderDto = new OrderDTO();
		OrderLineDTO lineDto = new OrderLineDTO();
		lineDto.setId(Long.valueOf(1));
		lineDto.setProductId(Long.valueOf(1));
		lineDto.setQuantity(1);
		orderDto.setId(Long.valueOf(1));
		orderDto.setUserId(Long.valueOf(1));
		orderDto.setOrderLines(Arrays.asList(lineDto));
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ < order.getOrderLines().size())
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		ObjectMapper obj = new ObjectMapper();
		mvc.perform(put("/orders/temporal").header("Authorization", "token").content(obj.writeValueAsString(orderDto))
				.contentType(APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testConfirmTemporalOrderNoStock() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setId(Long.valueOf(1));
		line.setProductId(Long.valueOf(1));
		line.setQuantity(1);
		line.setOrder(order);
		order.setId(Long.valueOf(1));
		order.setUserId(Long.valueOf(1));
		order.setOrderLines(Arrays.asList(line));
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		productDto.setStockAvailable(Integer.valueOf(0));
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ < order.getOrderLines().size())
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		ObjectMapper obj = new ObjectMapper();
		OrderDTO orderDto = new OrderDTO();
		OrderLineDTO lineDto = new OrderLineDTO();
		lineDto.setId(Long.valueOf(1));
		lineDto.setProductId(Long.valueOf(1));
		lineDto.setQuantity(1);
		orderDto.setOrderLines(Arrays.asList(lineDto));
		mvc.perform(put("/orders/temporal").header("Authorization", "token").content(obj.writeValueAsString(orderDto))
				.contentType(APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testConfirmTemporalOrderNotFound() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		Order order = new Order();
		OrderLine line = new OrderLine();
		line.setId(Long.valueOf(1));
		line.setProductId(Long.valueOf(1));
		line.setQuantity(1);
		line.setOrder(order);
		order.setId(Long.valueOf(1));
		order.setUserId(Long.valueOf(1));
		order.setOrderLines(Arrays.asList(line));
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		productDto.setStockAvailable(Integer.valueOf(100));
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ < order.getOrderLines().size())
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		ObjectMapper obj = new ObjectMapper();
		OrderDTO orderDto = new OrderDTO();
		OrderLineDTO lineDto = new OrderLineDTO();
		lineDto.setId(Long.valueOf(1));
		lineDto.setProductId(Long.valueOf(1));
		lineDto.setQuantity(1);
		orderDto.setOrderLines(Arrays.asList(lineDto));
		given(service.confirmTemporalOrder(any(), anyString(), any())).willThrow(new OrderNotFoundException());
		mvc.perform(put("/orders/temporal").header("Authorization", "token").content(obj.writeValueAsString(orderDto))
				.contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testCancelOrder() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		Order order = new Order();
		order.setId(Long.valueOf(1));
		given(service.getOrder(anyLong())).willReturn(order);
		given(restTemplate.exchange(anyString(), any(), any(), (Class<UserDTO>) any(Class.class)))
				.willReturn(new ResponseEntity<UserDTO>(userDto, HttpStatus.OK));
		mvc.perform(put("/orders/1/cancel").header("Authorization", "token")).andExpect(status().is2xxSuccessful());
	}

	@Test
	public void testCancelOrderException() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		mvc.perform(put("/orders/1/cancel").header("Authorization", "token")).andExpect(status().isNotFound());
	}
}
