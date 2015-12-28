package exception;

/*
 *      UNIVERSIDADE FEDERAL DE CAMPINA GRANDE
 *      CENTRO DE ENGENHARIA ELÉTRICA E INFORMÁTICA
 *      UNIDADE ACADÊMICA DE SISTEMAS E INFORMAÇÃO
 *      DISCIPLINA: COMPILADORES
 *      PROFESSOR: MARCUS SALERNO  DE AQUINO
 *      PERÍODO: 2009.1
 *      PROJETO - PARTE II:  ANALISADOR LÉXICO
 *
 *      EQUIPE 5:
 *
 *      CAYO M. AMORIM                  MATR.:20611119
 *      DAYANE P. M. GAUDÊNCIO          MATR.:20411032
 *      RENATA M. SARAIVA               MATR.:20521158
 * 
 */
public class LexicalException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Erro lançado pelo analisador Léxico
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
