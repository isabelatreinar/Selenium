package br.com.marph.selenium.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Classe responsavel por validar e executar formulas matematicas. Ex:
 * 3+SE(2/2==1?SE(2==3?1:4+2):2)/2
 * 
 * @author joao.oliveira
 *
 */
public class MathEvaluator {

	public static Operator[] operators;
	private Node node;
	private String expression;
	private SortedMap<String, Double> variables = new TreeMap<String, Double>(new FormulaVariablesComparator());
	private Set<String> nomeVariaveis = new HashSet<String>();
	private Set<String> nomeVariaveisTodosLevels = new HashSet<String>();
	
	private static String[] booleanOperators = {"==","!=",">=","<=",">","<"};

	/***
	 * creates an empty MathEvaluator. You need to use setExpression(String s)
	 * to assign a math expression string to it.
	 */
	public MathEvaluator() {
		init();
	}

	/***
	 * creates a MathEvaluator and assign the math expression string.
	 */
	public MathEvaluator(String s) {
		init();
		setExpression(s);
	}

	private void init() {
		if (operators == null)
			initializeOperators();
	}

	/***
	 * adds a variable and its value in the MathEvaluator
	 */
	public void addVariable(String v, double val) {
		addVariable(v, new Double(val));
	}

	/***
	 * adds a variable and its value in the MathEvaluator
	 */
	public void addVariable(String v, Double val) {
		variables.put(v, val == null ? 1D : val);
	}

	/***
	 * sets the expression
	 */
	public void setExpression(String s) {
		expression = s;
	}

	/***
	 * resets the evaluator
	 */
	public void reset() {
		node = null;
		expression = null;
		variables = new TreeMap<String, Double>(new FormulaVariablesComparator());
	}

