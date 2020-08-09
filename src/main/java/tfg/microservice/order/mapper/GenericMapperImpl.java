package tfg.microservice.order.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public abstract class GenericMapperImpl<E, D> implements GenericMapper<E, D> {

	@Autowired
	protected ModelMapper mapper;

	public abstract Class<E> getClazz();

	public abstract Class<D> getDtoClazz();

	@Override
	public E mapDtoToEntity(D dto) {
		return mapper.map(dto, getClazz());
	}

	@Override
	public D mapEntityToDto(E entity) {
		return mapper.map(entity, getDtoClazz());
	}

	@Override
	public Page<E> mapDtoPageToEntityPage(Page<D> dtoPage) {
		List<D> dtoList = new ArrayList<>();
		dtoPage.stream().forEach(dtoList::add);
		List<E> entityList = new ArrayList<>();
		dtoList.stream().forEach(dto -> entityList.add(mapDtoToEntity(dto)));
		return new PageImpl<>(entityList, dtoPage.getPageable(), dtoPage.getTotalElements());
	}

	@Override
	public Page<D> mapEntityPageToDtoPage(Page<E> entityPage) {
		List<E> entityList = new ArrayList<>();
		entityPage.getContent().stream().forEach(entityList::add);
		List<D> dtoList = new ArrayList<>();
		entityList.stream().forEach(entity -> dtoList.add(mapEntityToDto(entity)));
		return new PageImpl<>(dtoList, entityPage.getPageable(), entityPage.getTotalElements());
	}

	@Override
	public List<E> mapDtoListToEntityList(List<D> dtoList) {
		List<E> entityList = new ArrayList<>();
		dtoList.stream().forEach(dto -> entityList.add(mapDtoToEntity(dto)));
		return entityList;
	}

	@Override
	public List<D> mapEntityListToDtoList(List<E> entityList) {
		List<D> dtoList = new ArrayList<>();
		entityList.stream().forEach(entity -> dtoList.add(mapEntityToDto(entity)));
		return dtoList;
	}
}