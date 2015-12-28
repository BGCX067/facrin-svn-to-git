package gerador;


public class TriplaAtribuicao extends Tripla {
	private String operador;
	private String lado_esquerdo;
	private String oper_1;

	/**
	 * Tripla de uma atribuição para inteiros
	 * 
	 * @param lado_esquerdo
	 * @param operador
	 * @param oper_1
	 */
	public TriplaAtribuicao(Object lado_esquerdo, String operador, Object oper_1) {
		this.operador = operador;
		this.lado_esquerdo = lado_esquerdo.toString();
		this.oper_1 = oper_1.toString();

	}

	public String toString() {
		return " " + this.lado_esquerdo + " | " + this.operador + " | "
				+ this.oper_1;
	}

}
