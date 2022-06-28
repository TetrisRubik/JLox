package herramientas;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

public class Generador_ASA {
	public static void main(String[] args) throws IOException {
		if (args.length != 1) {
			System.err.println("Uso: generador_asa <directorio de salida>");
			System.exit(64);
		}
		String salidad_dir = args[0];
		definir_ASA(salidad_dir, "Expresión", Arrays.asList(
				"Binaria    : Expresión izquierda, Token operador, Expresión derecha",
				"Agrupación : Expresión expresión",
				"Literal    : Object valor",
				"Unaria     : Token operador, Expresión expresión"));
	}

	private static void definir_ASA(
			String salida_dir, String base_nombre, List<String> tipos) throws IOException {
		String ruta = salida_dir + "/" + base_nombre + ".java";
		PrintWriter escriba = new PrintWriter(ruta, "UTF-8");

		escriba.println("package lox;");
		escriba.println();
		escriba.println("import java.util.List;");
		escriba.println();
		escriba.println("abstract class " + base_nombre + " {");

		definir_visitante(escriba, base_nombre, tipos);

		// La clase ASA.
		for (String tipo : tipos) {
			String clase_nombre = tipo.split(":")[0].trim();
			String campo = tipo.split(":")[1].trim();
			definir_tipo(escriba, base_nombre, clase_nombre, campo);
		}

		// La base de aceptar().
		escriba.println();
		escriba.println("\tabstract <T> T aceptar(Visitante<T> visitante);");

		escriba.println("}");
		escriba.close();
	}

	private static void definir_visitante(
			PrintWriter escriba, String base_nombre, List<String> tipos) {
		escriba.println("\tinterface Visitante<T> {");

		for (String tipo : tipos) {
			String tipo_nombre = tipo.split(":")[0].trim();
			escriba.println("\t\tT visitar" + tipo_nombre + base_nombre + "(" +
					tipo_nombre + " " + base_nombre.toLowerCase() + ");");
		}

		escriba.println("\t}");
	}

	private static void definir_tipo(
			PrintWriter escriba, String base_nombre, String clase_nombre, String lista_campos) {
		escriba.println("\tstatic class " + clase_nombre + " extends " + base_nombre + " {");

		// Constructor.
		escriba.println("\t\t" + clase_nombre + "(" + lista_campos + ") {");

		// Guarda parámetros en campo.
		String[] campos = lista_campos.split(", ");
		for (String campo : campos) {
			String nombre = campo.split(" ")[1];
			escriba.println("\t\t\tthis." + nombre + " = " + nombre + ";");
		}

		escriba.println("\t\t}");

		// Patrón visitante.
		escriba.println();
		escriba.println("\t\t@Override");
		escriba.println("\t\t<T> T aceptar(Visitante<T> visitante) {");
		escriba.println("\t\t\treturn visitante.visitar" + clase_nombre + base_nombre + "(this);");
		escriba.println("\t\t}");

		// Campos.
		escriba.println();
		for (String field : campos) {
			escriba.println("\t\tfinal " + field + ";");
		}

		escriba.println("\t}");
	}
}