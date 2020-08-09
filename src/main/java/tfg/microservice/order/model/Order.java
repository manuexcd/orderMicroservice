package tfg.microservice.order.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "order_table")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Data
@RequiredArgsConstructor
public class Order implements Serializable {

	private static final long serialVersionUID = 6892693125355139371L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(unique = true, nullable = false)
	private Long id;
	@Column(unique = false, nullable = false)
	private double totalPrice = 0;
	@Column(unique = false, nullable = false)
	@JsonSerialize(using = CustomDateSerializer.class)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp date;
	@Column(unique = false, nullable = false)
	private String orderStatus = Constants.ORDER_STATUS_TEMPORAL;
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OrderLine> orderLines;
	@Column(unique = false, nullable = false)
	private Long userId;
}
