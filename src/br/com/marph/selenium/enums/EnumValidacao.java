package br.com.marph.selenium.enums;

public enum EnumValidacao {
	MARCACAO_ERRO("chosen-container chosen-container-single chosen-container-active");
	
	private String html;

	private EnumValidacao(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}
}
