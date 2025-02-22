package com.otus.docum.reportservice.adapters.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;


@Slf4j
@RequiredArgsConstructor
@Lazy
@Service
public class JasperReportService {

    private final FileService fileService;

    public JasperPrint getReportPrint(String reportFile, Map<String, Object> empParams) throws JRException{
        return JasperFillManager.fillReport
				   (        //"employees-details.jrxml"
							JasperCompileManager.compileReport(
								fileService.getFileURIFromResources(reportFile).toString()
							)
							, empParams // dynamic parameters
							, new JREmptyDataSource()
					);
    }

}
