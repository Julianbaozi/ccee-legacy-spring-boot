package ccee.legacy.test.ejb;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
@Remote
public interface FakeInterface {
	

	public String JNDI_NAME = "ejb/FakeStatelessWithAttribute";

	public void testMethod();
	
}
