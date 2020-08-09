package tfg.microservice.order.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class OrderDTO implements Serializable {

	private static final long serialVersionUID = 6892693125355139371L;
	private Long id;
	private double totalPrice = 0;
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Timestamp date;
	private String orderStatus;
	private List<OrderLineDTO> orderLines;
	private Long userId;
}
