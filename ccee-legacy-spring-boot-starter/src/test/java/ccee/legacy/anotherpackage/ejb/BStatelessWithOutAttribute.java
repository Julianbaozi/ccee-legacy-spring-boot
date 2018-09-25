package ccee.legacy.anotherpackage.ejb;

import javax.ejb.Stateless;

import ccee.legacy.test.ejb.FakeInterface;

@Stateless(name = "ejb/BStatelessWithOutAttribute", mappedName = "ejb/BStatelessWithOutAttribute")
public class BStatelessWithOutAttribute implements FakeInterface {

	@Override
	public void testMethod() {
	}
	
}
