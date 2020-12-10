package com.shree.clinicalworkflow.controller.layouts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class WebPageController {

    @RequestMapping({"/", "/home","/index"})
    public String home(){
    	log.info("WebPageController:: home");
        return "admin/dashboard";
    }

    @RequestMapping("/contact")
    public String contact(){
    	log.info("WebPageController:: contact");
        return "contact";
    }

    @RequestMapping("/about")
    public String about(){
    	log.info("WebPageController:: about");
        return "about";
    }

}
