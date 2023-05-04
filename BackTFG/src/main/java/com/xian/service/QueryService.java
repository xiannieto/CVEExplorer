package com.xian.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.xian.controller.CWEController;
import com.xian.dto.QueryDTO;
import com.xian.dto.QueryResultsDTO;
import com.xian.dto.ResultPair;

@SuppressWarnings("deprecation")
@Service
@CrossOrigin(origins = "http://localhost:4200")
public class QueryService {

	private final static Logger logger = LoggerFactory.getLogger(CWEController.class);

	@Autowired
	private RestHighLevelClient client;

	public QueryResultsDTO searchCVEs(QueryDTO queryDTO) {
	    QueryResultsDTO queryResultsDTO = new QueryResultsDTO();
	    try {
	        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
	        if (queryDTO.getDescription() != null && !queryDTO.getDescription().isEmpty()) {
	            queryBuilder.must(QueryBuilders.matchQuery("description", queryDTO.getDescription()));
	        }
	        if (queryDTO.getAssigner() != null && !queryDTO.getAssigner().isEmpty()) {
	            queryBuilder.must(QueryBuilders.matchQuery("assigner", queryDTO.getAssigner()));
	        }
	        if (queryDTO.getCwes() != null && !queryDTO.getCwes().isEmpty()) {

	            queryBuilder.should(QueryBuilders.termsQuery("cwes", queryDTO.getCwes()));
	            queryBuilder.should(QueryBuilders.termsQuery("cwesWithAncestors", queryDTO.getCwes()));
	            queryBuilder.minimumShouldMatch(1);
	        }
	        if (queryDTO.getVendors() != null && !queryDTO.getVendors().isEmpty()) {
	            List<String> vendorKeys = queryDTO.getVendors().stream().map(vendor -> vendor + "/*")
	                    .collect(Collectors.toList());
	            queryBuilder.must(QueryBuilders.termsQuery("vendorProductPairs", vendorKeys));
	        }
	        if (queryDTO.getVendorProductPairs() != null && !queryDTO.getVendorProductPairs().isEmpty()) {
	            queryBuilder.must(QueryBuilders.termsQuery("vendorProductPairs", queryDTO.getVendorProductPairs()));
	        }
	        if (queryDTO.getAttackVectors() != null && !queryDTO.getAttackVectors().isEmpty()) {
	            queryBuilder.must(QueryBuilders.termsQuery("impact.attackVector", queryDTO.getAttackVectors()));
	        }
	        SearchRequest searchRequest = new SearchRequest("cve_index");
	        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
	        System.out.println("ANTES DE EJECUTAR CONSULTA  ---------------------  " + queryBuilder);
	        searchSourceBuilder.query(queryBuilder);
	        searchRequest.source(searchSourceBuilder);
	        SearchResponse searchResponse = null;
	        try {
	            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
	        } catch (IOException e) {
	            logger.error("[ERROR] No se ha podido hacer la búsqueda: ", e);
	            e.printStackTrace();
	        }
	        SearchHits hits = searchResponse.getHits();

	        queryResultsDTO.setResultCount(hits.getTotalHits().value);
	        if (hits.getTotalHits().value > 0) {
	            queryResultsDTO.setMaxScore(hits.getMaxScore());
	            List<ResultPair> results = new ArrayList<>();
	            for (SearchHit hit : hits) {
	                ResultPair resultPair = new ResultPair(hit.getId(), hit.getScore());
	                results.add(resultPair);
	            }
	            queryResultsDTO.setResults(results);
	        }
	    } catch (Exception e) {
	        logger.error("[ERROR] Algo ha salido mal en la busqueda: ", e);
	    }
	    return queryResultsDTO;
	}

	public List<String> getCwes() throws IOException {
        SearchRequest searchRequest = new SearchRequest("cve_index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(AggregationBuilders.terms("cwes").field("cwes"));
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        Terms terms = searchResponse.getAggregations().get("cwes");

        List<String> cwes = new ArrayList<>();
        for (Terms.Bucket bucket : terms.getBuckets()) {
            cwes.add(bucket.getKeyAsString());
        }
        return cwes;
    }


}
