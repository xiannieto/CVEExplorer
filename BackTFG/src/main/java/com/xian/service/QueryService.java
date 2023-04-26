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
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.xian.controller.CWEController;
import com.xian.dto.QueryDTO;
import com.xian.model.CVE;

@Service
@CrossOrigin(origins = "http://localhost:4200")
public class QueryService {

	private final static Logger logger = LoggerFactory.getLogger(CWEController.class);

	@SuppressWarnings("deprecation")
	@Autowired
	private RestHighLevelClient client;

	@Autowired
	private ObjectMapper objectMapper;

	public List<CVE> searchCVEs(QueryDTO queryDTO) {
		try {
			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
			if (queryDTO.getDescription() != null && !queryDTO.getDescription().isEmpty()) {
				queryBuilder.must(QueryBuilders.matchQuery("description", queryDTO.getDescription()));
			}
			if (queryDTO.getAssigner() != null && !queryDTO.getAssigner().isEmpty()) {
				queryBuilder.must(QueryBuilders.matchQuery("assigner", queryDTO.getAssigner()));
			}
			if (queryDTO.getCwes() != null && !queryDTO.getCwes().isEmpty()) {
				queryBuilder.must(QueryBuilders.termsQuery("cwesWithAncestors", queryDTO.getCwes()));
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
			List<CVE> results = new ArrayList<>();
			for (SearchHit hit : hits) {
				results.add(objectMapper.convertValue(hit.getSourceAsMap(), CVE.class));
			}
			return results;
		} catch (Exception e) {
			logger.error("[ERROR] Algo ha salido mal en la busqueda: ", e);
		}
		return null;
	}

//	@SuppressWarnings("deprecation")
//	public List<CVE> searchCVEs(String cveID, String assigner, String description) {
//		try {
//			BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
//			if (cveID != null && !cveID.isEmpty()) {
//				queryBuilder.must(QueryBuilders.matchQuery("cveID", cveID));
//			}
//			if (assigner != null && !assigner.isEmpty()) {
//				queryBuilder.must(QueryBuilders.matchQuery("assigner", assigner));
//			}
//			if (description != null && !description.isEmpty()) {
//				queryBuilder.must(QueryBuilders.matchQuery("description", description));
//			}
//			SearchRequest searchRequest = new SearchRequest("cve_index");
//			SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
//			searchSourceBuilder.query(queryBuilder);
//			searchRequest.source(searchSourceBuilder);
//			SearchResponse searchResponse = null;
//			try {
//				searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
//			} catch (IOException e) {
//				logger.error("[ERROR] No se ha podido hacer la búsqueda: ", e);
//				e.printStackTrace();
//			}
//			SearchHits hits = searchResponse.getHits();
//			List<CVE> results = new ArrayList<>();
//			for (SearchHit hit : hits) {
//				results.add(objectMapper.convertValue(hit.getSourceAsMap(), CVE.class));
//			}
//			return results;
//		} catch (Exception e) {
//			logger.error("[ERROR] Algo ha salido mal en la busqueda: ", e);
//		}
//		return null;
//	}

}
