package br.com.marph.selenium.exceptions;

import java.util.List;

import br.com.marph.selenium.enums.EnumMensagens;
import br.com.marph.selenium.utils.LogUtils;

public class TesteAutomatizadoException extends Exception {
	private static final long serialVersionUID = 2846760922006894033L;

	/**
	 * Excessao customizada para o teste automatizado
	 * 
	 * @param erro
	 *            Enumerador da mensagem de erro
	 * @param clazz
	 *            Classe de onde o erro esta sendo gerado
	 */
	public TesteAutomatizadoException(EnumMensagens erro, String metodo) {
		super(erro.getMensagem());
		LogUtils.log(erro, metodo);
	}
	
	public TesteAutomatizadoException(List<String> mensagens, String metodo) {
		StringBuilder sb = new StringBuilder();
		for (String string : mensagens) {
			sb.append(string).append("\n");
		}
		LogUtils.logError(sb.toString(), metodo);
	}

}
