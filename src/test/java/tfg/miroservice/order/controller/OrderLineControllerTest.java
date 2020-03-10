package tfg.miroservice.order.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;

import tfg.miroservice.order.dto.ProductDTO;
import tfg.miroservice.order.dto.UserDTO;
import tfg.miroservice.order.exception.OrderLineNotFoundException;
import tfg.miroservice.order.model.Order;
import tfg.miroservice.order.model.OrderLine;
import tfg.miroservice.order.service.OrderLineService;
import tfg.miroservice.order.service.OrderService;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineControllerTest {

	private MockMvc mvc;

	@Mock
	private OrderLineService service;

	@Mock
	private OrderService orderService;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private OrderLineController controller;

	@Before
	public void setup() {
		mvc = MockMvcBuilders.standaloneSetup(controller).build();
	}

	@Test
	public void testGetOrderLine() throws Exception {
		OrderLine orderLine = new OrderLine();
		orderLine.setProductId(Long.valueOf(1));
		orderLine.setOrder(new Order());
		orderLine.setQuantity(1);
		given(service.getOrderLine(anyLong())).willReturn(orderLine);
		mvc.perform(get("/lines/1").contentType(APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().string(containsString("quantity")));
	}

	@Test
	public void testGetOrderLineNotFound() throws Exception {
		given(service.getOrderLine(anyLong())).willThrow(new OrderLineNotFoundException());
		mvc.perform(get("/lines/1").contentType(APPLICATION_JSON)).andExpect(status().isNotFound());
	}

	@Test
	public void testDeleteOrderLine() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		OrderLine orderLine = new OrderLine();
		Order order = new Order();
		order.setUserId(Long.valueOf(1));
		orderLine.setOrder(order);
		orderLine.setProductId(Long.valueOf(1));
		List<OrderLine> orderLines = new ArrayList<OrderLine>();
		orderLines.add(orderLine);
		order.setOrderLines(orderLines);
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		given(service.getOrderLine(anyLong())).willReturn(orderLine);
		given(orderService.getOrder(orderLine.getId())).willReturn(order);
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ == 1)
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		mvc.perform(delete("/lines/1").header("Authorization", "token").contentType(APPLICATION_JSON))
				.andExpect(status().isOk());
	}

	@Test
	public void testDeleteOrderLineNotFound() throws Exception {
		ReflectionTestUtils.setField(controller, "userUrl", "http://foo");
		ReflectionTestUtils.setField(controller, "productUrl", "http://foo");
		OrderLine orderLine = new OrderLine();
		Order order = new Order();
		order.setUserId(Long.valueOf(1));
		orderLine.setOrder(order);
		orderLine.setProductId(Long.valueOf(1));
		List<OrderLine> orderLines = new ArrayList<OrderLine>();
		orderLines.add(orderLine);
		order.setOrderLines(orderLines);
		UserDTO userDto = new UserDTO();
		userDto.setEmail("");
		ProductDTO productDto = new ProductDTO();
		productDto.setId(Long.valueOf(1));
		productDto.setPrice(Double.valueOf(10));
		given(service.getOrderLine(anyLong())).willReturn(orderLine);
		given(orderService.getOrder(orderLine.getId())).willReturn(order);
		given(restTemplate.exchange(anyString(), any(), any(), (Class<ProductDTO>) any(Class.class)))
				.willAnswer(new Answer<Object>() {
					private int count = 0;

					public Object answer(InvocationOnMock invocation) {
						if (count++ == 1)
							return new ResponseEntity<ProductDTO>(productDto, HttpStatus.OK);
						else
							return new ResponseEntity<UserDTO>(userDto, HttpStatus.OK);
					}
				});
		Mockito.doThrow(new OrderLineNotFoundException()).when(service).deleteOrderLine(anyLong(), anyString(), any());
		mvc.perform(delete("/lines/1").header("Authorization", "token").contentType(APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}
}
