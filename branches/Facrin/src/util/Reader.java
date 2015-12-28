package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
 * Classe responsável por auxiliar na leitura e manipulação de caracteres do
 * arquivo de entrada
 */
public class Reader {

	private BufferedReader br;
	private String lastLine;
	private int pos;
	private int linha;

	/**
	 * Constroi um leitor para o arquivo de entrada
	 * 
	 * @param fileName
	 * @throws IOException
	 */
	public Reader(String fileName) throws IOException {
		this.pos = 0;
		this.linha = 1;
		this.br = new BufferedReader(new FileReader(fileName));
		this.lastLine = br.readLine();
	}

	/**
	 * Lê linhas e retorna em ordem cada caracter da linha
	 * 
	 * @return o caracter lido
	 * @throws IOException
	 */
	public char read() throws IOException {
		char returnChar = ' ';

		while (lastLine == null || lastLine.equals("")) {
			if (lastLine == null) {
				return (char) -1;
			}
			lastLine = br.readLine();
			linha++;
			pos = 0;
		}
		if (pos > 0 && pos == (lastLine.length())) {
			returnChar = '\n';
			linha++;
			pos = 0;
			lastLine = br.readLine();
		} else {
			returnChar = lastLine.charAt(pos);
			pos++;
		}
/*
		if (lastLine == null) {
			return (char) -1;
		} else if (lastLine.equals("")) {
			lastLine = br.readLine();
			if ((lastLine = br.readLine()) == null) {
				return (char) -1;
			} else {

				pos++;
				return lastLine.charAt(0);
			}
		} else {
			if (pos == (lastLine.length())) {
				returnChar = '\n';
				linha++;
				pos = 0;
				lastLine = br.readLine();
			} else {
				returnChar = lastLine.charAt(pos);
				pos++;
			}
		} */
		return returnChar;
	}

	/**
	 * Retorna o char lido anteriormente
	 * 
	 * @return char anterior
	 */
	public char readBackwards() {
		char returnChar = ' ';

		if (pos == 0)
			return (char) -1;
		else {
			pos -= 1;
			returnChar = lastLine.charAt(pos);
		}

		return returnChar;
	}

	/***
	 * Obtem o numero da linha
	 */
	public int getLinha() {
		return linha;
	}

	/**
	 * Encerrar o BufferedReader
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException {
		br.close();
	}

	public int getpos() {
		return pos;
	}
}
