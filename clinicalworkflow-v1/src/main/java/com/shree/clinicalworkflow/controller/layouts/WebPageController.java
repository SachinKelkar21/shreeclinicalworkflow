package com.shree.clinicalworkflow.controller.layouts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
public class WebPageController {

    @RequestMapping({"/", "/home","/index"})
    public String home(){
    	log.info("home");
        return "admin/dashboard";
    }

    @RequestMapping("/contact")
    public String contact(){
        return "contact";
    }

    @RequestMapping("/about")
    public String about(){
    	log.info("about");
        return "about";
    }

}
