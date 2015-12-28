package sintatico;

import java.io.IOException;
import java.util.ArrayList;

import lexical.LexScanner;
import lexical.SymbolDictionary;
import util.Token;
import exception.ExceptionManager;
import exception.LexicalException;
import exception.SemanticException;
import exception.SyntacticException;
import gerador.Acao;

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
public class AnalisadorSintatico {

	private SymbolDictionary symbols;

	private int index = -1;

	private Token next;
	private ArrayList<Identificador> simbolos_declarados;
	private Acao acao;
	private String tipo;
	private String valor;

	private LexScanner scanner;

	public AnalisadorSintatico(LexScanner scanner, Acao acao) {
		this.scanner = scanner;
		this.acao = acao;
		simbolos_declarados = new ArrayList<Identificador>();
		this.symbols = scanner.getDictionary();
		this.acao.setListIdentificadores(simbolos_declarados);
	}

	// verifica se o identificador passado como parâmetro já foi declarado
	private boolean ehDeclarado(String nome) throws SemanticException {
		if (!existeIdentificador(nome)) {
			ExceptionManager.add(new SemanticException(next.getLinha(),
					"Identificador '" + nome + "' não declarado!"));
			return false;
		} else {
			return true;
		}
	}

	// <programa> ::= program <identificador> <main> end
	public void start() throws SyntacticException, SemanticException,
			IOException {
		index++;
		next = this.symbols.getToken(index);
		if (next.getSymbol().equals("program")) {
			index++;
			next = symbols.getToken(index);
			if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
				main();
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals("end")) {
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado um END para PROGRAM!"));
				}
			} else
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Esperado um IDENTIFICADOR para PROGRAM!"));
		} else
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Esperado o comando PROGRAM"));

	}

	// <main> ::= <declaracoes> <subroutines> <body>
	private void main() throws SyntacticException, SemanticException {
		index++;
		declaracoes();
		index++;
		subroutines();
		index++;
		body();
	}

	// <declaracoes> ::= <declaracao><declaracoes>| lambida
	private void declaracoes() throws SyntacticException, SemanticException {
		next = this.symbols.getToken(index);
		if ((next.getCode() == SymbolDictionary.ID_RESERVED)) {
			if (next.getSymbol().equals("integer")) {
				index++;
				declaracao("integer");
				index++;
				declaracoes();

			} else if (next.getSymbol().equals("string")) {
				index++;
				declaracao("string");
				index++;
				declaracoes();

			} else {
				index--;
				next = this.symbols.getToken(index);
			}
		} else
			index--;
		next = this.symbols.getToken(index);
		// else{
		// throw new SyntacticException("Esperado tipo da declaração!");
		// }

	}

	// <declaração> ::= <tipo> <identificador> = <expressao> ;
	private void declaracao(String type) throws SyntacticException,
			SemanticException {
		next = this.symbols.getToken(index);

		if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
			String nome = next.getSymbol();
			if (!existeIdentificador(nome)) {
				simbolos_declarados.add(new Identificador(nome, type, valor));
				acao.ag1(nome); // armazena o lado esquerdo da atribuição

			} else {
				ExceptionManager.add(new SemanticException(next.getLinha(),
						"O Identificador '" + nome
								+ "' já foi declarado anteriormente!"));
				while (!this.symbols.getToken(index).getSymbol().equals(";")) {
					index++;
				}

			}

			index++;
			next = this.symbols.getToken(index);
			if (next.getSymbol().equals("=")) {
				index++;
				next = this.symbols.getToken(index);
				valor = next.getSymbol();
				setIdentificador(nome, valor);
				acao.ag7(valor);
				acao.ag2(valor);// gera código para atribuição
				index++;
				next = this.symbols.getToken(index);

				if (!next.getSymbol().equals(";"))
					ExceptionManager
							.add(new SyntacticException(next.getLinha(),
									"Esperado sinal ';' no final da atribuição na declaração!"));
			} else {
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Esperado sinal '=' de atribuição na declaração!"));
				while (!this.symbols.getToken(index).getSymbol().equals(";")) {
					index++;
				}
			}
		} else {
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Esperado identificador para a declaração!"));
			while (!this.symbols.getToken(index).getSymbol().equals(";")) {
				index++;
			}
		}
	}

	private boolean existeIdentificador(String nome) {

		for (int k = 0; k < simbolos_declarados.size(); k++) {
			if (simbolos_declarados.get(k).getNome().equalsIgnoreCase(nome))
				return true;
		}
		return false;

	}

	// <subroutines> ::= <subroutine><subroutines> | lambida
	private void subroutines() throws SyntacticException, SemanticException {
		next = this.symbols.getToken(index);
		if (next.getCode() == SymbolDictionary.ID_RESERVED) {
			if (next.getSymbol().equals("subroutine")) {
				subroutine();
				index++;
				subroutines();
			} else
				index--;
			next = this.symbols.getToken(index);
		} else {
			index--;
			next = this.symbols.getToken(index);
		}
		// else
		// throw new SyntacticException(next.getLinha(),
		// "Comando SUBROUTINE esperado");
	}

	// <subroutine> ::= subroutine <identificador> <body> end
	private void subroutine() throws SyntacticException, SemanticException {
		next = this.symbols.getToken(index);
		if (!next.getSymbol().equals("subroutine"))
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Esperado comando SUBROUTINE"));

		// if (next.getSymbol().equals("subroutine")) {
		index++;
		next = this.symbols.getToken(index);
		if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
			String nome = next.getSymbol();
			if (!simbolos_declarados.contains(nome)) {
				simbolos_declarados.add(new Identificador(nome, tipo, valor));
			} else {
				ExceptionManager.add(new SemanticException(next.getLinha(),
						"O Identificador '" + nome
								+ "' já foi declarado anteriormente!"));
			}
			index++;
			body();
		} else {
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Esperado Identificador da subrotina!"));
			index++;
			body();
		}
		index++;
		next = this.symbols.getToken(index);
		if (!next.getSymbol().equals("end")) {
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Esperado END para  SUBROUTINE"));
		}
		// } else
		// throw new SyntacticException(next.getLinha(),
		// "Esperado comando SUBROUTINE");
	}

	// <body> ::= <commands>
	private void body() throws SyntacticException, SemanticException {
		commands();
	}

	// <commands> ::= <command><commands> | lambida
	private void commands() throws SyntacticException, SemanticException {
		next = this.symbols.getToken(index);
		if (next.getCode() == SymbolDictionary.ID_RESERVED
				|| next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
			String simbolo = next.getSymbol();
			if ((next.getCode() == SymbolDictionary.ID_IDENTIFIER)
					|| simbolo.equals("while") || simbolo.equals("if")
					|| simbolo.equals("read") || simbolo.equals("write")) {
				command();
				index++;
				commands();
			} else
				index--;
		}
		// else
		// throw new SyntacticException(next.getLinha(),
		// "Esperado comando válido para: " + next.getSymbol());
	}

	// <command> ::= <atribuição> | <laço> | <condicional>
	// | <entrada> | <saída> | <chamadaSubroutine>
	private void command() throws SyntacticException, SemanticException {
		// next = this.symbols.getToken(index);
		String simbolo = next.getSymbol();
		if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
			ehDeclarado(simbolo);

			index++;
			next = this.symbols.getToken(index);
			// <atribuição> ::= <identificador> = <expressao> ;
			if (next.getSymbol().equals("=")) {
				acao.ag1(simbolo);
				index++;
				expressao();
				acao.ag2(next.getSymbol());
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals(";"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(),
							"Esperado sinal ';' no final da atribuição"));
			} else if (next.getSymbol().equals(";")) {
				// <chamadaSubRoutine> ::= <identificador>
			} else
				index--;

		} else if (next.getCode() == SymbolDictionary.ID_RESERVED) {
			// <laço> ::= while<expressãoLógica><body> end
			if (simbolo.equals("while")) {
				acao.ag1WHILE();
				index++;
				expressaoLogica();
				index++;
				body();
				index++;
				next = this.symbols.getToken(index);
				acao.ag2WHILE();
				if (!next.getSymbol().equals("end"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado um END para o WHILE"));
				// <condicional> ::= if (<expressãoLógica>) then
				// <body><resto_if> end
			} else if (simbolo.equals("if")) {
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals("("))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado '(' para IF"));
				// if (next.getSymbol().equals("(")) {
				index++;
				expressaoLogica();
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals(")"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado ')' para IF"));
				// if (next.getSymbol().equals(")")) {
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals("then"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado 'then' para IF"));
				// if (next.getSymbol().equals("then")) {
				index++;
				body();
				acao.ag5IF();
				index++;
				resto_if();
				acao.ag6IF();
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals("end"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(), "Esperado um END para o IF"));
				// } else
				// throw new SyntacticException(next.getLinha(),
				// "Esperado 'then' para IF");
				// } else
				// throw new SyntacticException(next.getLinha(),
				// "Esperado ')' para IF");
				// } else
				// throw new SyntacticException(next.getLinha(),
				// "Esperado '(' para IF");

				// <entrada> ::= read <identificador> ;
			} else if (simbolo.equals("read")) {
				index++;
				next = this.symbols.getToken(index);
				if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
					String nome = next.getSymbol();
					if (!existeIdentificador(nome)) {
						ExceptionManager.add(new SemanticException(next
								.getLinha(), "O Identificador '" + nome
								+ "' não foi declarado anteriormente!"));
					}
					index++;
					next = this.symbols.getToken(index);
					if (!next.getSymbol().equals(";"))
						ExceptionManager.add(new SyntacticException(next
								.getLinha(),
								"Esperado sinal ';' no final da atribuição"));

				} else {
					ExceptionManager.add(new SyntacticException(
							next.getLinha(),
							"Eperado um identificador para o comando READ!"));
					while (!this.symbols.getToken(index).getSymbol()
							.equals(";")) {
						index++;
					}
				}
				if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
					ehDeclarado(next.getSymbol());
				}
				// <saida> ::= write <valor>; | write <identificador>;
			} else if (simbolo.equals("write")) {
				index++;
				next = this.symbols.getToken(index);
				if (next.getCode() == SymbolDictionary.ID_NUMBER
						|| next.getCode() == SymbolDictionary.ID_STRING
						|| next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
					if ((next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
						String nome = next.getSymbol();
						ehDeclarado(nome);

					}
				}
				index++;
				next = this.symbols.getToken(index);
				if (!next.getSymbol().equals(";"))
					ExceptionManager.add(new SyntacticException(
							next.getLinha(),
							"Esperado sinal ';' no final da atribuição"));
			} else {
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Esperado argumento válido para write!"));
				while (!this.symbols.getToken(index).getSymbol().equals(";")) {
					index++;
				}
			}
			if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
				ehDeclarado(next.getSymbol());
			}

		} else if (!(next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Comando Inválido!"));
		} else {
			ExceptionManager.add(new SyntacticException(next.getLinha(), next
					.getSymbol()
					+ "Comando Inválido!"));
		}

	}

	// <resto_if>::= else <body> | lambida
	private void resto_if() throws SyntacticException, SemanticException {
		next = this.symbols.getToken(index);
		if (next.getCode() == SymbolDictionary.ID_RESERVED) {
			if (next.getSymbol().equals("else")) {
				index++;
				body();
			} else if (next.getSymbol().equals("end")) {
				index--;
			} else
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Comando Inválido!"));
			while (!this.symbols.getToken(index).getSymbol().equals("end")) {
				index++;
			}
			index--;
		} else
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Comando Inválido!"));

	}

	// <expressãoLogicaP> ::= <expressãoLógica><restoLogico>
	private void expressaoLogica() throws SyntacticException, SemanticException {
		expLogica();
		index++;
		restoLogico();
	}

	// <restoLogico> ::= <operadorLogico><expressaoLogica><restoLogico> |
	// lambida
	private void restoLogico() throws SyntacticException, SemanticException {
		next = symbols.getToken(index);
		if (next.getSymbol().equals("and") || next.getSymbol().equals("or")) {
			index++;
			expLogica();
			index++;
			restoLogico();
		} else
			index--;
	}

	// <expressãoLógica> ::= NOT<expressãoLógicaP> | <expressão> <operBinar>
	// <expressão>
	private void expLogica() throws SyntacticException, SemanticException {
		next = symbols.getToken(index);
		if (next.getSymbol().equals("NOT")) {
			index++;
			expressaoLogica();
		} else {
			acao.ag1IF();
			expressao();
			acao.ag2IF();
			index++;
			next = symbols.getToken(index);
			if (next.getCode() == SymbolDictionary.ID_BINARY_OPERATOR) {
				acao.ag3IF(next.getSymbol());
			} else
				ExceptionManager.add(new SyntacticException(next.getLinha(),
				"Um operador binário eh esperado!"));
			index++;
			expressao();
			acao.ag4IF();
		}
	}

	// <expressão> ::= <String> | <identificador> | <expressãoAritm>
	private void expressao() throws SyntacticException, SemanticException {
		next = symbols.getToken(index);

		if (next.getCode() == SymbolDictionary.ID_STRING) {
			// acao.ag2(next.getSymbol());
			acao.ag7(next.getSymbol());
		} else if (next.getCode() == SymbolDictionary.ID_IDENTIFIER
				|| next.getCode() == SymbolDictionary.ID_NUMBER) {
			//acao.ag1(next);
			expressaoAritm();
			//acao.ag2(next);
		}

		// if (next.getCode() != SymbolDictionary.ID_STRING) {// se for numero
		// expressaoAritm();

		// } else {
		// acao.ag2(next.getSymbol());

		// }

	}

	private String getNomeIdentificador() {
		index--;
		index--;
		next = symbols.getToken(index);
		String nome = next.getSymbol();
		index++;
		index++;
		next = symbols.getToken(index);
		return nome;
	}

	private void setIdentificador(String nome, String novoValor) {
		for (int k = 0; k < simbolos_declarados.size(); k++) {
			if (simbolos_declarados.get(k).getNome().equalsIgnoreCase(nome)) {
				simbolos_declarados.get(k).setValor(novoValor);
			}
		}

	}

	// <expressãoAritm> ::= <Integer><resto>
	private void expressaoAritm() throws SyntacticException, SemanticException {
//		if (existeOutroOperando()) {
//			acao.push(next.getSymbol());
//		} else {// eh uma atribuição
//			// configura o novo valor para o identificador
//			setIdentificador(getNomeIdentificador(), next.getSymbol());
//			acao.ag2(next.getSymbol());
//		}
		next = symbols.getToken(index);
		if ((next.getCode() == SymbolDictionary.ID_NUMBER)
				|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
			if ((next.getCode() == SymbolDictionary.ID_IDENTIFIER)
					&& ehDeclarado(next.getSymbol())) {
				if (ehInteiro(next.getSymbol())) {
					acao.ag5(next.getSymbol());
				}else
					ExceptionManager
					.add(new SyntacticException(next.getLinha(),
							"Operação não permitida. Verifique os tipos de identificadores."));
			} else 
				acao.ag6(next.getSymbol());

			// if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
			// ehDeclarado(next.getSymbol());
			// }
			index++;
			restoAritm();
		} else
			ExceptionManager.add(new SyntacticException(next.getLinha(),
					"Número ou identificador esperado!"));
	}

	private boolean ehInteiro(String symbol) {
		for (int k = 0; k < simbolos_declarados.size(); k++) {
			if (simbolos_declarados.get(k).getNome().equalsIgnoreCase(symbol)) {
				if(simbolos_declarados.get(k).getTipo()
						.equalsIgnoreCase("integer"))
				return true;
			}
		}
		return false;
	}

