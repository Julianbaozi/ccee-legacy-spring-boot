package ccee.legacy.test.ejb;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

import ccee.legacy.anothermodule.ejb.AnotherFakeInterface;
import ccee.legacy.test.jms.FakeEmail;

@Stateless
public class FakeStateless implements FakeInterface {
	
	@EJB(name="anotherFakeInterface")
	private AnotherFakeInterface anotherFakeInterface;
	
	@Autowired
	private JmsTemplate jmsTemplate;

	@Override
	public void testMethod() {
	}
	
	@Transactional
	public void testSendJMS() {

		//JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
		jmsTemplate.setSessionTransacted(true);
		jmsTemplate.convertAndSend("jms/fakeQueue", new FakeEmail());
		System.out.println("FIM");

	}
}
