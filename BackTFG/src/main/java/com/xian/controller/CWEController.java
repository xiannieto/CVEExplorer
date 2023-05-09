package com.xian.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xian.model.CWE;
import com.xian.service.CWEService;

@RestController
@RequestMapping("/api/cwes")
@Transactional
@CrossOrigin
public class CWEController {

	private final static Logger logger = LoggerFactory.getLogger(CWEController.class);

	@Autowired
	private CWEService cweService;

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

	@GetMapping("/roots/")
	@ResponseBody
	public ResponseEntity<Object> findRoots(@RequestParam(defaultValue = "0") int pageNumber,
			@RequestParam(defaultValue = "10") int pageSize) {
		List<CWE> roots = null;
		long totalResults = 0;
		try {
			roots = cweService.findRoots(pageNumber, pageSize);
			totalResults = cweService.countAllCVEs();
			logger.info("[INFO] Cargada lista de CWEs raíz (roots).");
		} catch (Exception e) {
			logger.error("[ERROR] No se ha podido obtener la lista de CWEs raíz: ", e);
		}

		if (roots == null || roots.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Map<String, Object> response = new HashMap<>();
		response.put("cwes", roots);
		response.put("totalResults", totalResults);
		return ResponseEntity.ok(response);
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
	
//	@GetMapping("/?pattern=^Buffer")
//	@ResponseBody
//	public ResponseEntity<List<CWE>> getPattern(@RequestParam String pattern) {
//	    CWE children = null;
//	    try {
//	        children = cweService.findByName(pattern);
//	        logger.info("[INFO] Lista de hijos para el ID -- [ {} ] -- : {}",children);
//	    } catch (Exception e) {
//	        logger.error("[ERROR] No se ha podido obtener la lista de hijos para el ID: [ {} ]", id, e);
//	    }
//
//	    if (children == null || children.isEmpty()) {
//	        return ResponseEntity.notFound().build();
//	    }
//
//	    return ResponseEntity.ok(children);
//	}

}
