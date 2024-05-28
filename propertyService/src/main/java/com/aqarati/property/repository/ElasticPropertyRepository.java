package com.aqarati.property.repository;

//import com.aqarati.property.model.ElasticProperty;
//import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
//import org.springframework.data.elasticsearch.annotations.Query;
//
//import java.util.List;
//
//
//public interface ElasticPropertyRepository extends ElasticsearchRepository<ElasticProperty,Long> {
//
//    @Query("{\"multi_match\": {\"query\": \"?0\",\"fields\": [\"name\", \"description\"]}}")
//    List<ElasticProperty> searchPropertyByKeyword(String keyword);
//
//}
public interface ElasticPropertyRepository{}
