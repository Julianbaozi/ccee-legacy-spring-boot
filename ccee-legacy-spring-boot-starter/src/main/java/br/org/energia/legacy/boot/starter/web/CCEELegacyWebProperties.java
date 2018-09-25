package br.org.energia.legacy.boot.starter.web;

public class CCEELegacyWebProperties {

	private Boolean enabledPortal = Boolean.FALSE;

	private Boolean enabledSiteMesh = Boolean.FALSE;
	
	public Boolean isEnabledPortal() {
		return enabledPortal;
	}

	public void setEnabledPortal(Boolean enabledPortal) {
		this.enabledPortal = enabledPortal;
	}

	public Boolean isEnabledSiteMesh() {
		return enabledSiteMesh;
	}

	public void setEnabledSiteMesh(Boolean enabledSiteMesh) {
		this.enabledSiteMesh = enabledSiteMesh;
	}

}