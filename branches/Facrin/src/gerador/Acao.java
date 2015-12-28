package gerador;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import sintatico.Identificador;

public class Acao {
	private Object lado_esquerdo;

	private int prox_temp;
	private int proxLabel;

	private LinkedList<Object> pco;
	private LinkedList<String> pcl;

	private GeraCodigo gr;

	private ArrayList<Identificador> tabSimbolos;
	private ArrayList<TriplaTabela> tab_label;

	private String oper_relacional;

	public Acao() throws IOException {
		this.pco = new LinkedList<Object>();
		this.pcl = new LinkedList<String>();
		this.gr = new GeraCodigo();
		this.tab_label = new ArrayList<TriplaTabela>();
	}

	private void reset_temp() {
		prox_temp = 1;
	}

	public void push(String id) {
		this.pco.add(Integer.parseInt(id));
	}

	// AG expressão aritimetica e atribuição
	public void ag1(Object simbolo) {
		lado_esquerdo = simbolo;
		reset_temp();
	}

	public void ag2(Object oper_1) {
		oper_1 = pco.pop();
		gr
				.adicionaTripla(new TriplaAtribuicao(this.lado_esquerdo, ":=",
						oper_1));
	}

	public void ag3() {
		Object oper_2 = pco.pop();
		Object oper_1 = pco.pop();
		String resul = make_temp();
		gr.adicionaTripla(new Tripla(resul, "+", oper_1, oper_2));
		int num1 = intParse(oper_1);
		int num2 = intParse(oper_2);
		int result = num1 + num2;
		pco.add(result);
	}



	public void ag33() {
		Object oper_2 = pco.pop();
		Object oper_1 = pco.pop();
		String resul = make_temp();
		gr.adicionaTripla(new Tripla(resul, "-", oper_1, oper_2));
		int num1 = intParse(oper_1);
		int num2 = intParse(oper_2);
		int result = num1 - num2;
		pco.add(result);
	}

	public void ag444() {
		Object oper_2 = pco.pop();
		Object oper_1 = pco.pop();
		String resul = make_temp();
		gr.adicionaTripla(new Tripla(resul, "**", oper_1, oper_2));
		int num1 = intParse(oper_1);
		int num2 = intParse(oper_2);
		Double result2 = Math.pow(num1, num2);
		int result = result2.intValue();
		pco.add(result);
	}

	public void ag44() {
		Object oper_2 = pco.pop();
		Object oper_1 = pco.pop();
		String resul = make_temp();
		gr.adicionaTripla(new Tripla(resul, "/", oper_1, oper_2));
		int num1 = intParse(oper_1);
		int num2 = intParse(oper_2);
		int result = num1 / num2;
		pco.add(result);
	}

	public void ag4() {
		Object oper_2 = pco.pop();
		Object oper_1 = pco.pop();
		String resul = make_temp();
		gr.adicionaTripla(new Tripla(resul, "*", oper_1, oper_2));
		int num1 = intParse(oper_1);
		int num2 = intParse(oper_2);
		int result = num1 * num2;
		pco.add(result);
	}

	public void ag5(String id) {
		pco.add(getValorId(id));

	}

	public void ag6(String id) {
		pco.add(id);

	}

	public void ag7(String id) {
		pco.add(id);

	}

	// AGs do if
	public void ag1IF() {
		this.reset_temp();
	}

	public void ag2IF() {
		lado_esquerdo = pco.pop();
	}

	public void ag3IF(String operador) {
		this.oper_relacional = operador;
	}

	public void ag4IF() {
		gr.adicionaTripla(new Tripla("", oper_relacional, lado_esquerdo, pco
				.pop()));
		String label = mkLabel();
		pcl.push(label);
		gr.adicionaTripla(new Tripla(label, "jif", "", ""));
	}

	public void ag5IF() {
		String label_aux = pcl.pop();
		String label = mkLabel();
		pcl.push(label);
		gr.adicionaTripla(new Tripla(label, "jmp", "", ""));
		String endereco_tripla_atual = label.substring(label.length() - 3);
		tab_label.add(new TriplaTabela(label, endereco_tripla_atual));
	}

	public void ag6IF() {
		String label = pcl.pop();
		String endereco_tripla_atual = label.substring(label.length() - 3);
		tab_label.add(new TriplaTabela(label, endereco_tripla_atual));
	}

	private String mkLabel() {
		String newLabel = String.format("L%03d", proxLabel);
		proxLabel++;
		return newLabel;
	}

	// AGs do While
	public void ag1WHILE() {
		String label = mkLabel();
		String endereco_tripla_atual = label.substring(label.length() - 3);
		tab_label.add(new TriplaTabela(label, endereco_tripla_atual));
		pcl.push(label);

	}

	public void ag2WHILE() {
		String label_fim = pcl.pop();
		String label_inicio = pcl.pop();
		gr.adicionaTripla(new Tripla(label_inicio, "jmp", "", ""));
		String endereco_tripla_atual = label_fim
				.substring(label_fim.length() - 3);
		tab_label.add(new TriplaTabela(label_fim, endereco_tripla_atual));
	}

	private Object getValorId(String id) {
		for (Identificador ident : tabSimbolos) {
			if (ident.getNome().equals(id))
				return ident.getValor();
		}
		return null;
	}

	// gera o nome de variável temporária
	private String make_temp() {
		String var = "t" + this.prox_temp;
		prox_temp++;
		return var;
	}

	public void geraCodigo() throws IOException {
		gr.geraCodigo();
	}

	public void setListIdentificadores(
			ArrayList<Identificador> simbolos_declarados) {
		this.tabSimbolos = simbolos_declarados;
	}
	private int intParse(Object oper_1) {
		if (oper_1 instanceof String) {
			String str = (String) oper_1;
			return Integer.parseInt(str);
		} else if (oper_1 instanceof Integer) {
			Integer inter = (Integer) oper_1;
			return inter;
		}
		return 0;
	}
}
