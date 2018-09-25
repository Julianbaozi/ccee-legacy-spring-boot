package ccee.legacy.test.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FakeEntity implements Serializable {

	private static final long serialVersionUID = -8203702655937026146L;

	@Id
	private long codigoProcessamentoAssincrono;

	public long getCodigoProcessamentoAssincrono() {
		return codigoProcessamentoAssincrono;
	}

	public void setCodigoProcessamentoAssincrono(long codigoProcessamentoAssincrono) {
		this.codigoProcessamentoAssincrono = codigoProcessamentoAssincrono;
	}

}
