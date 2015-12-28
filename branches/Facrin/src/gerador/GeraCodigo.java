package gerador;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class GeraCodigo {
	private ArrayList<String> tabela;
	private BufferedWriter gc;

	/**
	 * Construtor da Classe
	 * 
	 * @throws IOException
	 */
	public GeraCodigo() throws IOException {
		this.tabela = new ArrayList<String>();

	}

	public void adicionaTripla(Tripla tr) {
		tabela.add(tr.toString());

	}

	public void geraCodigo() throws IOException {
		this.gc = new BufferedWriter(new FileWriter("codigo.frcn"));
		final String SEPARATOR = System.getProperty("line.separator");
		for (String t : this.tabela) {
			gc.write(t.toString() + SEPARATOR);
		}
		gc.close();

	}

}
