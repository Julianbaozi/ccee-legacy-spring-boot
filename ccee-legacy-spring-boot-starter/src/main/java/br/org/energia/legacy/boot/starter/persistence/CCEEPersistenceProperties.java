package br.org.energia.legacy.boot.starter.persistence;

import org.springframework.util.StringUtils;

public class CCEEPersistenceProperties {

	private String entityPackage;
	
	private String unitName;
	
	private String ddlAction;
	
	private Boolean enabledVigenciaInterceptor = Boolean.TRUE;
	
	private Boolean enabledJpqlTranslator = Boolean.TRUE;

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public String getEntityPackage() {
		return entityPackage;
	}

	public void setEntityPackage(String entityPackage) {
		this.entityPackage = entityPackage;
	}

	public Boolean isEnabledVigenciaInterceptor() {
		return enabledVigenciaInterceptor;
	}

	public void setEnabledVigenciaInterceptor(Boolean enabledVigenciaInterceptor) {
		this.enabledVigenciaInterceptor = enabledVigenciaInterceptor;
	}

	public Boolean isEnabledJpqlTranslator() {
		return enabledJpqlTranslator;
	}

	public void setEnabledJpqlTranslator(Boolean enabledJpqlTranslator) {
		this.enabledJpqlTranslator = enabledJpqlTranslator;
	}

	public boolean isEnabledDDLActions() {
		return !StringUtils.isEmpty(getDdlAction());
	}
	
	public String getDdlAction() {
		return ddlAction;
	}

	public void setDdlAction(String ddlAction) {
		this.ddlAction = ddlAction;
	}
}
