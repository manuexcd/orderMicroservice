package tfg.miroservice.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tfg.miroservice.order.model.OrderLine;

@Repository("OrderLineDAO")
public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {

}
