package tfg.miroservice.order.dto;

import java.io.Serializable;

import lombok.Data;
import lombok.ToString;

@Data
public class OrderLineDTO implements Serializable {
	private static final long serialVersionUID = 4745195410724554377L;
	@ToString.Exclude
	private Long id;
	private Long productId;
	private Integer quantity;
}
