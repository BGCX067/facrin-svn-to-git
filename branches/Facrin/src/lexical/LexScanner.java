package lexical;


import java.io.IOException;
import java.util.Iterator;

import exception.ExceptionManager;
import exception.LexicalException;
import util.Reader;
import util.Token;

/*
 *      UNIVERSIDADE FEDERAL DE CAMPINA GRANDE
 *      CENTRO DE ENGENHARIA ELÉTRICA E INFORMÁTICA
 *      UNIDADE ACADÊMICA DE SISTEMAS E INFORMAÇÃO
 *      DISCIPLINA: COMPILADORES
 *      PROFESSOR: MARCUS SALERNO  DE AQUINO
 *      PERÍODO: 2009.1
 *      PROJETO - PARTE II:  ANALISADOR LÉXICO
 *
 *      EQUIPE 5:
 *
 *      CAYO M. AMORIM                  MATR.:20611119
 *      DAYANE P. M. GAUDÊNCIO          MATR.:20411032
 *      RENATA M. SARAIVA               MATR.:20521158
 * 
 */
/**
 * Classe responsavel pelo processamento do analisador Léxico
 */
public class LexScanner {

	private SymbolDictionary dictionary;
	public Reader br;

	/**
	 * Construtor
	 * 
	 * @param fileName
	 * @param outFileName
	 * @throws IOException
	 */
	public LexScanner(String fileName, String outFileName) throws IOException {
		this.dictionary = SymbolDictionary.getInstance(outFileName);
		this.br = new Reader(fileName);
	}

	/**
	 * Corresponde a implementação da máquina de estados do analisador Léxico
	 * 
	 * @throws IOException
	 * @throws LexicalException
	 */
	public void execute() throws IOException, LexicalException {

		char character = ' ';
		String symbol = "";
		while ((character = (char) br.read()) != 65535) {

			symbol = "";
			// Checagem de quando a entrada inicia com letra
			if (SymbolDictionary.isLetter(character)) {
				while (SymbolDictionary.isLetter(character)
						|| SymbolDictionary.isNumber(character)) {
					if (character != '\n') {
						symbol += character;
						character = (char) br.read();
					} else {
						character = ' ';
					}

				}
				if (symbol.length() > 25) {
					symbol = symbol.substring(0, 25);
					ExceptionManager.add(new LexicalException(br.getLinha(),
							symbol + " - Limite de caracteres (25)"));
				}
				// se o caracter lido não é nem letra e nem numero
				// o character recebe o char lido anteriormente e o
				// simbolo formado eh gravado como token e este adicionado na
				// tabela de simbolos
				if (!SymbolDictionary.isLetter(character)
						&& !SymbolDictionary.isNumber(character)) {
					character = (char) br.readBackwards();
				}
				dictionary.add(new Token(dictionary.getID(symbol), symbol, (br
						.getpos() == 0) ? br.getLinha() - 1 : br.getLinha()));

				// Checagem de quando a entrada inicia com numero. Falha se o
				// numero esta na primeira posição de um token.
			} else if (SymbolDictionary.isNumber(character)) {
				symbol += character;
				character = (char) br.read();
				if (SymbolDictionary.isLetter(character)) {
					ExceptionManager.add(new LexicalException(br.getLinha(),
							symbol + " - numero com letra"));

				} else {
					while (SymbolDictionary.isNumber(character)) {
						symbol += character;
						character = (char) br.read();
					}

					// se o caracter lido não é nem letra e nem numero
					// o character recebe o char lido anteriormente e o
					// simbolo formado eh gravado como token e este adicionado
					// na
					// tabela de simbolos
					if (!SymbolDictionary.isLetter(character)
							&& !SymbolDictionary.isNumber(character)) {
						character = (char) br.readBackwards();
					} else if (SymbolDictionary.isLetter(character)) {
						ExceptionManager.add(new LexicalException(
								br.getLinha(), symbol + " - numero com letra"));
						while (SymbolDictionary.isNumber(character)
								|| SymbolDictionary.isLetter(character)) {
							symbol += character;
							character = (char) br.read();
						}
					}
				}
				dictionary.add(new Token(dictionary.getID(symbol), symbol, (br
						.getpos() == 0) ? br.getLinha() - 1 : br.getLinha()));
				// Checagem de Simbolos especiais
			} else if (SymbolDictionary.isSymbol(character)) {
				Token t = null;
				if (character == '>') {
					symbol += character;
					character = (char) br.read();
					if (character == '=') {
						symbol += character;
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					} else {
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
						character = (char) br.readBackwards();
					}
				} else if (character == '<') {
					symbol += character;
					character = (char) br.read();
					if (character == '=') {
						symbol += character;
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					} else {
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
						character = (char) br.readBackwards();
					}
				} else if (character == '=') {
					symbol += character;
					character = (char) br.read();
					if (character == '=') {
						symbol += character;
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					} else {
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
						character = (char) br.readBackwards();
					}
				} else if (character == '"') {
					symbol += character;
					character = (char) br.read();
					while (character != '"') {
						symbol += character;
						character = (char) br.read();
					}
					symbol += character;
					if (symbol.length() > 150 + 2) {
						ExceptionManager.add(new LexicalException(br.getLinha(), symbol
								+ " - Limite de caracteres para String(150)"));
						symbol = symbol.substring(0, 151) + "\"" ;
					}
					t = new Token(dictionary.getID(symbol), symbol, (br
							.getpos() == 0) ? br.getLinha() - 1 : br.getLinha());
				} else if ((character == '+') || (character == '-')) {
					symbol += character;
					t = new Token(dictionary.getID(symbol), symbol, (br
							.getpos() == 0) ? br.getLinha() - 1 : br.getLinha());
				} else if (character == '/') {
					symbol += character;
					character = (char) br.read();
					if (character == '=') {
						symbol += character;
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					} else
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
				} else if (character == '*') {
					symbol += character;
					character = (char) br.read();
					if (character == '*') {
						symbol += character;
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					} else {
						t = new Token(dictionary.getID(symbol), symbol, (br
								.getpos() == 0) ? br.getLinha() - 1 : br
								.getLinha());
					}
				} else if ((character == '(') || (character == ')')
						|| (character == ',')) {
					symbol += character;
					t = new Token(dictionary.getID(symbol), symbol, (br
							.getpos() == 0) ? br.getLinha() - 1 : br.getLinha());
				} else if (character == ';') {
					symbol += character;
					t = new Token(dictionary.getID(symbol), symbol, (br
							.getpos() == 0) ? br.getLinha() - 1 : br.getLinha());
				} else {

					t = new Token(dictionary.getID(symbol), symbol, (br
							.getpos() == 0) ? br.getLinha() - 1 : br.getLinha());
				}

				if (!symbol.equals(""))
					dictionary.add(t);
			} else {

			}

		}// while
		br.close();
		dictionary.save();
	}

	/**
	 * Um Iterator para a tabela de simbolos
	 * 
	 * @return dictionary.iterator()
	 */
	public Iterator<Token> iterator() {
		return this.dictionary.iterator();
	}

	/**
	 * Obtém uma instancia da classe SymbolDictionary.
	 * 
	 * @return dictionary
	 */
	public SymbolDictionary getDictionary() {
		return this.dictionary;
	}

}
