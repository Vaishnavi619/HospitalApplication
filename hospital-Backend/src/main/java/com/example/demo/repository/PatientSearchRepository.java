package com.example.demo.repository;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.PatientIndex;

import java.util.List;

@Repository
public interface PatientSearchRepository extends ElasticsearchRepository<PatientIndex, String> {

    List<PatientIndex> findByFullNameContainingIgnoreCase(String name);

    List<PatientIndex> findByPhoneContaining(String phone);

    List<PatientIndex> findByAddressContainingIgnoreCase(String address);
    
    @Query("{\"match\": {\"fullName\": {\"query\": \"?0\", \"fuzziness\": \"AUTO\"}}}")
    List<PatientIndex> fuzzySearchByFullName(String name);
}

