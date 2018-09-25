package br.org.energia.legacy.boot.starter.ejb;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

/**
 * Registrador de servicos remotos para serem consumidos por modulos externos.
 *
 * @author vscarin
 */

public class CCEELegacyHttpInvokerProperties {

	private Boolean enabledServices = Boolean.TRUE;

	private Boolean enabledClients = Boolean.TRUE;

	private String localContextPrefix;

	private Map<String, String> applicationPrefix;

	private List<ChaveValor> applicationsHost;

	private ApplicationContext applicationContext;

	public String getLocalContextPrefix() {
		return localContextPrefix;
	}

	public void setLocalContextPrefix(String localContextPrefix) {
		if (!localContextPrefix.startsWith("/")) {
			localContextPrefix = "/" + localContextPrefix;
		}
		this.localContextPrefix = localContextPrefix;
	}

	public Map<String, String> getApplicationPrefix() {

		if (applicationPrefix == null) {
			applicationPrefix = new HashMap<>();
			try {
				Resource[] resources = applicationContext.getResources("classpath*:/META-INF/context.properties");
				for (Resource props : resources) {
					loadApplicationPrefix(props);
				}
			} catch (IOException e) {
				throw new IllegalStateException("Error getting resources classpath*:/META-INF/context.properties", e);
			}

		}
		return applicationPrefix;
	}

	private void loadApplicationPrefix(Resource file) {
		try {
			InputStream inputStream = file.getInputStream();
			Properties props = new Properties();
			props.load(inputStream);
			for (Entry<Object, Object> entry : props.entrySet()) {
				applicationPrefix.put((String) entry.getKey(), (String) entry.getValue());
			}
		} catch (IOException e) {
			throw new IllegalStateException("Error when reading file " + file.getFilename(), e);
		}
	}

	public Boolean isEnabledServices() {
		return enabledServices;
	}

	public void setEnabledServices(Boolean enabledServices) {
		this.enabledServices = enabledServices;
	}

	public Boolean isEnabledClients() {
		return enabledClients;
	}

	public void setEnabledClients(Boolean enabledClients) {
		this.enabledClients = enabledClients;
	}

	public List<ChaveValor> getApplicationsHost() {
		return applicationsHost;
	}

	public void setApplicationsHost(List<ChaveValor> applicationsHost) {
		this.applicationsHost = applicationsHost;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

}