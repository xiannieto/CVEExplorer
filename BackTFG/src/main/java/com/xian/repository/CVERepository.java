package com.xian.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.xian.model.CVE;

public interface CVERepository extends ElasticsearchRepository<CVE, String> {

}
