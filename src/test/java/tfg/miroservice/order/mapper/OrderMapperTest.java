package tfg.miroservice.order.mapper;

import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import tfg.miroservice.order.dto.OrderDTO;
import tfg.miroservice.order.model.Order;

@RunWith(MockitoJUnitRunner.class)
public class OrderMapperTest {
	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private OrderMapperImpl mapper;

	@Test
	public void testMapDtoToEntity() {
		OrderDTO dto = new OrderDTO();
		given(modelMapper.map(dto, Order.class)).willReturn(new Order());
		assertNotNull(mapper.mapDtoToEntity(dto));
	}

	@Test
	public void testMapEntityToDto() {
		Order order = new Order();
		given(modelMapper.map(order, OrderDTO.class)).willReturn(new OrderDTO());
		assertNotNull(mapper.mapEntityToDto(order));
	}

	@Test
	public void testMapDtoPageToEntityPage() {
		assertNotNull(mapper.mapDtoPageToEntityPage(Page.empty()));
	}

	@Test
	public void testMapEntityPageToDtoPage() {
		assertNotNull(mapper.mapEntityPageToDtoPage(Page.empty()));
	}

	@Test
	public void testMapDtoListToEntityList() {
		assertNotNull(mapper.mapDtoListToEntityList(new ArrayList<OrderDTO>()));
	}

	@Test
	public void testMapEntityListToDtoList() {
		assertNotNull(mapper.mapEntityListToDtoList(new ArrayList<Order>()));
	}
}
