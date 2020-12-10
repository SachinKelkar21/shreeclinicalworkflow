package com.shree.clinicalworkflow.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shree.clinicalworkflow.service.PersonalDetailsService;

@RestController
@RequestMapping("/personDeptTag")
public class PersonDepartmentTagController {
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
		return personalDetailsService.getAccess(tagId,doorNo,readerNo);
	}
	@PostMapping("/request/code")
	public String getCode(
			@RequestParam("tagId") final String tagId){
		return personalDetailsService.getCode(tagId);
	}

}
