package com.xian.dto;

import java.io.Serializable;
import java.util.List;

public class QueryResultsDTO implements Serializable {
	private int resultCount; // numero total de CVE encontrados
	private double maxScore; // score máximo del primer resultado
	private List<ResultPair> results; // lista ordenada de resultados (cveID + score)
}
