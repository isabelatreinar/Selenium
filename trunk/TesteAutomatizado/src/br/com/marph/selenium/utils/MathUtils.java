package br.com.marph.selenium.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.com.marph.selenium.utils.MathEvaluator.Node;

public class MathUtils {

	/**
	 * Método utilizado para validar uma expressão matemática. Caso a expressão
	 * estiver ok ele retornará um double qualquer (resolutado da expressão),
	 * caso não esteja ele retornará null.
	 * 
	 * @param variaveis
	 * @param expressao
	 * @return
	 */
	public static Double getValue(Map<String, Double> variaveis,
			String expressao) {

		MathEvaluator evaluator = new MathEvaluator(expressao);

		for (Map.Entry<String, Double> variavel : variaveis.entrySet()) {
			evaluator.addVariable(variavel.getKey(), variavel.getValue());
		}

		return evaluator.getValue();
	}

	/**
	 * Método utilizado para validar uma expressão matemática. Caso a expressão
	 * estiver ok ele retornará um double qualquer (resolutado da expressão),
	 * caso não esteja ele retornará null. Esse método recebe uma lista de
	 * variáveis e as preenche com o valor default (1D).
	 * 
	 * @param variaveis
	 * @param expressao
	 * @return
	 */
	public static Double getValue(Set<String> variaveis, String expressao) {

		MathEvaluator evaluator = new MathEvaluator(expressao);

		for (Map.Entry<String, Double> variavel : construirMapComVariaveis(
				variaveis).entrySet()) {

			evaluator.addVariable(variavel.getKey(), variavel.getValue());

		}

		return evaluator.getValue();
	}

	/**
	 * Método responsável por validar se será ou não possivel que a expressão em
	 * questão tenha uma divisão por zero.
	 * 
	 * @param expressao
	 * @return
	 */
	public static Boolean isPossibleDivisionByZero(String expressao) {

		MathEvaluator evaluator = new MathEvaluator(expressao);

		Node node = evaluator.getNode();

		return isPossibleDivisionByZero(node);
	}

	/**
	 * Método que retorna as variáveis contidas na formula
	 * 
	 * @param expressao
	 * @return
	 */
	public static Set<String> getVariaveisFormula(String expressao) {
		
		Node node = new MathEvaluator(expressao).getNode(); 
		try{
			return node.getNomeVariaveis().isEmpty() ? node.getNomeVariaveisTodosLevels() : node.getNomeVariaveis();
		}catch(Exception e){
			return new HashSet<String>();
		}
	}

	/**
	 * Método que valida a expressão (fórmula) matemática.
	 * 
	 * @param variaveis
	 * @param expressao
	 * @return
	 */
	public static boolean formulaValida(Map<String, Double> variaveis,
			String expressao) {

		Double resultadoExpressao = getValue(variaveis, expressao);

		return resultadoExpressao == null ? false : Double
				.isInfinite(resultadoExpressao) ? false : Double
				.isNaN(resultadoExpressao) ? false : true;
	}

	/**
	 * Método que valida a expressão (fórmula) matemática.
	 * 
	 * @param variaveis
	 * @param expressao
	 * @return
	 */
	public static boolean formulaValida(Set<String> variaveis, String expressao) {

		Double resultadoExpressao = getValue(variaveis, expressao);

		return resultadoExpressao == null ? false : Double
				.isInfinite(resultadoExpressao) ? false : Double
				.isNaN(resultadoExpressao) ? false : true;
	}

	/**
	 * Método responsável por pegar uma lista de variaveis e retornar um mapa
	 * com os valores padrão (1D) para as mesmas.
	 * 
	 * @param listVariaveis
	 * @return
	 */
	private static Map<String, Double> construirMapComVariaveis(
			Set<String> listVariaveis) {

		Map<String, Double> variaveis = new HashMap<String, Double>();

		for (String variavel : listVariaveis) {

			variaveis.put(variavel, 1D);

		}

		return variaveis;
	}

	/**
	 * Método responsável por validar se será ou não possivel que a expressão em
	 * questão tenha uma divisão por zero.
	 * 
	 * @param expressao
	 * @return
	 */
	private static Boolean isPossibleDivisionByZero(Node node) {

		Double valueTest = null;
		boolean validateLeft = false;
		boolean validateRight = false;

		if (node.nOperator != null && node.nOperator.getOperator().equals("/")) {

			try {
				MathEvaluator mathEvaluator = new MathEvaluator();
				valueTest = mathEvaluator.evaluate(node.nRight);
			} catch (Exception e) {
				valueTest = null;
			}

			if (valueTest == null || valueTest.equals(0D)) {
				return true;
			}

		}
		
		if (node.nOperator != null && node.nOperator.getOperator().equals("SE")) {
			Object[] trechos = MathEvaluator.splitIFFunction(node);
			boolean result = false;
			result |= isCondicionalPossibleDivisionByZero(trechos[0].toString());			
			result |= isPossibleDivisionByZero(((String[])trechos[1])[0]);			
			result |= isPossibleDivisionByZero(((String[])trechos[1])[1]);
			
			return result;
		}

		if (node.nLeft != null) {
			validateLeft = isPossibleDivisionByZero(node.nLeft);
		}

		if (node.nRight != null) {
			validateRight = isPossibleDivisionByZero(node.nRight);
		}
		
		return validateRight || validateLeft;
	}
	
	private static boolean isCondicionalPossibleDivisionByZero(String condicional){
		if(condicional.contains("/")){
			String valor = condicional.substring(condicional.indexOf("/")+1);			
			String result = MathEvaluator.getNextWord(valor);
			try{
				Double doubleValue = Double.parseDouble(result);
				return doubleValue.equals(0D);
			}catch(Exception e){
				return true;
			}
		}
		return false;
	}
	
	

}
