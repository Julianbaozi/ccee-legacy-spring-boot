package ccee.legacy.test.ejb;

public class TestFacade {

	private FakeStateless fakeStateless;

	public FakeStateless getFakeStateless() {
		return fakeStateless;
	}

	public void setFakeStateless(FakeStateless fakeStateless) {
		this.fakeStateless = fakeStateless;
	}
	
}
