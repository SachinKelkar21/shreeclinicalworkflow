package com.shree.clinicalworkflow.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.shree.clinicalworkflow.DateTimeUtils;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.dto.PersonDTO;
import com.shree.clinicalworkflow.dto.PersonDepartments;
import com.shree.clinicalworkflow.service.PersonalDetailsService;

import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JRException;
@Slf4j
@RestController
@RequestMapping("/personDeptTag")
public class PersonDepartmentTagController {
	
	@Value("${directory}")
	private String directory;
	
	@Autowired
	private  PersonalDetailsService personalDetailsService;
		
	public PersonDepartmentTagController(PersonalDetailsService personalDetailsService) {
		this.personalDetailsService=personalDetailsService;
	}
	@PostMapping("/request")
	public boolean getAccessForPersonDepartmentTag(
			@RequestParam("tagId") final String tagId,
			@RequestParam("doorNo") final Integer doorNo,
			@RequestParam("readerNo") final Integer readerNo) {
		log.info("getAccessForPersonDepartmentTag");
		return personalDetailsService.getAccess(tagId,doorNo,readerNo);
	}
	
	@PostMapping("/requestPersonDTO")
	public PersonDTO getAccessForPersonDepartmentTag2(
			@RequestParam("tagId") final String tagId,
			@RequestParam("doorNo") final Integer doorNo,
			@RequestParam("readerNo") final Integer readerNo) {
	    log.info("getAccessForPersonDepartmentTag2");
		return personalDetailsService.getAccess2(tagId,doorNo,readerNo);
	}
	@PostMapping("/request/code")
	public String getCode(
			@RequestParam("tagId") final String tagId){
		log.info("getCode");
		return personalDetailsService.getCode(tagId);
	}
	
	@PostMapping("/backupDB")
	public boolean backupDB() {
		log.info("backup");
		return personalDetailsService.backupDB(directory);
	}
	
	@GetMapping("/admin/database/backup")
    public ModelAndView backup(Model model,Authentication authentication){
			log.info("backup");
	    	ModelAndView mav = new ModelAndView("admin/backup");
	    	PersonDepartments pd = new PersonDepartments();
	    	pd.setFromDate(new java.util.Date());
	    	model.addAttribute("pd",pd);
	    	return mav;
    }
	@ResponseBody
	@PostMapping(value = "/admin/database/backup/submit")
	public ResponseEntity<InputStreamResource> backSubmit(@ModelAttribute("pd") PersonDepartments pd1,
	   				    		Model model,
	   				    		Authentication authentication,
	   				    		@RequestParam("page") Optional<Integer> page, 
	   				  	        @RequestParam("size") Optional<Integer> size,
	   				  	        RedirectAttributes redirectAttributes) 
	   				  	        	throws JRException,IOException{
		    log.info("backSubmit");
	    	PersonalDetails personalDetails = new PersonalDetails();
	    	
	    	Boolean isBackDone = personalDetailsService.backupDB(directory);
	    	
	    	if(isBackDone.equals(Boolean.TRUE))
	    	{	
		    	FileInputStream st = null;
		    	InputStreamResource ist =null;
				try {
					
					st = new FileInputStream(directory+"/backupDB");
					HttpHeaders responseHeaders = new HttpHeaders();
					responseHeaders.setContentType(MediaType.valueOf("application/zip"));
					responseHeaders.setContentDispositionFormData("attachment", "backup-"+DateTimeUtils.getDateTime(pd1.getFromDate(), "yyyy-MM-dd-HH-mm-a")+".zip");
					responseHeaders.setContentLength(st.available());
					ist =new InputStreamResource(st);
				    return new ResponseEntity<InputStreamResource>(ist, responseHeaders, HttpStatus.OK);
				} catch (Exception e) {
					e.printStackTrace();
				} 
	    	}	
			return null;
	    }



}
