package lox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Lox {
	static boolean hay_error = false;

	public static void main(String[] args) {
		try {
			if (args.length > 1) {
				System.out.println("Uso: jlox [archivo]");
				System.exit(64);
			} else if (args.length == 1) {
				ejecutar_archivo(args[0]);
			} else {
				ejecutar_interactivo();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void ejecutar_archivo(String ruta) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(ruta));
		ejecutar(new String(bytes, Charset.defaultCharset()));
		// Indica si hay algún error como salida.
		if (hay_error)
			System.exit(65);
	}

	private static void ejecutar_interactivo() throws IOException {
		InputStreamReader entrada = new InputStreamReader(System.in);
		BufferedReader espacio_entrada = new BufferedReader(entrada);

		while (true) {
			System.out.print("> ");
			String texto = espacio_entrada.readLine();
			if (texto == null)
				break;
			ejecutar(texto);

			hay_error = false;
		}
	}

	private static void ejecutar(String texto) {
		Lector lector = new Lector(texto);
		List<Token> tókenes = lector.obtener_tókenes();
		// Imprime cada uno de los tókenes.
		for (Token token : tókenes) {
			System.out.println(token);
		}
	}

	static void error(int línea, String mensaje) {
		reportar(línea, "", mensaje);
	}

	private static void reportar(int línea, String lugar, String mensaje) {
		System.err.println("[línea " + línea + "] Error" + lugar + ": " + mensaje);
		hay_error = true;
	}
}