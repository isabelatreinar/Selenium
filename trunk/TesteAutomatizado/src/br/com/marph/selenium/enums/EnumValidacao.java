package br.com.marph.selenium.enums;

public enum EnumValidacao {
	/**
	 * Quando os campos estão marcados com erro o atributo class da div "main" do campo recebe a classe "has-error"
	 */
	MARCACAO_ERRO("has-error");
	
	
	private String html;

	private EnumValidacao(String html) {
		this.html = html;
	}

	public String getHtml() {
		return html;
	}
}
