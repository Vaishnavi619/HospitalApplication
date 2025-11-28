package com.example.demo.service;

import java.util.List;



import com.example.demo.entity.PatientIndex;


public interface PatientSearchService {
    List<PatientIndex> searchByName(String name);
    List<PatientIndex> searchByPhone(String phone);
    List<PatientIndex> searchByAddress(String address);
}

