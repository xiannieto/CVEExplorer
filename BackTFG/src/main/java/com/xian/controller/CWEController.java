package com.xian.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xian.model.CWE;
import com.xian.service.CWEService;

@RestController
@RequestMapping("/api/cwes")
@Transactional
public class CWEController {

	private final static Logger logger = LoggerFactory.getLogger(CWEController.class);

	@Autowired
	private CWEService cweService;

	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<List<CWE>> getAllCwe() {
		List<CWE> cweList = null;
		try {
			cweList = cweService.getAllCWE();
			logger.info("[INFO] Lista de CWE cargada con éxito!");
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido obtener la lista de los CWEs: ", e);
		}

		if (cweList == null || cweList.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(cweList);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<CWE> getCweById(@PathVariable("id") String id) {
		CWE cwe = null;
		try {
			cwe = cweService.findByCWEID(id);
			logger.info("[INFO] Obtenido CWE con ID = [ {} ] : {}", id, cwe);
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido obtener el CWE con ID = [ {} ]: ", id, e);
		}
		return ResponseEntity.ok(cwe);
	}

	@GetMapping("{id}/ancestors")
	@ResponseBody
	public ResponseEntity<List<String>> getAncestorsIDS(@PathVariable String id) {
		List<String> ancestors = null;
		try {
			ancestors = cweService.getAncestorIDs(id);
			logger.info("[INFO] Lista de ancestros para el ID -- [ {} ] -- : {}", id, ancestors);
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido obtener la lista de ancestros para el ID [ {} ]: ", id, e);
		}

		if (ancestors == null || ancestors.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(ancestors);
	}

	@GetMapping("/roots")
	@ResponseBody
	public ResponseEntity<List<CWE>> findRoots() {
		List<CWE> roots = null;
		try {
			roots = cweService.findRoots();
			logger.info("[INFO] Cargada lista de CWEs raíz (roots).");
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido obtener la lista de CWEs raíz: ", e);
		}

		if (roots == null || roots.isEmpty()) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(roots);
	}

	@GetMapping("/{id}/getChildren")
	@ResponseBody
	public ResponseEntity<List<String>> getChildren(@PathVariable String id) {
	    List<String> children = null;
	    try {
	        children = cweService.getChildren(id);
	        logger.info("[INFO] Lista de hijos para el ID -- [ {} ] -- : {}", id, children);
	    } catch (Exception e) {
	        logger.error("[ERROR] No se ha podido obtener la lista de hijos para el ID: [ {} ]", id, e);
	    }

	    if (children == null || children.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok(children);
	}

}