	/***
	 * trace the binary tree for debug
	 */
	public void trace() {
		try {
			node = new Node(expression);
			node.trace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * evaluates and returns the value of the expression
	 */
	public Double getValue() {
		if (expression == null)
			return null;

		try {
			node = new Node(expression);
			return evaluate(node);
		} catch (Exception e) {
			return null;
		}
	}

	public Node getNode() {
		if (expression == null)
			return null;

		try {
			return new Node(expression);
		} catch (Exception e) {
			return null;
		}
	}

	public Double evaluate(Node n) throws Exception {
		if (n.hasOperator() && n.hasChild()) {
			if (n.getOperator().getType() == 1) {

				n.setValue(evaluateExpression(n.getOperator(), evaluate(n.getLeft()), null));

			} else if (n.getOperator().getType() == 2) {

				n.setValue(evaluateExpression(n.getOperator(), evaluate(n.getLeft()), evaluate(n.getRight())));

			} else if (n.getOperator().getType() == 3) {		
				Object[] result = splitIFFunction(n);				
				String condicional = result[0].toString();
				String resultados[] = (String[]) result[1];

				try {
					if (evaluateBoolean(condicional,variables)) {
						n.setValue(evaluate(new Node(resultados[0])));
					} else {
						n.setValue(evaluate(new Node(resultados[1])));
					}
				} catch (Exception e) {
					throw e;
				}				
			}
		}
		return n.getValue();
	}

	/**
	 * Quebra a sintaxe do if
	 * @return node [0] contém a condicional do if em string
	 * 			    [1] contém um array de string com 2 posições: 1-para if verdadeiro;2-para if false
	 */
	public static Object[] splitIFFunction(Node node) {
		Node nodeRight = node.getRight();

		if(nodeRight.getString().contains("?")){
			//Quebra a parte da condicional com os valores
			//String[] estrutura = nodeRight.getString().split("\\?(?![^()]*\\))");
			
			int indexInterrogacao = getNextIndexByLevel(nodeRight.getString(),'?');
			String condicional = nodeRight.getString().substring(0, indexInterrogacao);
			
			//Quebra os valores
			String resultadosExp = nodeRight.getString().substring(indexInterrogacao+1);
			int indexDoisPontos = getNextIndexByLevel(resultadosExp,':');
			//String resultados[] = nodeRight.getString().substring(indexInterrogacao+1).split(":(?![^()]*\\))");
			String resultados[] = {resultadosExp.substring(0,indexDoisPontos),resultadosExp.substring(indexDoisPontos+1)};
	
			Object[] result = { condicional, resultados };
			return result;
		}
		return null;
	}
	private static int getNextIndexByLevel(String exp,char c){
		int level = 0;
		for (int i = 0; i < exp.length(); i++) {
			if(exp.charAt(i)=='('){
				level++;
			}else if(exp.charAt(i)==')'){
				level--;
			}
			if(level==0 && exp.charAt(i)==c){
				return i;
			}
		}		
		return -1;
	}

	/**
	 * Metodo que executa uma expressao booleana
	 * @param expressao
	 * @return true or false
	 * @throws Exception 
	 */
	private boolean evaluateBoolean(String exp,SortedMap<String, Double> variables) throws Exception {
		try {
			String expressao = exp;
			String[] condionalExpression = splitBooleanConditionalByOperator(expressao);
			if(condionalExpression!=null){	
				expressao = evaluate(new Node(condionalExpression[1])) + condionalExpression[0] + evaluate(new Node(condionalExpression[2]));				
			}
			
			//Troca todos os valores da condicional pelos valores reais
			for (String key : variables.keySet()) {
				expressao = expressao.replaceAll(key, variables.get(key).toString());
			}				
			
			ScriptEngineManager sem = new ScriptEngineManager();
			ScriptEngine se = sem.getEngineByName("JavaScript");
			return (Boolean) se.eval(expressao);

		} catch (ScriptException e) {
			System.out.println("Erro ao verificar a condicional de uma formula.");
			return false;
		}
	}
	private static String[] splitBooleanConditionalByOperator(String exp){
		for (String operator : booleanOperators) {
			String[] result = exp.split(operator+"(?![^()]*\\))");
			if(result!=null && result.length>1){
				return new String[]{operator,result[0],result[1]};
			}
		}		
		return null;
	}

	private static Double evaluateExpression(Operator o, Double f1, Double f2) {
		String op = o.getOperator();
		Double res = null;

		if ("+".equals(op))
			res = new Double(f1.doubleValue() + f2.doubleValue());
		else if ("-".equals(op))
			res = new Double(f1.doubleValue() - f2.doubleValue());
		else if ("*".equals(op))
			res = new Double(f1.doubleValue() * f2.doubleValue());
		else if ("/".equals(op))
			res = new Double(f1.doubleValue() / f2.doubleValue());
		else if ("^".equals(op))
			res = new Double(Math.pow(f1.doubleValue(), f2.doubleValue()));
		else if ("%".equals(op))
			res = new Double(f1.doubleValue() % f2.doubleValue());
		else if ("&".equals(op))
			res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
		else if ("|".equals(op))
			res = new Double(f1.doubleValue() + f2.doubleValue()); // todo
		else if ("cos".equals(op))
			res = new Double(Math.cos(f1.doubleValue()));
		else if ("sin".equals(op))
			res = new Double(Math.sin(f1.doubleValue()));
		else if ("tan".equals(op))
			res = new Double(Math.tan(f1.doubleValue()));
		else if ("acos".equals(op))
			res = new Double(Math.acos(f1.doubleValue()));
		else if ("asin".equals(op))
			res = new Double(Math.asin(f1.doubleValue()));
		else if ("atan".equals(op))
			res = new Double(Math.atan(f1.doubleValue()));
		else if ("sqr".equals(op))
			res = new Double(f1.doubleValue() * f1.doubleValue());
		else if ("sqrt".equals(op))
			res = new Double(Math.sqrt(f1.doubleValue()));
		else if ("log".equals(op))
			res = new Double(Math.log(f1.doubleValue()));
		else if ("min".equals(op))
			res = new Double(Math.min(f1.doubleValue(), f2.doubleValue()));
		else if ("max".equals(op))
			res = new Double(Math.max(f1.doubleValue(), f2.doubleValue()));
		else if ("exp".equals(op))
			res = new Double(Math.exp(f1.doubleValue()));
		else if ("floor".equals(op))
			res = new Double(Math.floor(f1.doubleValue()));
		else if ("ceil".equals(op))
			res = new Double(Math.ceil(f1.doubleValue()));
		else if ("abs".equals(op))
			res = new Double(Math.abs(f1.doubleValue()));
		else if ("ABS".equals(op))
			res = new Double(Math.abs(f1.doubleValue()));
		else if ("neg".equals(op))
			res = new Double(-f1.doubleValue());
		else if ("rnd".equals(op))
			res = new Double(Math.random() * f1.doubleValue());

		return res;
	}

	private void initializeOperators() {
		operators = new Operator[27];
		operators[0] = new Operator("+", 2, 0, false);
		operators[1] = new Operator("-", 2, 0, false);
		operators[2] = new Operator("*", 2, 10, false);
		operators[3] = new Operator("/", 2, 10, false);
		operators[4] = new Operator("^", 2, 10, false);
		operators[5] = new Operator("%", 2, 10, false);
		operators[6] = new Operator("&", 2, 0, false);
		operators[7] = new Operator("|", 2, 0, false);
		operators[8] = new Operator("cos", 1, 20, true);
		operators[9] = new Operator("sin", 1, 20, true);
		operators[10] = new Operator("tan", 1, 20, true);
		operators[11] = new Operator("acos", 1, 20, true);
		operators[12] = new Operator("asin", 1, 20, true);
		operators[13] = new Operator("atan", 1, 20, true);
		operators[14] = new Operator("sqrt", 1, 20, true);
		operators[15] = new Operator("sqr", 1, 20, true);
		operators[16] = new Operator("log", 1, 20, true);
		operators[17] = new Operator("min", 2, 0, true);
		operators[18] = new Operator("max", 2, 0, true);
		operators[19] = new Operator("exp", 1, 20, true);
		operators[20] = new Operator("floor", 1, 20, true);
		operators[21] = new Operator("ceil", 1, 20, true);
		operators[22] = new Operator("abs", 1, 20, true);
		operators[23] = new Operator("neg", 1, 20, true);
		operators[24] = new Operator("rnd", 1, 20, true);
		operators[25] = new Operator("ABS", 1, 20, true);
		operators[26] = new Operator("SE", 3, 30, true);
	}

	/***
	 * gets the variable's value that was assigned previously
	 */
	public Double getVariable(String s) {
		return variables.get(s);
	}

	private Double getDouble(String s) {
		DecimalFormat df = new DecimalFormat("#.##");
		
		if (s == null)
			return null;

		Double res = null;
		try {
			res = df.parse(s).doubleValue();
		} catch (Exception e) {
			return getVariable(s);
		}

		return res;
	}

	protected Operator[] getOperators() {
		return operators;
	}

	public class Operator {
		private String op;
		private int type;
		private int priority;
		private boolean function;

		public Operator(String o, int t, int p, boolean function) {
			op = o;
			type = t;
			priority = p;
			this.function = function;
		}

		public String getOperator() {
			return op;
		}

		public void setOperator(String o) {
			op = o;
		}

		public int getType() {
			return type;
		}

		public int getPriority() {
			return priority;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((op == null) ? 0 : op.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Operator other = (Operator) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (op == null) {
				if (other.op != null)
					return false;
			} else if (!op.equals(other.op))
				return false;
			return true;
		}

		private MathEvaluator getOuterType() {
			return MathEvaluator.this;
		}

		public boolean isFunction() {
			return function;
		}
	}

	public class Node {
		public String nString = null;
		public Operator nOperator = null;
		public Node nLeft = null;
		public Node nRight = null;
		public Node nParent = null;
		public int nLevel = 0;
		public Double nValue = null;

		public Node(String s) throws Exception {
			init(null, s, 0);
		}

		public Node(Node parent, String s, int level) throws Exception {
			init(parent, s, level);
		}

		private void init(Node parent, String s, int level) throws Exception {
			s = removeIllegalCharacters(s);
			s = removeBrackets(s);
			s = addZero(s);
			if (checkBrackets(s) != 0) {
				throw new Exception("Wrong number of brackets in [" + s + "]");
			}

			nParent = parent;
			nString = s;
			nValue = getDouble(s);
			nLevel = level;
			int sLength = s.length();
			int inBrackets = 0;
			int startOperator = 0;

			for (int i = 0; i < sLength; i++) {
				if (s.charAt(i) == '(') {
					inBrackets++;
				} else if (s.charAt(i) == ')') {
					inBrackets--;
				} else {
					// the expression must be at "root" level
					if (inBrackets == 0) {
						Operator o = getOperator(nString, i);
						if (o != null) {
							// if first operator or lower priority operator
							if (nOperator == null || nOperator.getPriority() >= o.getPriority()) {
								nOperator = o;
								startOperator = i;
							}
						}
					}
				}
			}

			if (nOperator != null) {
				// one operand, should always be at the beginning
				if (startOperator == 0 && nOperator.getType() == 1) {
					// the brackets must be ok
					if (checkBrackets(s.substring(nOperator.getOperator().length())) == 0) {
						nLeft = new Node(this, s.substring(nOperator.getOperator().length()), nLevel + 1);
						nRight = null;
						return;
					} else
						throw new Exception("Error during parsing... missing brackets in [" + s + "]");
				}
				// two operands
				else if (startOperator > 0 && nOperator.getType() == 2) {
					nLeft = new Node(this, s.substring(0, startOperator), nLevel + 1);
					nRight = new Node(this, s.substring(startOperator + nOperator.getOperator().length()), nLevel + 1);
				} else if (startOperator == 0 && nOperator.getType() == 3) {
					nRight = new Node(this, s.substring(startOperator + nOperator.getOperator().length()), nLevel + 1);
				}
			}

			if (nOperator == null && nValue == null && !nString.equals("")) {

				nomeVariaveisTodosLevels.add(nString);

				if (nLevel != 0) {
					nomeVariaveis.add(nString);
				}
			}
		}

		private Operator getOperator(String s, int start) {
			Operator[] operators = getOperators();
			String temp = s.substring(start);
			temp = getNextWord(temp);
			for (int i = 0; i < operators.length; i++) {
				String complement = operators[i].isFunction() ? "(" : "";
				if (temp.startsWith(operators[i].getOperator() + complement))
					return operators[i];
			}
			return null;
		}

		/***
		 * checks if there is any missing brackets
		 * 
		 * @return true if s is valid
		 */
		protected int checkBrackets(String s) {
			int sLength = s.length();
			int inBracket = 0;

			for (int i = 0; i < sLength; i++) {
				if (s.charAt(i) == '(' && inBracket >= 0) {
					inBracket++;
				} else if (s.charAt(i) == ')') {
					inBracket--;
				}
			}

			return inBracket;
		}

		/***
		 * returns a string that doesnt start with a + or a -
		 */
		protected String addZero(String s) {
			if (s.startsWith("+") || s.startsWith("-")) {
				int sLength = s.length();
				for (int i = 0; i < sLength; i++) {
					if (getOperator(s, i) != null)
						return "0" + s;
				}
			}

			return s;
		}

		/***
		 * displays the tree of the expression
		 */
		public void trace() {
	//		String op = getOperator() == null ? " " : getOperator().getOperator();
			if (this.hasChild()) {
				if (hasLeft())
					getLeft().trace();
				if (hasRight())
					getRight().trace();
			}
		}

		protected boolean hasChild() {
			return (nLeft != null || nRight != null);
		}

		protected boolean hasOperator() {
			return (nOperator != null);
		}

		protected boolean hasLeft() {
			return (nLeft != null);
		}

		protected Node getLeft() {
			return nLeft;
		}

		protected boolean hasRight() {
			return (nRight != null);
		}

		protected Node getRight() {
			return nRight;
		}

		protected Operator getOperator() {
			return nOperator;
		}

		protected int getLevel() {
			return nLevel;
		}

		protected Double getValue() {
			return nValue;
		}

		protected void setValue(Double f) {
			nValue = f;
		}

		protected String getString() {
			return nString;
		}

		/***
		 * Removes spaces, tabs and brackets at the begining
		 */
		public String removeBrackets(String s) {
			String res = s;
			if (s.length() > 2 && res.startsWith("(") && res.endsWith(")")
					&& checkBrackets(s.substring(1, s.length() - 1)) == 0) {
				res = res.substring(1, res.length() - 1);
			}
			if (res != s)
				return removeBrackets(res);
			else
				return res;
		}

		/***
		 * Removes illegal characters
		 */
		public String removeIllegalCharacters(String s) {
			char[] illegalCharacters = { ' ' };
			String res = s;

			for (int j = 0; j < illegalCharacters.length; j++) {
				int i = res.lastIndexOf(illegalCharacters[j], res.length());
				while (i != -1) {
					String temp = res;
					res = temp.substring(0, i);
					res += temp.substring(i + 1);
					i = res.lastIndexOf(illegalCharacters[j], s.length());
				}
			}
			return res;
		}

		/**
		 * @return the nomeVariaveis
		 */
		public Set<String> getNomeVariaveis() {
			return nomeVariaveis;
		}

		/**
		 * @return the nomeVariaveis
		 */
		public Set<String> getNomeVariaveisTodosLevels() {
			return nomeVariaveisTodosLevels;
		}

	}

	public static String getNextWord(String s) {
		int sLength = s.length();
		for (int i = 1; i < sLength; i++) {
			char c = s.toLowerCase().charAt(i);
			if ((c > 'z' || c < 'a') && (c > '9' || c < '0') && c != '_') {
				if (c == '(') {
					i++;
				}
				return s.substring(0, i);
			}
		}
		return s;
	}
	
	public static void main(String[] args) {
		String formula = "(NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES+NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES+NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES+NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES)*"+
				"VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA+"+
					"SE (NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES==0?0:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES>=1?"+
					"0,35*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES>=0,5?"+
					"0,15*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0)))+"+
					"SE (NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES==0?0:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES>=1?"+
					"0,35*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES>=0,5?"+
					"0,15*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0)))+"+					
					"SE(NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES==0?0:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES>=1?"+
					"0,35*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES>=0,5?"+
					"0,15*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0)))+"+					
					"SE(NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES==0?0:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES>=1?"+
					"0,35*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:"+
					"SE((NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS)/NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES>=0,5?"+
					"0,15*NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0)))+"+
					"NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS+NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM+"+
				"NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS+NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS+"+
				"NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA+"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS==0?0:"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM/NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS<=1?"+
					"(NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS)*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0))+"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS==0?0:"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM/NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS<=1?"+
					"(NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS)*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0))+"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS==0?0:"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM/NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS<=1?"+
					"(NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS)*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0))+"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS==0?0:"+
					"SE(NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM/NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS<=1?"+
					"(NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM+NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS)*VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA:0))+"+
					"NUMERO_DE_EQUIPES_SAUDE_NA_FAMILIA_ASSOCIADO_AO_NASF*VALOR_DO_FATOR_DE_ALOCACAO_NASF+NUMERO_DE_EQUIPES_ECR_MODALIDADE_UM*VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_UM+NUMERO_DE_EQUIPES_ECR_MODALIDADE_DOIS*"+
				"VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_DOIS+NUMERO_DE_EQUIPES_ECR_MODALIDADE_TRES*VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_TRES";
		
		
		Map<String,Double> val = new HashMap<String,Double>();
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_NA_FAMILIA", 0.36D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_EQUIPE_SAUDE_BUCAL", 5D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_NASF", 6D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_UM", 9.1D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_TRES", 2.2D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO_ECR_MODALIDADE_DOIS", 12.4D);
		val.put("VALOR_DO_FATOR_DE_ALOCACAO", 5D);
		val.put("TRAVESSIA", 8D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_UM", 5D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_TERCEIRO_MES_MODALIDADE_DOIS", 0.69D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_UM", 54D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_SEGUNDO_MES_MODALIDADE_DOIS", 5D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_UM", 8D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_QUARTO_MES_MODALIDADE_DOIS", 6D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_UM", 3.6D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_BUCAL_PRIMEIRO_MES_MODALIDADE_DOIS", 9.2D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_NA_FAMILIA_ASSOCIADO_AO_NASF", 1.5D);
		val.put("NUMERO_DE_EQUIPES_ECR_MODALIDADE_UM", 45D);
		val.put("NUMERO_DE_EQUIPES_ECR_MODALIDADE_TRES", 12D);
		val.put("NUMERO_DE_EQUIPES_ECR_MODALIDADE_DOIS", 8D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_TERCEIRO_MES", 4D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_SEGUNDO_MES", 1D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_QUARTO_MES", 4D);
		val.put("NUMERO_DE_EQUIPES_SAUDE_DA_FAMILIA_PRIMEIRO_MES", 6D);
		val.put("FATOR_DE_ALOCACAO", 8D);
		val.put("EQUIPES_DE_SAUDE_DA_FAMILIA", 5D);
		val.put("EQUIPE_CONSCIENTIZACAO_DO_LAR", 0.5D);
		val.put("ANTENAS_DO_CANAL_MINAS_SAUDE", 4D);
				
		Double result = MathUtils.getValue(val, formula);
		System.out.println(result);
	}	
}
