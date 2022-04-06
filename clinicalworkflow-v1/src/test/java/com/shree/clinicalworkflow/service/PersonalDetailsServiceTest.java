package com.shree.clinicalworkflow.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


public class PersonalDetailsServiceTest {
	
	private PersonalDetailsServiceImpl personalDetailsService;
	

	
	
	
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        personalDetailsService = new PersonalDetailsServiceImpl();
    }
	/*
	@Test
	public void createPersonalDetailsTest() {
		
		PersonalDetails personalDetails = personalDetailsService.createPersonalDetails("MR","SACHIN","SHRIDHAR","KELKAR","G304","NALASOPARA","MUMBAI",new Integer("123456"),new Integer("123456"),"sachin@email.com");
		
		assertThat(personalDetails.getFirstName()).isEqualTo("SACHIN");
		
	}*/

}
