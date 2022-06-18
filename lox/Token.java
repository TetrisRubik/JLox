package lox;

public class Token {
	final Token_Tipo tipo;
	final String lexema;
	final Object literal;
	final int línea;

	Token(Token_Tipo tipo, String lexema, Object literal, int línea) {
		this.tipo = tipo;
		this.lexema = lexema;
		this.literal = literal;
		this.línea = línea;
	}

	public String toString() {
		return tipo + " " + lexema + " " + literal;
	}
}