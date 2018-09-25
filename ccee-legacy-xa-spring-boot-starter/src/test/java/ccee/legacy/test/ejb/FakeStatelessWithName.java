package ccee.legacy.test.ejb;

import javax.ejb.Stateless;

@Stateless(name="ejb/FakeStatelessWithName")
public class FakeStatelessWithName implements FakeInterface {

	@Override
	public void testMethod() {
	}
	
}
