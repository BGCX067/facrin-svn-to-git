import exception.ExceptionManager;
import gerador.Acao;
import lexical.LexScanner;
import sintatico.AnalisadorSintatico;

public class Facrin {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			LexScanner scanner = new LexScanner("in1_.frcm", "out1.frcm");
			scanner.execute();
			Acao acao = new Acao();
			AnalisadorSintatico sa1 = new AnalisadorSintatico(scanner, acao);
			sa1.start();
			if (ExceptionManager.isEmpty()) {
				acao.geraCodigo();
				System.out.println("OK");
			}
		} catch (Exception e) {
			//e.printStackTrace();
			System.err.println("Erro Fatal = "+e.getMessage());
		} finally {
			if (!ExceptionManager.isEmpty())
				ExceptionManager.show();
		}
	}

}
