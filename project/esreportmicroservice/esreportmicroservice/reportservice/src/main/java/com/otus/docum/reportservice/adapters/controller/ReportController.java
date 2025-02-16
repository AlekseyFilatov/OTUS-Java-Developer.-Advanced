package com.otus.docum.reportservice.adapters.controller;



import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import net.sf.jasperreports.engine.JasperExportManager;

import net.sf.jasperreports.engine.JasperPrint;

import com.otus.docum.reportservice.entities.domain.EmployeeDocument;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.otus.docum.reportservice.application.exception.JasperReportRuntimeException;

import com.otus.docum.reportservice.adapters.service.ReportService;


@Slf4j
@RequiredArgsConstructor
@RestController
public class ReportController {

    private final ReportService reportService;

	@Operation(summary = "Create report")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "Report created",
				content = { @Content(mediaType = "APPLICATION_PDF",
						schema = @Schema(implementation = EmployeeDocument.class)) }),
		@ApiResponse(responseCode = "400", description = "Invalid id supplied",
				content = @Content),
		@ApiResponse(responseCode = "404", description = "Report not found",
				content = @Content) })
	@GetMapping(path = "/employee/records/report")
	ResponseEntity<byte[]> getEmployeeRecordReport() {
    try {
			log.info("(ReportController)Report begin: ");
			JasperPrint empReport = reportService.getEmployeeRecordReport();
			log.info("(ReportController)Report end.");
			HttpHeaders headers = new HttpHeaders();
			//set the PDF format
			headers.setContentType(MediaType.APPLICATION_PDF);
			headers.setContentDispositionFormData("filename", "employees-details.pdf");
			//create the report in PDF format
			return new ResponseEntity<byte[]>
					(JasperExportManager.exportReportToPdf(empReport), headers, HttpStatus.OK);
			
		} catch(Exception e) {
			log.error("JasperReportRuntimeException: " + e.getMessage());
			throw new JasperReportRuntimeException("employees-details.jrxml", e);
		}
    }

}

