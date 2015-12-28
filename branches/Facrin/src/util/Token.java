package util;

/*
 *      UNIVERSIDADE FEDERAL DE CAMPINA GRANDE
 *      CENTRO DE ENGENHARIA EL�TRICA E INFORM�TICA
 *      UNIDADE ACAD�MICA DE SISTEMAS E INFORMA��O
 *      DISCIPLINA: COMPILADORES
 *      PROFESSOR: MARCUS SALERNO  DE AQUINO
 *      PER�ODO: 2009.1
 *      PROJETO - PARTE II:  ANALISADOR L�XICO
 *
 *      EQUIPE 5:
 *
 *      CAYO M. AMORIM                  MATR.:20611119
 *      DAYANE P. M. GAUD�NCIO          MATR.:20411032
 *      RENATA M. SARAIVA               MATR.:20521158
 * 
 */
/**
 * Classe que contr�i e manipula os tokens
 */
public class Token {
	private String code;
	private String symbol;
	private int linha;

	/**
	 * Constr�i um Token
	 * 
	 * @param code
	 * @param symbol
	 */
	public Token(String code, String symbol, int linha) {
		this.code = code;
		this.symbol = symbol;
		this.linha = linha;
	}

	/**
	 * Obtem o numero da linha ao qual o token pertence
	 * @return o numero da linha
	 */
	public int getLinha() {
		return linha;
	}

	/**
	 * Obtem o c�digo de um token
	 * 
	 * @return code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Altera o c�digo do Symbol
	 * 
	 * @param code
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Obtem Symbol de um token
	 * 
	 * @return symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * Altera Symbol
	 * 
	 * @param symbol
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * Representa��o de um token
	 */
	@Override
	public String toString() {
		return getCode() + " " + getSymbol() + "   Linha: " + getLinha();
	}
}
