package tfg.microservice.order.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import tfg.microservice.order.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

	public Page<Order> findAllByOrderByOrderStatus(Pageable page);

	public Page<Order> findAllByOrderByDate(Pageable page);

	public Page<Order> findAllByOrderByDateDesc(Pageable page);
	
	public List<Order> findByUserId(long userId);

	public Page<Order> findByUserId(long userId, Pageable page);

	public Page<Order> findByUserIdOrderByDate(long userId, Pageable page);

	public Page<Order> findByUserIdOrderByDateDesc(long userId, Pageable page);

	public List<Order> findByOrderStatus(String orderStatus);

	@Query("SELECT o FROM Order o WHERE o.orderStatus LIKE %?1% OR o.totalPrice LIKE %?1%")
	public Page<Order> findByParam(String param, Pageable page);
	
	@Query("SELECT o FROM Order o WHERE o.orderStatus LIKE %?1% OR o.totalPrice LIKE %?1% AND o.userId LIKE %?2%")
	public Page<Order> findByParamAndUser(String param, long userId, Pageable page);
}