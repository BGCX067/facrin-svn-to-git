package gerador;

public class Tripla {
	private String operador;
	private String resul;
	private Object oper_2;
	private Object oper_3;

	/**
	 * Tripla de uma expressao
	 * 
	 * @param operador
	 * @param operando_2
	 * @param operando_3
	 */
	public Tripla(String resul, String operador, Object operando_2, Object operando_3) {
		this.operador = operador;
		this.oper_2 = operando_2;
		this.oper_3 = operando_3;
		this.resul = resul;
	}

	public Tripla() {

	}

	public String toString() {
		return " " + this.resul + " | " + this.operador + " | " + this.oper_2
				+ " | " + this.oper_3;
	}

}
