package br.com.marph.selenium.enums;

public enum EnumMensagensSistema {
	
	ERRO("Existem erros no formulário."),
	PREENCHIMENTO_OBRIGATORIO("Preenchimento obrigatório!");
	
	private String mensagem;

	private EnumMensagensSistema(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getMensagem() {
		return mensagem;
	}
}
