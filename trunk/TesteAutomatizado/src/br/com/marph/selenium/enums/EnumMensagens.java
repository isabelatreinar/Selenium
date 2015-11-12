package br.com.marph.selenium.enums;

public enum EnumMensagens {
	CPF_INVALIDO("CPF inválido - Obrigatório\n"),
	CPF_JA_CADASTRADO("CPF já cadastrado\n"),
	CNPJ_INVALIDO("CNPJ inválido\n"),
	NOME_EM_BRANCO("Nome em branco - Obrigatório\n"),
	CPF_EM_BRANCO("CPF em branco - Obrigatório\n"),
	CARGO_EM_BRANCO("Cargo em branco - Obrigatório\n"),
	PERFIL_EM_BRANCO("Perfil em branco - Obrigatório\n"),
	EXTENSAO_EM_BRANCO("Extensao em branco - Obrigatório\n"),
	TELA_INCORRETA("Página acessada não é a correta\n"),
	BENEFICIARIO_INCORRETO("Beneficiário incorreto.\n"),
	BASE_LEGAL_VALIDADO("**Base legal validada.**", TipoMensagem.VALIDACAO),
	USUARIO_VALIDADO("**Usuario validado.**", TipoMensagem.VALIDACAO),
	USUARIO_NAO_VALIDADO("**Usuario não validado.**"),
	BASE_LEGAL_NAO_VALIDADO("**Base legal não validada.**\n"),
	CAGEC_VALIDADO("**Cagec validado.**"),
	CAGEC_NAO_VALIDADO("**Cagec não validado.**\n"),
	CAMPO_PREENCHIDO("Campo preenchido.\n"),
	TIPO_EM_BRANCO("Campo tipo estava em branco - Obrigatório"),
	NUMERO_EM_BRANCO("Campo numero estava em branco - Obrigatório\n"),
	DELIBERACAO_CADASTRADO("Existe uma Deliberação cadastrada com este número.\n"),
	DATA_PUBLICACAO_EM_BRANCO("Campo data publicação estava em branco - Obrigatório\n"),
	DATA_VIGENCIA_EM_BRANCO("Campo data vigencia estava em branco - Obrigatório\n"),
	PDF_EM_BRANCO("Campo PDF em branco - Obrigatório\n"),
	PDF_MAIOR("PDF maior que 5mb.\n"),
	QUANTIDADE_EXCEDIDA("Quantidade de registros excedida.\n"),
	ANO_EM_BRANCO("Ano em branco! - Obrigatório\n"),
	DATA_EM_BRANCO("Data em branco! - Obrigatório\n"),
	BREADCRUMB_INCORRETO("Breadcrumb incorreto.\n"),
	BASE_LEGAL_INCORRETA("Base legal incorreta !"),
	ERRO_HISTORICO("Erro na pesquisa do histórico.\n"),
	TOAST_DESABILITADO("Toast desabilitado.\n"),
	MENSAGEM_INCORRETA("Mensagem incorreta.\n"),
	USUARIO_ERRADO("Nome do usuario não corresponde com o clicado\n"),
	USUARIO_ERRADO_PERFIL("Nome do usuario não corresponde com o clicado em perfil\n"),
	RESOLUCAO_ERRADA("Numero da resolução não corresponde com o clicado\n"),
	TIPO_BASE_NAO_PODE_SER_EXCLUIDA("O tipo de base legal não pode ser excluído pois"
	+ " está vinculado a uma ou mais bases legais.\n"),
	TIPO_BASE_LEGAL_ERRO("Tipo base não pode ser excluido,pois ocorreu algum erro\n"),
	TRANSFERENCIA_DE_RECURSOS_EM_BRANCO("Transferencia de recusos está vazia.\n"),
	PRESTACAO_DE_METAS_EM_BRANCO("Prestação de metas está vazia.\n"),
	PRESTACAO_DE_CONTAS_EM_BRANCO("Prestação de contas está vazia.\n"),
	TIPO_BASE_LEGAL_VALIDADO("**Tipo base legal validado**\n",TipoMensagem.VALIDACAO),
	TIPO_BASE_LEGAL_NAO_VALIDO("**Tipo base não legal validada**\n",TipoMensagem.VALIDACAO),
	CONFIRMACAO_DESABILITADA("Não exibe popup de confirmação.\n"),
	BASE_LEGAL_JA_CADASTRADA("Já existe base legal cadastrada com esse numero.\n"),
	TIPO_DE_BASE_LEGAL_JA_CADASTRADA("Tipo de base legal já cadastrado.\n"),
	PROGRAMA_EM_BRANCO("Programa em branco.\n"),
	RESOLUCAO_JA_CADASTRADA("Ja existe uma resolução cadastrada com esse número\n"),
	BASE_LEGAL_EM_BRANCO("Base legal em branco!\n"),
	TEMPO_EM_BRANCO("Tempo em branco.\n"),
	PDF_ERRO_DE_LOG("PDF apresentou log.\n"),
	DESCRICAO_EM_BRANCO("Descrição em branco.\n"),
	ARQUIVO_EM_BRANCO("Arquivo em branco.\n"),
	INDICADOR_EM_BRANCO("indicador em branco.\n"),
	TIPO_INDICADOR_EM_BRANCO("Tipo indicador em branco\n"),
	TIPO_FONTE_EM_BRANCO("Tipo fonte em branco\n"),
	MEDIA_MOVEL_EM_BRANCO("Meses da média movel em branco\n"),
	MESES_DE_DEFASAGEM_EM_BRANCO("Meses de defasagem em branco\n"),
	POLARIDADE_EM_BRANCO("Polaridade em branco\n"),
	NOME_INDICADOR_EM_BRANCO("Nome indicador em branco.\n"),
	NOME_DA_FONTE_EM_BRANCO("Nome da fonte em branco.\n"),
	INICIO("INICIO DA ROTINA", TipoMensagem.INFO);
	
	private String mensagem;
	private TipoMensagem tipoMensagem;
	
	private EnumMensagens(String mensagem) {
		this(mensagem,TipoMensagem.ERRO);
	}
	private EnumMensagens(String mensagem, TipoMensagem tipoMensagem) {
		this.mensagem = mensagem;
		this.tipoMensagem = tipoMensagem;
	}

	public String getMensagem() {
		return mensagem;
	}
	public TipoMensagem getTipoMensagem() {
		return tipoMensagem;
	}
	

}
