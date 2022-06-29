package lox;

abstract class Expresión {
	interface Visitante<T> {
		T visitarBinariaExpresión(Binaria expresión);
		T visitarAgrupaciónExpresión(Agrupación expresión);
		T visitarLiteralExpresión(Literal expresión);
		T visitarUnariaExpresión(Unaria expresión);
	}
	static class Binaria extends Expresión {
		Binaria(Expresión izquierda, Token operador, Expresión derecha) {
			this.izquierda = izquierda;
			this.operador = operador;
			this.derecha = derecha;
		}

		@Override
		<T> T aceptar(Visitante<T> visitante) {
			return visitante.visitarBinariaExpresión(this);
		}

		final Expresión izquierda;
		final Token operador;
		final Expresión derecha;
	}
	static class Agrupación extends Expresión {
		Agrupación(Expresión expresión) {
			this.expresión = expresión;
		}

		@Override
		<T> T aceptar(Visitante<T> visitante) {
			return visitante.visitarAgrupaciónExpresión(this);
		}

		final Expresión expresión;
	}
	static class Literal extends Expresión {
		Literal(Object valor) {
			this.valor = valor;
		}

		@Override
		<T> T aceptar(Visitante<T> visitante) {
			return visitante.visitarLiteralExpresión(this);
		}

		final Object valor;
	}
	static class Unaria extends Expresión {
		Unaria(Token operador, Expresión expresión) {
			this.operador = operador;
			this.expresión = expresión;
		}

		@Override
		<T> T aceptar(Visitante<T> visitante) {
			return visitante.visitarUnariaExpresión(this);
		}

		final Token operador;
		final Expresión expresión;
	}

	abstract <T> T aceptar(Visitante<T> visitante);
}