package lexical;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

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
 * Classe responsável por manipular os simbolos adquiridos ma classe LexScanner
 */
public class SymbolDictionary {

	public static final String ID_IDENTIFIER = "<IDENTIFICADOR>";
	public static final String ID_NUMBER = "<NUMERO>";
	public static final String ID_STRING = "<STRING>";
	public static final String ID_UNARY_OPERATOR = "<OPERADOR UNARIO>";
	public static final String ID_BINARY_OPERATOR = "<OPERADOR BINARIO>";
	public static final String ID_ARITMETIC_OPERATOR = "<OPERADOR ARITMETICO>";
	public static final String ID_ATRIBUTION_OPERATOR = "<OPERADOR DE ATRIBUICAO>";
	public static final String ID_SPECIAL_SYMBOLS = "<SIMBOLO ESPECIAL>";
	public static final String ID_RESERVED = "<PALAVRA RESERVADA>";

	private final String[] reserved = { "program", "end", "subroutine",
			"while", "if", "then", "else", "write", "read", "integer", "string" };
	private final String[] arit_operator = { "+", "-", "*", "/", "**" };
	private final String[] atrib_operator = { "=" };
	private final String[] binary_operator = { "<", ">", "<=", ">=", "==",
			"/=", "and", "or" };
	private final String[] unary_operator = { "-", "not" };
	private final String[] special_symbols = { "(", ")", "!", ",", ";" };

	private ArrayList<Token> symbolTable;
	private BufferedWriter bw;
	private static SymbolDictionary instance = null;

	/*
	 * Construtor da classe
	 */
	private SymbolDictionary(String outFileName) throws IOException {
		this.symbolTable = new ArrayList<Token>();
		this.bw = new BufferedWriter(new FileWriter(outFileName));
	}

	/**
	 * Obtem uma instancia da classe SymbolDictionary
	 * 
	 * @param outFileName
	 * @return instancia
	 * @throws IOException
	 */
	public static SymbolDictionary getInstance(String outFileName)
			throws IOException {
		if (instance == null)
			instance = new SymbolDictionary(outFileName);
		return instance;
	}

	/**
	 * Obtém o Id que corresponde ao símbolo passado como parâmentro
	 * 
	 * @param symbol
	 * @return ID do símbolo
	 */
	public String getID(String symbol) {
		if (getElement(symbol, reserved))
			return ID_RESERVED;
		else if (getElement(symbol, arit_operator))
			return ID_ARITMETIC_OPERATOR;
		else if (getElement(symbol, atrib_operator))
			return ID_ATRIBUTION_OPERATOR;
		else if (getElement(symbol, unary_operator))
			return ID_UNARY_OPERATOR;
		else if (getElement(symbol, special_symbols))
			return ID_SPECIAL_SYMBOLS;
		else if (getElement(symbol, binary_operator))
			return ID_BINARY_OPERATOR;
		else {
			try {
				Integer.parseInt(symbol);
				return ID_NUMBER;
			} catch (NumberFormatException e) {
				return (symbol.startsWith("\"")) ? ID_STRING : ID_IDENTIFIER;
			}
		}
	}

	/**
	 * verifica se o simbolo adquirido pertence a um dos conjuntos de simbolos
	 * especificados
	 * 
	 * @param symbol
	 * @param symbol_set
	 * @return true caso peretença e false caso contrário
	 */
	public static boolean getElement(String symbol, String[] symbol_set) {
		for (int i = 0; i < symbol_set.length; i++) {
			if (symbol.equals(symbol_set[i]))
				return true;
		}
		return false;
	}

	/**
	 * Verifica se o caracter passado no parametro é um simbolo ou seja, se ele
	 * nao é nem letra e nem número
	 * 
	 * @param character
	 * @return true caso seja um simbolo e false caso contrario
	 */
	public static boolean isSymbol(char character) {
		return (!isLetter(character) && !isNumber(character));
	}

	/**
	 * Verifica se o caracter passado como parametro é uma letra
	 * 
	 * @param character
	 * @return true se for letra e false caso contrário
	 */
	public static boolean isLetter(char character) {

		int charCode = (int) character;
		final int A_LOWER = 97;
		final int A_UPPER = 65;
		final int Z_LOWER = 122;
		final int Z_UPPER = 90;

		return (((charCode >= A_UPPER) && (charCode <= Z_UPPER)) || ((charCode >= A_LOWER) && (charCode <= Z_LOWER)));
	}

	/**
	 * Verifica se o caracter passado como paramentro é um numero.
	 * 
	 * @param character
	 * @return true caso seja um numero e false caso contrario
	 */
	public static boolean isNumber(char character) {
		int charCode = (int) character;
		final int ZERO = 48;
		final int NINE = 57;
		return ((charCode >= ZERO) && (charCode <= NINE));
	}

	/**
	 * Adiciona um token na tabela de simbolos
	 * 
	 * @param t
	 */
	public void add(Token t) {
		this.symbolTable.add(t);
	}

	/**
	 * Um iterator para tabela de simbolos
	 * 
	 * @return symbolTable.iterator()
	 */
	public Iterator<Token> iterator() {
		return symbolTable.iterator();
	}

	/**
	 * Obtem um token gravado na tabela de simobolos a partir de um indice dado
	 * como parâmentro.
	 * 
	 * @param index
	 * @return token.
	 * @throws IndexOutOfBoundsException
	 */
	public Token getToken(int index) throws IndexOutOfBoundsException {
		return this.symbolTable.get(index);
	}

	/**
	 * Grava a tabela de simbolos no arquivo de saída
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException {
		final String SEPARATOR = System.getProperty("line.separator");
		for (Token t : this.symbolTable) {
			bw.write(t.toString() + SEPARATOR);
		}
		bw.close();
	}

	/**
	 * Retorna o tamanho da Tabela de Símbolos
	 * 
	 * @return Tamanho da Tabela de Simbolos
	 */
	public int size() {
		return this.symbolTable.size();
	}
}
