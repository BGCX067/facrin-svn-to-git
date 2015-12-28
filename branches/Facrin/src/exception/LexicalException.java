package exception;

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
public class LexicalException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Erro lan�ado pelo analisador L�xico
	 * 
	 * @param symbol
	 */
	public LexicalException(String symbol) {
		super("Lexical exception: Error on symbol " + symbol);
	}

	public LexicalException(int linha, String symbol) {
		super("Linha: " + linha + " - " + "Lexical exception: Error on symbol "
				+ symbol);
	}
}
