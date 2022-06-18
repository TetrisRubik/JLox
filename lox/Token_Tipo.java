package lox;

enum Token_Tipo {
	// Tókenes de un carácter.
	PAREN_IZQ, PAREN_DER, LLAVE_IZQ, LLAVE_DER,
	COMA, PUNTO, MENOS, MÁS, PUNTO_Y_COMA, BARRA, ASTERISCO,

	// Tókenes de dos caracteres.
	EXCLA, EXCLA_IGUAL,
	IGUAL, IGUAL_IGUAL,
	MAYOR, MAYOR_IGUAL,
	MENOR, MENOR_IGUAL,

	// Literales.
	IDENTIFICADOR, CADENA, NÚMERO,

	// Palabras reservadas.
	AND, CLASS, ELSE, FALSE, FUN, FOR, IF, NIL, OR,
	PRINT, RETURN, SUPER, THIS, TRUE, VAR, WHILE,

	FIN
}