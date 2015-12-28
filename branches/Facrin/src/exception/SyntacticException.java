package exception;

public class SyntacticException extends Exception {

	private static final long serialVersionUID = 1L;

	public SyntacticException(int i, String erro) {
		super("linha: " + i + " - Syntatic exception: " + erro);

	}

	public SyntacticException(String string) {
		super("Syntatic exception: " + string);
	}
}
