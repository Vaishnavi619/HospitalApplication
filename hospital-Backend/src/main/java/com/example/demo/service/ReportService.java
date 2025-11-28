package com.example.demo.service;

import java.util.List;
import java.util.Map;

public interface ReportService {
    List<Map<String, Object>> getAppointmentsPerMonth();
    List<Map<String, Object>> getBillStatusCounts();
}