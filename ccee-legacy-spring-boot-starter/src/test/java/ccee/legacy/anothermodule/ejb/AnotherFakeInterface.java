package ccee.legacy.anothermodule.ejb;

import javax.ejb.Local;
import javax.ejb.Remote;

@Local
@Remote
public interface AnotherFakeInterface {

	public String JNDI_NAME = "ejb/AnotherFakeInterface";

	public void testMethod();	

}
