package tfg.miroservice.order.mapper;

import java.util.List;

import org.springframework.data.domain.Page;

public interface GenericMapper<E, D> {
	public E mapDtoToEntity(D dto);

	public D mapEntityToDto(E entity);

	public Page<E> mapDtoPageToEntityPage(Page<D> dtoPage);

	public Page<D> mapEntityPageToDtoPage(Page<E> entityPage);

	public List<E> mapDtoListToEntityList(List<D> dtoList);

	public List<D> mapEntityListToDtoList(List<E> entityList);
}
