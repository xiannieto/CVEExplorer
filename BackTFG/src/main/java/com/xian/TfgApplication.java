package com.xian;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TfgApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TfgApplication.class, args);
	}

	public void run(String... args) throws Exception {
//		if (args.length > 0) {
//			String jsonFilePath = args[0];
//			File file = new File(jsonFilePath);
//			if (file.exists() && file.isFile()) {
//				cveService.loadFromJSON(jsonFilePath);
//			} else {
//				System.err
//						.println("Error: El archivo especificado no existe o no es un archivo válido: " + jsonFilePath);
//				System.exit(1);
//			}
//		} else {
//			System.err.println("Error: Debe especificar la ruta del archivo JSON como primer argumento.");
//			System.exit(1);
//		}
	}
}