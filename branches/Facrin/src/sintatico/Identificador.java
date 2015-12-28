package sintatico;

public class Identificador {
	private String nome;
	private String tipo;
	private String valor;
    
	/**
     * Construtor
     * @param nome
     * @param tipo
     * @param valor
     */
	public Identificador(String nome, String tipo, String valor) {
		this.setNome(nome);
		this.setTipo(tipo);
		this.setValor(valor);
	}

	/**
	 * @param nome the nome to set
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * @return the tipo
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * @param valor the valor to set
	 */
	public void setValor(String valor) {
		this.valor = valor;
	}

	/**
	 * @return the valor
	 */
	public String getValor() {
		return valor;
	}

}
