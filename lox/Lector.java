package lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lox.Token_Tipo.*;

class Lector {
	private final String texto;
	private final List<Token> tókenes = new ArrayList<Token>();
	private int inicio = 0;
	private int actual = 0;
	private int línea = 1;

	private static final Map<String, Token_Tipo> reservadas;

	static {
		reservadas = new HashMap<String, Token_Tipo>();
		reservadas.put("and", AND);
		reservadas.put("class", CLASS);
		reservadas.put("else", ELSE);
		reservadas.put("false", FALSE);
		reservadas.put("for", FOR);
		reservadas.put("fun", FUN);
		reservadas.put("if", IF);
		reservadas.put("nil", NIL);
		reservadas.put("or", OR);
		reservadas.put("print", PRINT);
		reservadas.put("return", RETURN);
		reservadas.put("super", SUPER);
		reservadas.put("this", THIS);
		reservadas.put("true", TRUE);
		reservadas.put("var", VAR);
		reservadas.put("while", WHILE);
	}

	Lector(String texto) {
		this.texto = texto;
	}

	List<Token> obtener_tókenes() {
		while (!en_final()) {
			// Estamos al principio del lexema.
			inicio = actual;
			buscar_token();
		}
		tókenes.add(new Token(FIN, "", null, línea));
		return tókenes;
	}

	private void buscar_token() {
		char c = siguiente();
		switch (c) {
			case '(':
				guardar_token(PAREN_IZQ);
				break;
			case ')':
				guardar_token(PAREN_DER);
				break;
			case '{':
				guardar_token(LLAVE_IZQ);
				break;
			case '}':
				guardar_token(LLAVE_DER);
				break;
			case ',':
				guardar_token(COMA);
				break;
			case '.':
				guardar_token(PUNTO);
				break;
			case '-':
				guardar_token(MENOS);
				break;
			case '+':
				guardar_token(MÁS);
				break;
			case ';':
				guardar_token(PUNTO_Y_COMA);
				break;
			case '*':
				guardar_token(ASTERISCO);
				break;
			case '!':
				guardar_token(prosigue('=') ? EXCLA_IGUAL : EXCLA);
				break;
			case '=':
				guardar_token(prosigue('=') ? IGUAL_IGUAL : IGUAL);
				break;
			case '<':
				guardar_token(prosigue('=') ? MENOR_IGUAL : MENOR);
				break;
			case '>':
				guardar_token(prosigue('=') ? MAYOR_IGUAL : MAYOR);
				break;
			case '/':
				if (prosigue('/'))
					// Todo es comentado hasta fin de línea.
					while (ojear() != '\n' && !en_final())
						siguiente();
				else
					guardar_token(BARRA);
				break;
			case ' ':
			case '\r':
			case '\t':
				// Ignora espacios.
				break;
			case '\n':
				línea++;
				break;
			case '"':
				crear_cadena();
				break;
			default:
				if (es_dígito(c))
					crea_número();
				else if (es_letra(c))
					crear_identificador();
				else
					Lox.error(línea, "Carácter inesperado.");
				break;
		}
	}

	private void crear_identificador() {
		while (es_alfanumérico(ojear()))
			siguiente();

		String palabra = texto.substring(inicio, actual);
		Token_Tipo tipo = reservadas.get(palabra);

		if (tipo == null)
			tipo = IDENTIFICADOR;
		guardar_token(tipo);
	}

	private void crea_número() {
		while (es_dígito(ojear()))
			siguiente();
		// Buscamos parte fraccional.
		if (ojear() == '.' && es_dígito(ojear_siguiente())) {
			// Consume el punto.
			siguiente();
			while (es_dígito(ojear()))
				siguiente();
		}
		guardar_token(NÚMERO, Double.parseDouble(texto.substring(inicio, actual)));
	}

	private boolean es_dígito(char c) {
		return c >= '0' && c <= '9';
	}

	private void crear_cadena() {
		while (ojear() != '"' && !en_final()) {
			if (ojear() == '\n')
				línea++;
			siguiente();
		}
		if (en_final()) {
			Lox.error(línea, "Cadena inacabada.");
			return;
		}
		// Saltar comilla de cierre.
		siguiente();
		// Guardamos el valor de la cadena.
		String valor = texto.substring(inicio + 1, actual - 1);
		guardar_token(CADENA, valor);
	}

	private boolean prosigue(char esperado) {
		if (en_final())
			return false;
		if (texto.charAt(actual) != esperado)
			return false;
		actual++;
		return true;
	}

	private char ojear() {
		if (en_final())
			return '\0';
		return texto.charAt(actual);
	}

	private char ojear_siguiente() {
		if (actual + 1 >= texto.length())
			return '\0';
		return texto.charAt(actual + 1);
	}

	private boolean es_letra(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || (c >= 'À' && c <= 'ÿ') || c == '_';
	}

	private boolean es_alfanumérico(char c) {
		return es_letra(c) || es_dígito(c);
	}

	private boolean en_final() {
		return actual >= texto.length();
	}

	private char siguiente() {
		return texto.charAt(actual++);
	}

	private void guardar_token(Token_Tipo tipo) {
		guardar_token(tipo, null);
	}

	private void guardar_token(Token_Tipo tipo, Object literal) {
		String palabra = texto.substring(inicio, actual);
		tókenes.add(new Token(tipo, palabra, literal, línea));
	}
}