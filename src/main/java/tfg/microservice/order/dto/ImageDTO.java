package tfg.microservice.order.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class ImageDTO implements Serializable {
	private static final long serialVersionUID = 2868430411020089105L;
	private long id;
	private String url;
}
