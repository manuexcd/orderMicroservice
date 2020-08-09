package tfg.microservice.order.mapper;

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

import tfg.microservice.order.dto.OrderLineDTO;
import tfg.microservice.order.mapper.OrderLineMapperImpl;
import tfg.microservice.order.model.OrderLine;

@RunWith(MockitoJUnitRunner.class)
public class OrderLineMapperTest {
	@Mock
	private ModelMapper modelMapper;

	@InjectMocks
	private OrderLineMapperImpl mapper;

	@Test
	public void testMapDtoToEntity() {
		OrderLineDTO dto = new OrderLineDTO();
		given(modelMapper.map(dto, OrderLine.class)).willReturn(new OrderLine());
		assertNotNull(mapper.mapDtoToEntity(dto));
	}

	@Test
	public void testMapEntityToDto() {
		OrderLine orderLine = new OrderLine();
		given(modelMapper.map(orderLine, OrderLineDTO.class)).willReturn(new OrderLineDTO());
		assertNotNull(mapper.mapEntityToDto(orderLine));
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
		assertNotNull(mapper.mapDtoListToEntityList(new ArrayList<OrderLineDTO>()));
	}

	@Test
	public void testMapEntityListToDtoList() {
		assertNotNull(mapper.mapEntityListToDtoList(new ArrayList<OrderLine>()));
	}
}
