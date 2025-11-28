package com.example.demo.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.PatientIndex;
import com.example.demo.repository.PatientSearchRepository;
import com.example.demo.service.PatientSearchService;

@Service
public class PatientSearchServiceImpl implements PatientSearchService {

    @Autowired
    private PatientSearchRepository searchRepository;

    @Override
    public List<PatientIndex> searchByName(String name) {
    	 List<PatientIndex> results = searchRepository.fuzzySearchByFullName(name);

        
         if (results.isEmpty()) {
             results = searchRepository.findByFullNameContainingIgnoreCase(name);
         }
         return results;
    }

    @Override
    public List<PatientIndex> searchByPhone(String phone) {
        return searchRepository.findByPhoneContaining(phone);
    }

    @Override
    public List<PatientIndex> searchByAddress(String address) {
        return searchRepository.findByAddressContainingIgnoreCase(address);
    }
}

