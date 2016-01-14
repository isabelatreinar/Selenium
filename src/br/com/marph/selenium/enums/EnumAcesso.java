package br.com.marph.selenium.enums;

public enum EnumAcesso {
	ADMINISTRADOR("17069");
	
	private String id;

	private EnumAcesso(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
	
	
	
}
