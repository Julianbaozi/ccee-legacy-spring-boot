package ccee.legacy.test.jms;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.ObjectMessage;

import org.springframework.stereotype.Component;

@Component
@MessageDriven(activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
		@ActivationConfigProperty(propertyName = "destination", propertyValue = "jms/fakeQueue") })
public class FakeMDB implements MessageListener {

	@Override
	public void onMessage(Message message) {
		try {

			System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES", "*");
			
			ObjectMessage objectMessage = (ObjectMessage) message;

			FakeEmail email = (FakeEmail) objectMessage.getObject();
			System.out.println(email.getEmail() + " : " + email.getMessage());
			
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

}
