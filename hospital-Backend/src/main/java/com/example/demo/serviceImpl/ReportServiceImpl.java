package com.example.demo.serviceImpl;

import com.example.demo.repository.AppointmentRepository;
import com.example.demo.repository.BillRepository;
import com.example.demo.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BillRepository billRepository;

    @Override
    public List<Map<String, Object>> getAppointmentsPerMonth() {
        List<Object[]> results = appointmentRepository.countAppointmentsPerMonth();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("month", row[0]);
            map.put("count", row[1]);
            response.add(map);
        }

        return response;
    }

    @Override
    public List<Map<String, Object>> getBillStatusCounts() {
        List<Object[]> results = billRepository.countByPaymentStatus();
        List<Map<String, Object>> response = new ArrayList<>();

        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("status", row[0]);
            map.put("count", row[1]);
            response.add(map);
        }

        return response;
    }
}
