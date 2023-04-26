package com.xian.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.xian.model.CVE;

@Repository
public interface CVERepository extends ElasticsearchRepository<CVE, String> {

}
