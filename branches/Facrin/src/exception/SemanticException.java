package exception;

/*
 *      UNIVERSIDADE FEDERAL DE CAMPINA GRANDE
 *      CENTRO DE ENGENHARIA EL�TRICA E INFORM�TICA
 *      UNIDADE ACAD�MICA DE SISTEMAS E INFORMA��O
 *      DISCIPLINA: COMPILADORES
 *      PROFESSOR: MARCUS SALERNO  DE AQUINO
 *      PER�ODO: 2009.1
 *      PROJETO - PARTE IV:  ANALISADOR SEM�NTICO
 *
 *      EQUIPE 5:
 *
 *      CAYO M. AMORIM                  MATR.:20611119
 *      DAYANE P. M. GAUD�NCIO          MATR.:20411032
 *      RENATA M. SARAIVA               MATR.:20521158
 * 
 */
public class SemanticException extends Exception {

	private static final long serialVersionUID = 1L;

	public SemanticException(int i, String erro) {
		super("linha: " + i + " - Semantic exception: " + erro);

	}

}
