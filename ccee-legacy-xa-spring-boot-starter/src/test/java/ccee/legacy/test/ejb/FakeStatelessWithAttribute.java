package ccee.legacy.test.ejb;

import javax.ejb.Stateless;

import ccee.legacy.test.ejb.FakeInterface;

@Stateless
public class FakeStatelessWithAttribute implements FakeInterface {

	
	@Override
	public void testMethod() {
	}
	
}
