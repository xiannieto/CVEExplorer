package com.xian.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.xian.model.CVE;
import com.xian.service.CVEService;

@RestController
@RequestMapping("/api/cves")
@Transactional
@CrossOrigin
public class CVEController {
	private final static Logger logger = LoggerFactory.getLogger(CVEController.class);

	@Autowired
	private CVEService cveService;

	@GetMapping("/")
	@ResponseBody
	public ResponseEntity<Object> getAllCVE() {
	    List<CVE> cveList = null;
	    try {
	        cveList = cveService.getAllCVE();
	        logger.info("[INFO] Lista de cve argada con éxito! : {}", cveList);
	    } catch (Exception e) {
	        logger.error("[ERROR] No se ha podido obtener la lista de cves: ", e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return  ResponseEntity.ok(cveList);
	}

	@GetMapping("/{id}")
	@ResponseBody
	public ResponseEntity<CVE> getCVEById(@PathVariable("id") String id) {
	    CVE cve = new CVE();
	    try {
	        cve = cveService.getCVEById(id);
	        logger.info("[INFO] Obtenido CVE con ID = [ {} ] : {}", id, cve);
	    } catch (Exception e) {
	        logger.error("[ERROR] No se ha podido obtener el CVE {}: ", id, e);
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	    return  ResponseEntity.ok(cve);
	}

//	@PostMapping("/index-cve")
//	public ResponseEntity<String> indexCVE(@RequestParam("filePath") String filePath) throws IOException {
//		try {
//			cveService.loadFromJSON(filePath);
//			return new ResponseEntity<>("El archivo ha sido indexado correctamente", HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>("El archivo no se ha podido indexar", HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

}
