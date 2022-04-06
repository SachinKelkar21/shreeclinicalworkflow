package com.shree.clinicalworkflow.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.dto.PersonDepartments;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.fill.JRFileVirtualizer;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.util.JRSaver;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
@Slf4j
@Controller
public class JasperController {
	@Autowired
	JRFileVirtualizer fv;
	@Value("${directory}")
	private String directory;
	
	@Autowired
	DataSource datasource;
	
	JasperReport jasperReport;
	
	@GetMapping("/admin/personalDetails/CWReport1")
    public ModelAndView cwReport1PersonalDetails(Model model,Authentication authentication){
	    	ModelAndView mav = new ModelAndView("admin/personalDetails-CWReport1");
	    	PersonDepartments pd = new PersonDepartments();
	    	pd.setFromDate(new java.util.Date());
	    	pd.setToDate(new java.util.Date());
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) 
	        {
	    		pd.setReportName("CWReport1");
	        }
	    	else
	    	{
	    		pd.setReportName("CWReport2");
	    	}
	    	
	    	model.addAttribute("pd",pd);
	 
	        return mav;
    }

	@GetMapping("/admin/personalDetails/Presenty")
    public ModelAndView cwReport1PersonalDetails5(Model model,Authentication authentication){
	    	ModelAndView mav = new ModelAndView("admin/personalDetails-CWReport5");
	    	PersonDepartments pd = new PersonDepartments();
	    	pd.setFromDate(new java.util.Date());
	    	pd.setToDate(new java.util.Date());
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	pd.setReportName("Presenty");
	    	model.addAttribute("pd",pd);
	        return mav;
    }
	@GetMapping("/admin/personalDetails/DailyInOut")
    public ModelAndView cwReport1PersonalDetails6(Model model,Authentication authentication){
	    	ModelAndView mav = new ModelAndView("admin/personalDetails-CWReport6");
	    	PersonDepartments pd = new PersonDepartments();
	    	pd.setFromDate(new java.util.Date());
	    	pd.setToDate(new java.util.Date());
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	pd.setReportName("DailyInOut");
	    	model.addAttribute("pd",pd);
	    	return mav;
    }
	@GetMapping("/admin/personalDetails/DailyInOut2")
    public ModelAndView cwReport1PersonalDetails7(Model model,Authentication authentication){
	    	ModelAndView mav = new ModelAndView("admin/personalDetails-CWReport7");
	    	PersonDepartments pd = new PersonDepartments();
	    	pd.setFromDate(new java.util.Date());
	    	pd.setToDate(new java.util.Date());
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	    	pd.setReportName("DailyInOut2");
	    	model.addAttribute("pd",pd);
	    	return mav;
    }

	
	@ResponseBody
	@PostMapping(value = "/admin/personalDetails/CWReport1/submit")
	public ResponseEntity<InputStreamResource> submitCwReport1(@ModelAttribute("pd") PersonDepartments pd1,
	   				    		Model model,
	   				    		Authentication authentication,
	   				    		@RequestParam("page") Optional<Integer> page, 
	   				  	        @RequestParam("size") Optional<Integer> size,
	   				  	        RedirectAttributes redirectAttributes) 
	   				  	        	throws JRException,IOException{
		    log.info("getReport(" +pd1.getReportName()  + ")");
	    	PersonalDetails personalDetails = new PersonalDetails();
	    	personalDetails.setCode(" ");
	    	pd1.setPersonalDetails(personalDetails);
	    	
			Map<String, Object> m = new HashMap<>();
			m.put("fromDate", pd1.getFromDate());
			m.put("toDate", pd1.getToDate());
			m.put(JRParameter.REPORT_VIRTUALIZER, fv);
			
			jasperReport=report(pd1.getReportName()); 
			String name =pd1.getReportName()+".xls";
  	
	    	
	    	redirectAttributes.addFlashAttribute("pd", pd1);
	    	new RedirectView("/admin/personalDetails/success", true);
	    	return generateReport(name, m);
	       
	    }	
	@ResponseBody
	@RequestMapping(value = "/xls/{reportName}/{dept}")
	public ResponseEntity<InputStreamResource> getReport(@PathVariable("reportName") String reportName,
			@PathVariable("dept") String dept)
	
	throws JRException,IOException
	{
		log.info("getReport(" + reportName + ")");
		Map<String, Object> m = new HashMap<>();
		m.put(JRParameter.REPORT_VIRTUALIZER, fv);
		m.put("dept", dept);
		
		jasperReport=report(reportName); 
		String name =reportName+".xls";
		return generateReport(name, m);
	}
	
	JasperReport report(String reportName) throws JRException,IOException {
		log.info("Compiling Report!");
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
		log.info("Fill Report!");
		FileInputStream st = null;
		Connection cc = null;
		try {
			cc = datasource.getConnection();
			JasperPrint p = JasperFillManager.fillReport(jasperReport, params, cc);
			log.info("Filled Report!");
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
			log.info("Exported Report!");	
		}
		return null;
	}
}
