package ccee.legacy.test.jms;

import java.io.Serializable;

public class FakeEmail implements Serializable {

	private static final long serialVersionUID = -4572365098331706151L;

	private String email = "teste@teste.com";
	private String message = "msg text";

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
