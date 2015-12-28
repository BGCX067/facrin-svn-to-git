package exception;

/*
 *      UNIVERSIDADE FEDERAL DE CAMPINA GRANDE
 *      CENTRO DE ENGENHARIA ELÉTRICA E INFORMÁTICA
 *      UNIDADE ACADÊMICA DE SISTEMAS E INFORMAÇÃO
 *      DISCIPLINA: COMPILADORES
 *      PROFESSOR: MARCUS SALERNO  DE AQUINO
 *      PERÍODO: 2009.1
 *      PROJETO - PARTE IV:  ANALISADOR SEMÂNTICO
 *
 *      EQUIPE 5:
 *
 *      CAYO M. AMORIM                  MATR.:20611119
 *      DAYANE P. M. GAUDÊNCIO          MATR.:20411032
 *      RENATA M. SARAIVA               MATR.:20521158
 * 
 */
public class SemanticException extends Exception {

	private static final long serialVersionUID = 1L;

	public SemanticException(int i, String erro) {
		super("linha: " + i + " - Semantic exception: " + erro);

	}

}
