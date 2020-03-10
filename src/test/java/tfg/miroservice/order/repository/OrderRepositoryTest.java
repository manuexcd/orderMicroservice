package tfg.miroservice.order.repository;

import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import tfg.miroservice.order.model.Order;

@RunWith(SpringRunner.class)
@DataJpaTest
public class OrderRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private OrderRepository orders;

	@Before
	public void setUp() {
		Order order = new Order();
		order.setUserId(Long.valueOf(1));
		order.setDate(new Timestamp(System.currentTimeMillis()));
		entityManager.persist(order);
		entityManager.flush();
	}

	@Test
	public void findAll() {
		assertNotNull(orders.findAll());
	}

	@Test
	public void findById() {
		assertNotNull(orders.findById(Long.valueOf(1)));
	}
}