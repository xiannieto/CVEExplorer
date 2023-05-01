package com.xian.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xian.dto.QueryDTO;
import com.xian.dto.QueryResultsDTO;
import com.xian.service.QueryService;

@RestController
@RequestMapping("/api/")
@Transactional
public class QueryController {

	private final static Logger logger = LoggerFactory.getLogger(QueryController.class);

	@Autowired
	private QueryService queryService;

	@PostMapping("/search")
	public ResponseEntity<QueryResultsDTO> search(@RequestBody QueryDTO queryDTO) {
		QueryResultsDTO results = null;
		try {
			results = queryService.searchCVEs(queryDTO);
			logger.info("[INFO] Query encontrada con éxito! DTO: {}", results);
		} catch (Exception e) {
			logger.error("[ERROR] Error al realizar la consulta.", e);
		}
		return new ResponseEntity<QueryResultsDTO>(results, HttpStatus.OK);
	}

}
