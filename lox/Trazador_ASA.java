package lox;

public class Trazador_ASA implements Expresión.Visitante<String> {
	String trazar(Expresión expresión) {
		return expresión.aceptar(this);
	}

	public String visitarBinariaExpresión(Expresión.Binaria expresión) {
		return parentear(expresión.operador.lexema,
				expresión.izquierda, expresión.derecha);
	}

	public String visitarAgrupaciónExpresión(Expresión.Agrupación expresión) {
		return parentear("grupo", expresión.expresión);
	}

	public String visitarLiteralExpresión(Expresión.Literal expresión) {
		if (expresión.valor == null)
			return "nil";
		return expresión.valor.toString();
	}

	public String visitarUnariaExpresión(Expresión.Unaria expresión) {
		return parentear(expresión.operador.lexema, expresión.expresión);
	}

	private String parentear(String nombre, Expresión... expresiones) {
		StringBuilder cadenero = new StringBuilder();

		cadenero.append("(").append(nombre);
		for (Expresión expresión : expresiones) {
			cadenero.append(" ");
			cadenero.append(expresión.aceptar(this));
		}
		cadenero.append(")");

		return cadenero.toString();
	}

	public static void main(String[] args) {
		Expresión expresión = new Expresión.Binaria(
				new Expresión.Unaria(new Token(Token_Tipo.MENOS, "-", null, 1), new Expresión.Literal(123)),
				new Token(Token_Tipo.ASTERISCO, "*", null, 1),
				new Expresión.Agrupación(new Expresión.Literal(45.67)));

		System.out.println(new Trazador_ASA().trazar(expresión));
	}
}