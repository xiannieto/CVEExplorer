package com.xian;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.xian.service.CVEService;

@SpringBootApplication
public class TfgApplication implements ApplicationRunner {

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	CVEService cveService;

	public static void main(String[] args) {
		SpringApplication.run(TfgApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (args.containsOption("add-cve")) {
		// Anadir fichero CVE en formato JSON
			for (String cveJSONPath : args.getOptionValues("add-cve")) {
				Logger.getLogger(TfgApplication.class.getName()).log(Level.INFO,
						"Loading CVEs from " + cveJSONPath);
				cveService.loadFromJSON(cveJSONPath);
			}

		// Finalizar la aplicacion
			SpringApplication.exit(applicationContext, () -> 0);
		}
	}

}