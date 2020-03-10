package tfg.miroservice.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import tfg.miroservice.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	public Page<Order> findAllByOrderByOrderStatus(Pageable page);

	public Page<Order> findAllByOrderByDate(Pageable page);

	public Page<Order> findByUserId(long userId, Pageable page);

	public List<Order> findByOrderStatus(String orderStatus);
}