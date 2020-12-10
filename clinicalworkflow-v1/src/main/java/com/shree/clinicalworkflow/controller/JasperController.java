package com.shree.clinicalworkflow.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.fill.JRSwapFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.engine.util.JRSwapFile;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

@RestController
public class JasperController {

	protected Logger logger = Logger.getLogger(JasperController.class.getName());
	public static int count = 0;
	
	/*@Autowired
	JRFileVirtualizer fv;
	@Value("${directory}")
	private String directory;
	*/
	
	@Autowired
	DataSource datasource;
	
	JasperReport jasperReport;
	
	@ResponseBody
	@RequestMapping(value = "/xls/{reportName}")
	public ResponseEntity<InputStreamResource> getReport(@PathVariable("reportName") String reportName)
	throws JRException,IOException
	{
		logger.info("getReport(" + reportName + ")");
		Map<String, Object> m = new HashMap<>();
		//m.put("age", age);	
		
		jasperReport=report(reportName); 
		String name =reportName+".xls";
		
		
		return generateReport(name, m);
	}
	
	JasperReport report(String reportName) throws JRException,IOException {
		logger.info("Compiling Report!");
		JasperReport jr = null;
		File f = new File("BOOT-INF/classes/reports/"+reportName+".jasper");
		if (f.exists()) {
		jr = (JasperReport) JRLoader.loadObject(f);
		} else {
			jr = JasperCompileManager.compileReport(getClass().getResourceAsStream("/reports/"+reportName+".jrxml"));
			JRSaver.saveObject(jr, reportName+".jasper");
		}
		return jr;
	}
	
	
	private ResponseEntity<InputStreamResource> generateReport(String name, Map<String, Object> params) {
		logger.info(" fill Report!");
		FileInputStream st = null;
		Connection cc = null;
		try {
			cc = datasource.getConnection();
			JasperPrint p = JasperFillManager.fillReport(jasperReport, params, cc);
			//JRPdfExporter exporter = new JRPdfExporter();
			JRXlsExporter exporter = new JRXlsExporter();
			SimpleOutputStreamExporterOutput c = new SimpleOutputStreamExporterOutput(name);
			exporter.setExporterInput(new SimpleExporterInput(p));
			exporter.setExporterOutput(c);
			exporter.exportReport();
			
			st = new FileInputStream(name);
			HttpHeaders responseHeaders = new HttpHeaders();
			responseHeaders.setContentType(MediaType.valueOf("application/vnd.ms-excel"));
			responseHeaders.setContentDispositionFormData("attachment", name);
			responseHeaders.setContentLength(st.available());
		    return new ResponseEntity<InputStreamResource>(new InputStreamResource(st), responseHeaders, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//fv.cleanup();
			if (cc != null)
				try {
					cc.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return null;
	}
}
