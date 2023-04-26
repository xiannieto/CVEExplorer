package com.xian.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.xian.model.CWE;

public interface CWERepository extends ElasticsearchRepository<CWE, String> {

}
