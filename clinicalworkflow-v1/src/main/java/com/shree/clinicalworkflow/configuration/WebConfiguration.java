package com.shree.clinicalworkflow.configuration;

import org.h2.server.web.WebServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Configuration
public class WebConfiguration {

	ServletRegistrationBean h2servletRegistration() {
		log.info("h2servletRegistration");
		ServletRegistrationBean registrationBean
			= new ServletRegistrationBean(new WebServlet());
		registrationBean.addUrlMappings("/h2-console/*");
		
		return registrationBean;
	}
}
