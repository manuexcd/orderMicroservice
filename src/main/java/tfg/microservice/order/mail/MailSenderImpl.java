package tfg.microservice.order.mail;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

import com.samskivert.mustache.Mustache;

@Component
public class MailSenderImpl implements MailSender {

	@Autowired
	private JavaMailSenderImpl emailSender;

	@Override
	public void sendEmail(String to, String subject, String body, Map<Object, Object> params) {
		Mustache.compiler().compile(body).execute(params);
		SimpleMailMessage message = new SimpleMailMessage();
		message.setTo("manuexcd@gmail.com");
		message.setSubject(subject);
		message.setText(Mustache.compiler().compile(body).execute(params));
		emailSender.send(message);
	}
}
