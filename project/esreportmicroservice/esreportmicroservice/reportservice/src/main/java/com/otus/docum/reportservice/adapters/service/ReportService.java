package com.otus.docum.reportservice.adapters.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.otus.docum.reportservice.entities.domain.EmployeeDocument;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@Slf4j
@RequiredArgsConstructor
@Lazy
@Service
public class ReportService {

    private final StoreService serviceMongo;
    private final JasperReportService jasperReportService;

    public JasperPrint getEmployeeRecordReport() throws JRException {

        List<EmployeeDocument> empLst = serviceMongo.getEmployeeList();
		Map<String, Object> empParams = new HashMap<String, Object>();
		empParams.put("CompanyName", "Otus Homework report");
		empParams.put("employeeData", new JRBeanCollectionDataSource(empLst));        

        return jasperReportService.getReportPrint("employees-details.jrxml", empParams);
    }

}
