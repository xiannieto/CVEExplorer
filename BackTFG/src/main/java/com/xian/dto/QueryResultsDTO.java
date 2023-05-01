package com.xian.dto;

import java.io.Serializable;
import java.util.List;

public class QueryResultsDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float resultCount; // numero total de CVE encontrados
	private double maxScore; // score máximo del primer resultado
	private List<ResultPair> results; // lista ordenada de resultados (cveID + score)
	
	public QueryResultsDTO() {
		super();
	}
	
	public QueryResultsDTO(float resultCount, double maxScore, List<ResultPair> results) {
		super();
		this.resultCount = resultCount;
		this.maxScore = maxScore;
		this.results = results;
	}

	public float getResultCount() {
		return resultCount;
	}
	public void setResultCount(float resultCount) {
		this.resultCount = resultCount;
	}
	public double getMaxScore() {
		return maxScore;
	}
	public void setMaxScore(double maxScore) {
		this.maxScore = maxScore;
	}
	public List<ResultPair> getResults() {
		return results;
	}
	public void setResults(List<ResultPair> results) {
		this.results = results;
	}
	
	
}
