package com.xian;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.xian.model.CWE;
import com.xian.service.CVEService;

@SpringBootApplication
public class TfgApplication implements CommandLineRunner {

	@Autowired
	private CVEService cveService;

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
		// Crear instancias de prueba de la clase CWE
		CWE cwe1 = new CWE("CWE-1", "Nombre 1", "Descripción 1", "Descripción extendida 1", new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());
		CWE cwe2 = new CWE("CWE-2aaa", "Nombre 2", "Descripción 2", "Descripción extendida 2", new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>(), new HashSet<>());

		// Utilizar el método addChild para agregar una relación entre cwe1 y cwe2
		cwe1.addChild(cwe2);

		// Verificar si el método addChild ha agregado la relación correctamente
		Set<String> children = cwe1.getChildren();
		Set<String> parents = cwe2.getParents();

		System.out.println(cwe1.getChildren());
		System.out.println(cwe2.getParents());
	}
}