//	private boolean existeOutroOperando() {
//		index++;
//		next = symbols.getToken(index);
//		if ((next.getCode() == SymbolDictionary.ID_ARITMETIC_OPERATOR)
//				|| (next.getCode() == SymbolDictionary.ID_BINARY_OPERATOR)
//				|| (next.getCode() == SymbolDictionary.ID_UNARY_OPERATOR)) {
//			index--;
//			next = symbols.getToken(index);
//			return true;
//		}
//		index--;
//		next = symbols.getToken(index);
//		return false;
//	}

	// <resto> ::= <operAritm><Integer><resto> | lambida
	private void restoAritm() throws SyntacticException, SemanticException {
		next = symbols.getToken(index);
		if (next.getCode() == SymbolDictionary.ID_ARITMETIC_OPERATOR) {
			if (next.getSymbol().equals("+")){
				index++;
			next = symbols.getToken(index);
			if ((next.getCode() == SymbolDictionary.ID_NUMBER)
					|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
				if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
					ehDeclarado(next.getSymbol());
					if (ehInteiro(next.getSymbol())) {
						acao.ag5(next.getSymbol());
					}else
						ExceptionManager
						.add(new SyntacticException(next.getLinha(),
								"Operação não permitida. Verifique os tipos de identificadores."));
				} else 
					acao.ag6(next.getSymbol());
				index++;
				restoAritm();
				acao.ag3();
			} else
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Número ou identificador esperado!"));
				//acao.ag3();
			}
			else if (next.getSymbol().equals("-")){
				index++;
				next = symbols.getToken(index);
				if ((next.getCode() == SymbolDictionary.ID_NUMBER)
						|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
					if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
						ehDeclarado(next.getSymbol());
						if (ehInteiro(next.getSymbol())) {
							acao.ag5(next.getSymbol());
						}else
							ExceptionManager
							.add(new SyntacticException(next.getLinha(),
									"Operação não permitida. Verifique os tipos de identificadores."));
					} else 
						acao.ag6(next.getSymbol());

					index++;
					restoAritm();
					acao.ag33();
				} else
					ExceptionManager.add(new SyntacticException(next.getLinha(),
							"Número ou identificador esperado!"));			
				//acao.ag33();
			}
			else if (next.getSymbol().equals("*")){
				index++;
				next = symbols.getToken(index);
				if ((next.getCode() == SymbolDictionary.ID_NUMBER)
						|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
					if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
						ehDeclarado(next.getSymbol());
						if (ehInteiro(next.getSymbol())) {
							acao.ag5(next.getSymbol());
						}else
							ExceptionManager
							.add(new SyntacticException(next.getLinha(),
									"Operação não permitida. Verifique os tipos de identificadores."));
					} else 
						acao.ag6(next.getSymbol());

					index++;
					restoAritm();
					acao.ag4();
				} else
					ExceptionManager.add(new SyntacticException(next.getLinha(),
							"Número ou identificador esperado!"));
				//acao.ag4();
			}
			else if (next.getSymbol().equals("**")){
				index++;
				next = symbols.getToken(index);
				if ((next.getCode() == SymbolDictionary.ID_NUMBER)
						|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
					if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
						ehDeclarado(next.getSymbol());
						if (ehInteiro(next.getSymbol())) {
							acao.ag5(next.getSymbol());
						}else
							ExceptionManager
							.add(new SyntacticException(next.getLinha(),
									"Operação não permitida. Verifique os tipos de identificadores."));
					} else 
						acao.ag6(next.getSymbol());

					index++;
					restoAritm();
					acao.ag444();
				} else
					ExceptionManager.add(new SyntacticException(next.getLinha(),
							"Número ou identificador esperado!"));
				//acao.ag444();
			}
			else if (next.getSymbol().equals("/")){
				index++;
				next = symbols.getToken(index);
				if ((next.getCode() == SymbolDictionary.ID_NUMBER)
						|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
					if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
						ehDeclarado(next.getSymbol());
						if (ehInteiro(next.getSymbol())) {
							acao.ag5(next.getSymbol());
						}else
							ExceptionManager
							.add(new SyntacticException(next.getLinha(),
									"Operação não permitida. Verifique os tipos de identificadores."));
					} else 
						acao.ag6(next.getSymbol());

					index++;
					restoAritm();
					acao.ag44();
				} else
					ExceptionManager.add(new SyntacticException(next.getLinha(),
							"Número ou identificador esperado!"));	
				//acao.ag44();
			}
			/*
			index++;
			next = symbols.getToken(index);
			if ((next.getCode() == SymbolDictionary.ID_NUMBER)
					|| (next.getCode() == SymbolDictionary.ID_IDENTIFIER)) {
				if (next.getCode() == SymbolDictionary.ID_IDENTIFIER) {
					ehDeclarado(next.getSymbol());
					if (ehInteiro(next.getSymbol())) {
						acao.ag5(next.getSymbol());
					}else
						ExceptionManager
						.add(new SyntacticException(next.getLinha(),
								"Operação não permitida. Verifique os tipos de identificadores."));
				} else 
					acao.ag6(next.getSymbol());

				index++;
				restoAritm();
			} else
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Número ou identificador esperado!")); */
		} else
			index--;
	}

	// <valor> ::= <Integer> | <String>
	private void valor(int codeType) throws SyntacticException {
		index++;
		next = this.symbols.getToken(index);
		if (codeType == 1) {
			if (!(next.getCode() == SymbolDictionary.ID_NUMBER)) {
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Esperado um inteiro para uma atribuição válida!"));
			}
		} else {
			if (!(next.getCode() == SymbolDictionary.ID_STRING)) {
				ExceptionManager.add(new SyntacticException(next.getLinha(),
						"Esperado uma string para uma atribuição válida!"));
			}
		}

	}

}

//private void panico(Collection<Integer> primeiro,
//		Collection<Integer> seguidor, String msg) throws Exception {
//	this.houveErro = true;
//	this.erroTransiente = true;
//	if (!primeiro.contains(this.simboloAtual)) {
//		System.out.println(msg);
//		List<Integer> uniao = new ArrayList<Integer>(primeiro);
//		uniao.addAll(seguidor);
//		while (analisadorLexico.existeProximoSimbolo(numSimbolo)
//				&& !uniao.contains(this.simboloAtual))
//			this.simboloAtual = analisadorLexico.getProximoSimbolo(
//					numSimbolo++).getCode();
//	}
//}
