package com.shree.clinicalworkflow.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.shree.clinicalworkflow.service.UserDetailsServiceImpl;

import lombok.extern.slf4j.Slf4j;





@Slf4j
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	public UserDetailsService userDetailsService() {
		log.info("userDetailsService");
		return new UserDetailsServiceImpl();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		log.info("passwordEncoder");
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		log.info("authenticationProvider");
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());
		
		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		log.info("configure:Auth");
		auth.authenticationProvider(authenticationProvider());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		log.info("configure:HttpSecu");
		http.authorizeRequests()
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/personDeptTag/**").permitAll()
			.antMatchers("/xls/**").permitAll()
			.antMatchers("/").hasAnyAuthority("USER","ADMIN","SUPERADMIN","RECEPTION1","RECEPTION2","ADMISSION")
			.antMatchers("/new").hasAnyAuthority("ADMIN", "CREATOR")
			.antMatchers("/edit/**").hasAnyAuthority("ADMIN", "EDITOR")
			.antMatchers("/delete/**").hasAuthority("ADMIN")
			.anyRequest().authenticated()
			.and()
			.formLogin().permitAll()
			.and()
			.logout().permitAll()
			.and()
			.exceptionHandling().accessDeniedPage("/403")
			;

		http.csrf().disable();
        http.headers().frameOptions().disable();
	}
	
}
