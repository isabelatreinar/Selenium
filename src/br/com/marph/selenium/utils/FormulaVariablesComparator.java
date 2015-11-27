package br.com.marph.selenium.utils;

import java.util.Comparator;

/**
 * Mantem a ordenacao dos itens da formula para sempre ordenar baseado no tamanho, para substituir as variaveis de maneira correta
 * @author bruno.everton
 *
 */
public class FormulaVariablesComparator implements Comparator<String>{
	@Override
	public int compare(String o1, String o2) {
		int result;
		if(o1!=null&& o2!=null){
			result = o2.length() - o1.length();
			if(result!=0){
				return result;
			}
			return o1.compareTo(o2);
		}
		return 0;
	}
}
