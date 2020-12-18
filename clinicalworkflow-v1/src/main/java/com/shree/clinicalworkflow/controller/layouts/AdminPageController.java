package com.shree.clinicalworkflow.controller.layouts;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.shree.clinicalworkflow.domain.Department;
import com.shree.clinicalworkflow.domain.Module;
import com.shree.clinicalworkflow.domain.PersonDepartmentTag;
import com.shree.clinicalworkflow.domain.PersonType;
import com.shree.clinicalworkflow.domain.PersonalDetails;
import com.shree.clinicalworkflow.domain.RfidReader;
import com.shree.clinicalworkflow.domain.RfidTag;
import com.shree.clinicalworkflow.domain.RfidTagStatus;
import com.shree.clinicalworkflow.domain.Role;
import com.shree.clinicalworkflow.domain.User;
import com.shree.clinicalworkflow.dto.LogData;
import com.shree.clinicalworkflow.dto.PersonDepartments;
import com.shree.clinicalworkflow.repository.DepartmentRepository;
import com.shree.clinicalworkflow.repository.ModuleRepository;
import com.shree.clinicalworkflow.repository.PersonDepartmentTagRepository;
import com.shree.clinicalworkflow.repository.PersonTypeRepository;
import com.shree.clinicalworkflow.repository.PersonalDetailsRepository;
import com.shree.clinicalworkflow.repository.RfidTagRepository;
import com.shree.clinicalworkflow.repository.RoleRepository;
import com.shree.clinicalworkflow.service.DepartmentService;
import com.shree.clinicalworkflow.service.ModuleService;
import com.shree.clinicalworkflow.service.PersonDepartmentTagService;
import com.shree.clinicalworkflow.service.PersonTypeService;
import com.shree.clinicalworkflow.service.PersonalDetailsService;
import com.shree.clinicalworkflow.service.RfidTagService;
import com.shree.clinicalworkflow.service.RoleService;
import com.shree.clinicalworkflow.service.UserService;


import lombok.extern.slf4j.Slf4j;

/**
 * @author tola on 4/9/20
 **/
@Slf4j
@Controller
public class AdminPageController {
	@Autowired
	private UserService userService;
	@Autowired
	private RoleService roleService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private PersonTypeService personTypeService;
	@Autowired
	private RfidTagService rfidTagService;
	@Autowired
	private ModuleService moduleService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private PersonTypeRepository personTypeRepository;
	@Autowired
	private PersonDepartmentTagService personDepartmentTagService;
	@Autowired
	private PersonalDetailsService personalDetailsService;
	@Autowired
	private DepartmentRepository departmentRepository;
	@Autowired
	private RfidTagRepository rfidTagRepository;
	@Autowired
	private PersonalDetailsRepository personalDetailsRepository;
	@Autowired
	private PersonDepartmentTagRepository personDepartmentTagRepository;
	@Autowired
	private ModuleRepository moduleRepository;
	
	@Value("${ws.address1}")
	private String wsAddress1;
	
	@Value("${ws.address2}")
	private String wsAddress2;
	
	@Value("${ws.address3}")
	private String wsAddress3;

	@Value("${ws.address4}")
	private String wsAddress4;

	public String getWsAddress2() {
		return wsAddress2;
	}
	public void setWsAddress2(String wsAddress2) {
		this.wsAddress2 = wsAddress2;
	}
	public String getWsAddress3() {
		return wsAddress3;
	}
	public void setWsAddress3(String wsAddress3) {
		this.wsAddress3 = wsAddress3;
	}
	public String getWsAddress4() {
		return wsAddress4;
	}
	public void setWsAddress4(String wsAddress4) {
		this.wsAddress4 = wsAddress4;
	}
	@Value("${ws.port}")
	private String wsPort;

	public void setWsAddress1(String wsAddress1) {
		this.wsAddress1 = wsAddress1;
	}
	public String getWsAddress1() {
		return wsAddress1;
	}
	public String getWsPort() {
		return wsPort;
	}

	@RequestMapping({"/admin/dashboard","/admin"})
    public ModelAndView dashboard(Model model,Authentication authentication){
		log.info("dashboard");
    	ModelAndView mav = new ModelAndView("admin/dashboard");
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")) ||  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("SUPERADMIN"))))
        {
        	access="MODULE";
        	List<LogData> logDatas=personDepartmentTagRepository.getLogCountByModuleAndLog(access);
        	for (LogData logData : logDatas) {
        		mav.addObject(logData.getLog() ,logData.getLogCount());
    		}
        
        }
        else 
        {
        	access="DEPT";
        	List<LogData> logDatas=personDepartmentTagRepository.getLogCountByDeptAndLog(access);
        	for (LogData logData : logDatas) {
        		mav.addObject(logData.getLog() ,logData.getLogCount());
    		}
        
        }

        List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
    	personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(RfidTagStatus.ISSUE,access).forEach(personalDetailss::add);
    	mav.addObject("issued",personalDetailss.size());
    	personalDetailss = new ArrayList<PersonalDetails>();
    	personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(RfidTagStatus.DEPOSITE,access).forEach(personalDetailss::add);
    	mav.addObject("deposited",personalDetailss.size());
    		
    	return mav;
    }
    @RequestMapping({"/admin/user/list","/admin/user"})
    public String listUser(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("userPage", userPage);
        
        int totalPages = userPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/user-list";
    }

    @RequestMapping("/admin/user/add")
    public ModelAndView addUser(Model model){
    	ModelAndView mav = new ModelAndView("admin/user-add");
    	User user = new User();
    	user.setEnabled(true);
    	model.addAttribute("user",user);
    	List<Role> roles = new ArrayList<Role>();
		roleRepository.findAll().forEach(roles::add);
    	mav.addObject("allRoles",roles);
        return mav;
    }
    @RequestMapping(value = "/admin/user/save", method = RequestMethod.POST)
	public String saveUser(@ModelAttribute("user") User user,
							 Model model,
				   		 	 @RequestParam("page") Optional<Integer> page, 
				 	         @RequestParam("size") Optional<Integer> size) {
    	userService.save(user);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("userPage", userPage);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/user-list";

	}
    @RequestMapping("/admin/user/edit/{id}")
    public ModelAndView editUser(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/user-edit");
    	mav.addObject("user", userService.get(id));
    	List<Role> roles = new ArrayList<Role>();
		roleRepository.findAll().forEach(roles::add);
    	mav.addObject("allRoles",roles);
		return mav;
    }
    @RequestMapping("/admin/user/delete/{id}")
    public String deleteUser(@PathVariable(name = "id") Long id,
				    		 Model model,
				    		 @RequestParam("page") Optional<Integer> page, 
				  	         @RequestParam("size") Optional<Integer> size){
    	userService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("userPage", userPage);
        int totalPages = userPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/user-list";
    }
    @RequestMapping({"/admin/role/list","/admin/role"})
    public String listRole(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Role> rolePage = roleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rolePage", rolePage);
        
        int totalPages = rolePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/role-list";
    }

    @RequestMapping("/admin/role/add")
    public ModelAndView addRole(Model model){
    	ModelAndView mav = new ModelAndView("admin/role-add");
    	Role role = new Role();
    	model.addAttribute("role",role);
        return mav;

    }
    @RequestMapping(value = "/admin/role/save", method = RequestMethod.POST)
	public String saveRole(@ModelAttribute("role") Role role,
				    		Model model,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	roleService.save(role);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Role> rolePage = roleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rolePage", rolePage);
        
        int totalPages = rolePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/role-list";
	}
    @RequestMapping("/admin/role/edit/{id}")
    public ModelAndView editRole(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/role-edit");
    	mav.addObject("role", roleService.get(id));
    	List<Role> roles = new ArrayList<Role>();
		roleRepository.findAll().forEach(roles::add);
    	mav.addObject("allRoles",roles);
		return mav;
    }
    @RequestMapping("/admin/role/delete/{id}")
    public String deleteRole(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	roleService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Role> rolePage = roleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rolePage", rolePage);
        
        int totalPages = rolePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/role-list";
    }
    @RequestMapping({"/admin/department/list","/admin/department"})
    public String listDepartment(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Department> departmentPage = departmentService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("departmentPage", departmentPage);
        
        int totalPages = departmentPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/department-list";
    }

    @RequestMapping("/admin/department/add")
    public ModelAndView addDepartment(Model model){
    	ModelAndView mav = new ModelAndView("admin/department-add");
    	Department department = new Department();
    	department.setActivationDate(new Date(System.currentTimeMillis()));
    	model.addAttribute("department",department);
    	log.info("add");
        return mav;
    }
    @RequestMapping(value = "/admin/department/save", method = RequestMethod.POST)
	public String saveDepartment(@ModelAttribute("department") Department department,
				    		Model model,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	log.info("save");
    	departmentService.save(department);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Department> departmentPage = departmentService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("departmentPage", departmentPage);
        
        int totalPages = departmentPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/department-list";
	}
    @RequestMapping("/admin/department/edit/{id}")
    public ModelAndView editDepartment(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/department-edit");
    	mav.addObject("department", departmentService.get(id));
    	List<Department> departments = new ArrayList<Department>();
    	departments=departmentService.listAll();
    	mav.addObject("allDepartments",departments);
		return mav;
    }
    @RequestMapping("/admin/department/delete/{id}")
    public String deleteDepartment(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	departmentService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Department> departmentPage = departmentService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("departmentPage", departmentPage);
        
        int totalPages = departmentPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/department-list";
    }
    @RequestMapping({"/admin/personType/list","/admin/personType"})
    public String listPersonType(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonType> personTypePage = personTypeService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personTypePage", personTypePage);
        
        int totalPages = personTypePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/personType-list";
    }

    @RequestMapping("/admin/personType/add")
    public ModelAndView addPersonType(Model model){
    	ModelAndView mav = new ModelAndView("admin/personType-add");
    	PersonType personType = new PersonType();
    	personType.setActivationDate(new Date(System.currentTimeMillis()));
    	model.addAttribute("personType",personType);
        return mav;

    }
    @RequestMapping(value = "/admin/personType/save", method = RequestMethod.POST)
	public String savePersonType(@ModelAttribute("personType") PersonType personType,
				    		Model model,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	personTypeService.save(personType);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonType> personTypePage = personTypeService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personTypePage", personTypePage);
        
        int totalPages = personTypePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/personType-list";
	}
    @RequestMapping("/admin/personType/edit/{id}")
    public ModelAndView editPersonType(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/personType-edit");
    	mav.addObject("personType", personTypeService.get(id));
    	List<PersonType> personTypes = new ArrayList<PersonType>();
    	personTypes=personTypeService.listAll();
    	mav.addObject("allPersonTypes",personTypes);
		return mav;
    }
    @RequestMapping("/admin/personType/delete/{id}")
    public String deletePersonType(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	personTypeService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonType> personTypePage = personTypeService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personTypePage", personTypePage);
        
        int totalPages = personTypePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/personType-list";
    }
    

    @RequestMapping({"/admin/rfidTag/list","/admin/rfidTag"})
    public String listRfidTag(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<RfidTag> rfidTagPage = rfidTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rfidTagPage", rfidTagPage);
        
        int totalPages = rfidTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/rfidTag-list";
    }

    @RequestMapping("/admin/rfidTag/add")
    public ModelAndView addRfidTag(Model model){
    	ModelAndView mav = new ModelAndView("admin/rfidTag-add");
    	RfidTag rfidTag = new RfidTag();
    	rfidTag.setActivationDate(new Date(System.currentTimeMillis()));
    	model.addAttribute("rfidTag",rfidTag);
        return mav;

    }
    @RequestMapping(value = "/admin/rfidTag/save", method = RequestMethod.POST)
	public String saveRfidTag(@ModelAttribute("rfidTag") RfidTag rfidTag,
				    		Model model,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	rfidTagService.save(rfidTag);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<RfidTag> rfidTagPage = rfidTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rfidTagPage", rfidTagPage);
        
        int totalPages = rfidTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/rfidTag-list";
	}
    @RequestMapping("/admin/rfidTag/edit/{id}")
    public ModelAndView editRfidTag(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/rfidTag-edit");
    	mav.addObject("rfidTag", rfidTagService.get(id));
    	List<RfidTag> rfidTags = new ArrayList<RfidTag>();
    	rfidTags=rfidTagService.listAll();
    	mav.addObject("allRfidTags",rfidTags);
		return mav;
    }
    @RequestMapping("/admin/rfidTag/delete/{id}")
    public String deleteRfidTag(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	rfidTagService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<RfidTag> rfidTagPage = rfidTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("rfidTagPage", rfidTagPage);
        
        int totalPages = rfidTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/rfidTag-list";
    }
    @RequestMapping({"/admin/module/list","/admin/module"})
    public String listModule(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Module> modulePage = moduleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("modulePage", modulePage);
        
        int totalPages = modulePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/module-list";
    }

    @RequestMapping("/admin/module/add")
    public ModelAndView addModule(Model model){
    	ModelAndView mav = new ModelAndView("admin/module-add");
    	Module module = new Module();
    	module.setActivationDate(new Date(System.currentTimeMillis()));
    	model.addAttribute("module",module);
    	log.info("add");
        return mav;
    }
    @RequestMapping(value = "/admin/module",params={"save"}, method = RequestMethod.POST)
	public String saveModule(@ModelAttribute("module") Module module,
				    		Model model,
				    		final BindingResult bindingResult,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	log.info("save");
    	if (bindingResult.hasErrors()) {
            return "admin/module-add";
        }
    	module.setRfidReaders(module.getRfidReaders());
    	
    	List<RfidReader> readers = new ArrayList<RfidReader>();
    	for(RfidReader rfidReader:module.getRfidReaders())
    	{
    		readers.add(rfidReader);
    		rfidReader.setModule(module);
    	}
    	module.setRfidReaders(readers);
    	
    	moduleService.save(module);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Module> modulePage = moduleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("modulePage", modulePage);
        
        int totalPages = modulePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/module-list";
	}
    @RequestMapping(value="/admin/module", params={"addRow"})
    public ModelAndView addRow(@ModelAttribute("module")Module module,
    		final BindingResult bindingResult,
    		Model model) {
    	log.info("addRow");
    	ModelAndView mav = new ModelAndView("admin/module-add");
    	RfidReader rfidReader = new RfidReader();
    	rfidReader.setActivationDate(new Date(System.currentTimeMillis()));
    	rfidReader.setModule(module);
    	module.getRfidReaders().add(rfidReader);
    	rfidReader.setModule(module);
    	log.info("rfidReader"+rfidReader.getActivationDate());    	
    	model.addAttribute("module",module);
    	mav.addObject("module",module);
        return mav;
    }
    
    
    @RequestMapping(value="/admin/module", params={"removeRow"})
    public ModelAndView removeRow(@ModelAttribute("module")Module module,
    		Model model,
    		final BindingResult bindingResult,
    		HttpServletRequest req) {
    	ModelAndView mav = new ModelAndView("admin/module-add");
        final Integer rowId = Integer.valueOf(req.getParameter("removeRow"));
        module.getRfidReaders().remove(rowId.intValue());
        mav.addObject("module",module);
        return mav;
    }
    
    @RequestMapping("/admin/module/edit/{id}")
    public ModelAndView editModule(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/module-edit");
    	mav.addObject("module", moduleService.get(id));
    	List<Module> modules = new ArrayList<Module>();
    	modules=moduleService.listAll();
    	mav.addObject("allModules",modules);
		return mav;
    }
    @RequestMapping("/admin/module/delete/{id}")
    public String deleteModule(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	moduleService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<Module> modulePage = moduleService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("modulePage", modulePage);
        
        int totalPages = modulePage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/module-list";
    }
    @RequestMapping({"/admin/personDepartmentTag/list","/admin/personDepartmentTag"})
    public String listPersonDepartmentTag(Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonDepartmentTag> personDepartmentTagPage = personDepartmentTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personDepartmentTagPage", personDepartmentTagPage);
        
        int totalPages = personDepartmentTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/personDepartmentTag-list";
    }

    @RequestMapping("/admin/personDepartmentTag/add")
    public ModelAndView addPersonDepartmentTag(Model model){
    	ModelAndView mav = new ModelAndView("admin/personDepartmentTag-add");
    	PersonDepartmentTag personDepartmentTag = new PersonDepartmentTag();
    	personDepartmentTag.setActivationDate(new Date(System.currentTimeMillis()));
    	model.addAttribute("personDepartmentTag",personDepartmentTag);
        return mav;

    }
    @RequestMapping(value = "/admin/personDepartmentTag/save", method = RequestMethod.POST)
	public String savePersonDepartmentTag(@ModelAttribute("personDepartmentTag") PersonDepartmentTag personDepartmentTag,
				    		Model model,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size) {
    	personDepartmentTagService.save(personDepartmentTag);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonDepartmentTag> personDepartmentTagPage = personDepartmentTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personDepartmentTagPage", personDepartmentTagPage);
        
        int totalPages = personDepartmentTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/personDepartmentTag-list";
	}
    @RequestMapping("/admin/personDepartmentTag/edit/{id}")
    public ModelAndView editPersonDepartmentTag(@PathVariable(name = "id") Long id){
    	ModelAndView mav = new ModelAndView("admin/personDepartmentTag-edit");
    	mav.addObject("personDepartmentTag", personDepartmentTagService.get(id));
    	List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
    	personDepartmentTags=personDepartmentTagService.listAll();
    	mav.addObject("allPersonDepartmentTags",personDepartmentTags);
		return mav;
    }
    @RequestMapping("/admin/personDepartmentTag/delete/{id}")
    public String deletePersonDepartmentTag(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	personDepartmentTagService.delete(id);
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Page<PersonDepartmentTag> personDepartmentTagPage = personDepartmentTagService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        model.addAttribute("personDepartmentTagPage", personDepartmentTagPage);
        
        int totalPages = personDepartmentTagPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/personDepartmentTag-list";
    }
    
    @RequestMapping({"/admin/personalDetails/list","/admin/personalDetails"})
    public String listPersonalDetails(Model model,
    		Authentication authentication,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(7);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	access="MODULE";
        }
        else
        {
        	access="DEPT";
        }
        Page<PersonalDetails> personalDetailsPage 
        = personalDetailsService.findPaginated(PageRequest.of(currentPage - 1, pageSize),RfidTagStatus.ISSUE,access,null);
        model.addAttribute("personalDetailsPage", personalDetailsPage);
        int totalPages = personalDetailsPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        
    	return "admin/personalDetails-list";
    }
    @GetMapping("/admin/personalDetails/delete")
    public ModelAndView deletePersonalDetails(Model model,Authentication authentication){
    	ModelAndView mav = new ModelAndView("admin/personalDetails-delete");
    	PersonDepartments pd = new PersonDepartments();
    	PersonalDetails personalDetails = new PersonalDetails();
    	RfidTag rfidTag = new RfidTag();
    	rfidTag.setRfidTagHexNo("-1");
    	personalDetails.setRfidTag(rfidTag);
    	pd.setPersonalDetails(personalDetails);
    	
    	model.addAttribute("pd",pd);
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	access="MODULE";
        	model.addAttribute("wsAddress",getWsAddress2());
        }
        else
        {
        	access="DEPT";
        	if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("RECEPTION1")))
        		model.addAttribute("wsAddress",getWsAddress3());
        	else
        		model.addAttribute("wsAddress",getWsAddress4());
        }
        model.addAttribute("wsPort",getWsPort());
    	List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
    	personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(RfidTagStatus.ISSUE,access).forEach(personalDetailss::add);
    	mav.addObject("allPersonalDetailssDeposite",personalDetailss);
    	
    	List<Department> departments = new ArrayList<Department>();
    	departmentRepository.getAllDepartmentExclusingAll().forEach(departments::add);
    	mav.addObject("allDepartments",departments);
    	
    	List<Module> modules = new ArrayList<Module>();
    	moduleRepository.findAll().forEach(modules::add);
    	mav.addObject("allModules",modules);

        return mav;

    }
    @GetMapping("/admin/personalDetails/add")
    public ModelAndView addPersonalDetails(Model model,Authentication authentication){
	    	ModelAndView mav = new ModelAndView("admin/personalDetails-add");
	    	PersonDepartments pd = new PersonDepartments();
	    	PersonalDetails personalDetails = new PersonalDetails();
	    	RfidTag rfidTag = new RfidTag();
	    	rfidTag.setRfidTagHexNo("-1");
	    	personalDetails.setRfidTag(rfidTag);
	    	pd.setPersonalDetails(personalDetails);
	    	
	    	model.addAttribute("pd",pd);
	    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
	        String access=null;
	        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
	        {
	        	access="MODULE";
	        	model.addAttribute("wsAddress",getWsAddress2());
	        	
	        }
	        else
	        {
	        	access="DEPT";
	        	if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("RECEPTION1")))
	        		model.addAttribute("wsAddress",getWsAddress3());
	        	else
	        		model.addAttribute("wsAddress",getWsAddress4());
	        	
	        }
	        model.addAttribute("wsPort",getWsPort());
	    	List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
	    	personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(RfidTagStatus.DEPOSITE,access).forEach(personalDetailss::add);
	    	mav.addObject("allPersonalDetailssDeposite",personalDetailss);
	    	
	    	List<Department> departments = new ArrayList<Department>();
	    	departmentRepository.getAllDepartmentExclusingAll().forEach(departments::add);
	    	mav.addObject("allDepartments",departments);
	    	
	    	List<Module> modules = new ArrayList<Module>();
	    	moduleRepository.findAll().forEach(modules::add);
	    	mav.addObject("allModules",modules);
	
	        return mav;

    }
    @PostMapping(value = "/admin/personalDetails/save")
	public RedirectView savePersonalDetails(@ModelAttribute("pd") PersonDepartments pd1,
				    		Model model,
				    		Authentication authentication,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size,
				  	        RedirectAttributes redirectAttributes) {
    	
    	if (!PersonDepartments.isValidPersonDepartments(pd1)) {
    	
            return new RedirectView("/admin/personalDetails/add", true);
            
        }
    	else
    	{	
    		log.info("****"+pd1.getPersonalDetails().getRfidTag().getRfidTagHexNo());	
    	List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
     	PersonalDetails personalDetails=personalDetailsRepository.getPersonalDetailsByRfidTagHexNo(pd1.getPersonalDetails().getRfidTag().getRfidTagHexNo());
     	if(pd1!=null && personalDetails!=null && personalDetails.getPersonDepartmentTags().size()>0)
     	{
	    	for (Iterator iterator = personalDetails.getPersonDepartmentTags().iterator(); iterator.hasNext();) {
				PersonDepartmentTag personDepartmentTag = (PersonDepartmentTag) iterator.next();
				personDepartmentTag.setDeactivationDate(new Date(System.currentTimeMillis()));
				personDepartmentTags.add(personDepartmentTag);
				//personDepartmentTagRepository.save(personDepartmentTag);
			}
	    	personDepartmentTagRepository.saveAll(personDepartmentTags);	
     	}	
    	RfidTag rfidTag = personalDetails.getRfidTag();
    	rfidTag.setStatus(RfidTagStatus.ISSUE);
    	rfidTag.setLastUpdated(new Date(System.currentTimeMillis()));
    	personalDetails.setRfidTag(rfidTag);
    	
    	//for (PersonDepartmentTag personDepartmentTag : personDepartmentTags)
    	for (Iterator iterator = personDepartmentTags.iterator(); iterator.hasNext();) 	
    	{
   		
    		personalDetails.getPersonDepartmentTags().remove((PersonDepartmentTag) iterator.next());
    	}
    	
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
    	if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	List<Module> modules=pd1.getModules();
        	for (Module module : modules) {
        			PersonDepartmentTag personDepartmentTag = new PersonDepartmentTag();
    			    personDepartmentTag.setPersonalDetails(personalDetails);
    			    personDepartmentTag.setActivationDate(new Date(System.currentTimeMillis()));
    			    personDepartmentTag.setModule(module);
    			    personDepartmentTag.setStartTime(System.currentTimeMillis());
    			    personDepartmentTag.setEndTime(System.currentTimeMillis()*1000);
    			    personDepartmentTags.add(personDepartmentTag);
        	}

        }
        else
        {
        	List<Department> departments=pd1.getDepartments();
        	for (Department dept : departments) {
        		if(!(dept!=null && dept.getName().equals("ALL")))
    			{
    				PersonDepartmentTag personDepartmentTag = new PersonDepartmentTag();
    			    personDepartmentTag.setPersonalDetails(personalDetails);
    			    personDepartmentTag.setActivationDate(new Date(System.currentTimeMillis()));
    			    personDepartmentTag.setDepartment(dept);
    			    personDepartmentTag.setStartTime(System.currentTimeMillis());
    			    personDepartmentTag.setEndTime(System.currentTimeMillis()*1000);
    			    personDepartmentTags.add(personDepartmentTag);
    			} 
        	}

        }
    	    	
    	personalDetails.setPersonDepartmentTags(personDepartmentTags);
    	personalDetailsService.save(personalDetails);
    	pd1.setPersonalDetails(personalDetails);
    	redirectAttributes.addFlashAttribute("pd", pd1);
        return new RedirectView("/admin/personalDetails/success", true);

    	}
	}
    @GetMapping(value="/admin/personalDetails/success")
	public String successPersonalDetails(HttpServletRequest request) {
	    Map<String, ?> inputFlashMap = RequestContextUtils.getInputFlashMap(request);
	    if (inputFlashMap != null) {
	        PersonDepartments personDepartments = (PersonDepartments) inputFlashMap.get("pd");
	        return "admin/personalDetails-success";
	    } else {
	        return "redirect:/admin/personalDetails-list";
	    }
	}
    @RequestMapping("/admin/personalDetails/edit/{id}")
    public ModelAndView editPersonalDetails(@PathVariable(name = "id") Long id,Authentication authentication){
    	ModelAndView mav = new ModelAndView("admin/personalDetails-edit");
    	PersonDepartments pd = new PersonDepartments();
    	pd.setPersonalDetails(personalDetailsService.get(id));
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	List<Module> modules = new ArrayList<Module>();
        	List<PersonDepartmentTag> pdtList=personalDetailsService.get(id).getPersonDepartmentTags();
        	for (PersonDepartmentTag personDepartmentTag : pdtList) {
        		modules.add(personDepartmentTag.getModule());
    		}
        	pd.setModules(modules);

        	access="MODULE";
        }
        else
        {
        	
        	List<Department> departments = new ArrayList<Department>();
        	List<PersonDepartmentTag> pdtList=personalDetailsService.get(id).getPersonDepartmentTags();
        	for (PersonDepartmentTag personDepartmentTag : pdtList) {
        		departments.add(personDepartmentTag.getDepartment());
    		}
        	pd.setDepartments(departments);

        	access="DEPT";
        }

    	mav.addObject("pd", pd);
    	
    	List<PersonalDetails> personalDetailss = new ArrayList<PersonalDetails>();
    	personalDetailsRepository.getAllPersonalDetailsByRfidTagStatusDept(RfidTagStatus.ISSUE,access).forEach(personalDetailss::add);
    	mav.addObject("allPersonalDetailssDeposite",personalDetailss);
    	   	
    	List<Department> departments = new ArrayList<Department>();
    	departmentRepository.getAllDepartmentExclusingAll().forEach(departments::add);
    	mav.addObject("allDepartments",departments);
    	
    	List<Module> modules = new ArrayList<Module>();
    	moduleRepository.findAll().forEach(modules::add);
    	mav.addObject("allModules",modules);

		return mav;
    }
    @PostMapping(value = "/admin/personalDetails/deposit")
	public RedirectView depositPersonalDetails(@ModelAttribute("pd") PersonDepartments pd1,
				    		Model model,
				    		Authentication authentication,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size,
				  	        RedirectAttributes redirectAttributes) {
    	
    	if (!PersonDepartments.isValidPersonDepartments(pd1)) {
    	
            return new RedirectView("/admin/personalDetails/delete", true);
            
        }
    	else
    	{	
    		
    	List<PersonDepartmentTag> personDepartmentTags = new ArrayList<PersonDepartmentTag>();
     	PersonalDetails personalDetails=personalDetailsRepository.getPersonalDetailsByRfidTagHexNo(pd1.getPersonalDetails().getRfidTag().getRfidTagHexNo());
     	personalDetailsService.delete(personalDetails.getId());
    	pd1.setPersonalDetails(personalDetails);
    	redirectAttributes.addFlashAttribute("pd", pd1);
        return new RedirectView("/admin/personalDetails/success", true);

    	}
	}

    @RequestMapping("/admin/personalDetails/delete/{id}")
    public String deletePersonalDetails(
    		@PathVariable(name = "id") Long id,
    		Model model,
    		Authentication authentication,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size){
    	personalDetailsService.delete(id);
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	access="MODULE";
        }
        else
        {
        	access="DEPT";
        }

    	
    	int currentPage = page.orElse(1);
        int pageSize = size.orElse(7);
        Page<PersonalDetails> personalDetailsPage = personalDetailsService.findPaginated(PageRequest.of(currentPage - 1, pageSize),RfidTagStatus.ISSUE,access,null);
        model.addAttribute("personalDetailsPage", personalDetailsPage);
        
        int totalPages = personalDetailsPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "admin/personalDetails-list";
    }
    @RequestMapping("/search")
    public String viewSearch(Model model, 
    		@Param("keyword") String keyword,
    		Authentication authentication,
    		@RequestParam("page") Optional<Integer> page, 
  	        @RequestParam("size") Optional<Integer> size) {
    	log.info("********");     
        log.info(keyword);
        
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	access="MODULE";
        }
        else
        {
        	access="DEPT";
        }

     	int currentPage = page.orElse(1);
        int pageSize = size.orElse(7);
        Page<PersonalDetails> personalDetailsPage = personalDetailsService.findPaginated(PageRequest.of(currentPage - 1, pageSize),RfidTagStatus.ISSUE,access,keyword);
        model.addAttribute("personalDetailsPage", personalDetailsPage);
       	model.addAttribute("keyword", keyword);
        int totalPages = personalDetailsPage.getTotalPages();
        
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                .boxed()
                .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
         
        return "admin/personalDetails-list";
    }

    @GetMapping("/admin/registration/add")
    public ModelAndView addRegistration(Model model,Authentication authentication){
    	ModelAndView mav = new ModelAndView("admin/registration-add");
    	PersonDepartments personDepartments = new PersonDepartments();
    	PersonalDetails personalDetails = new PersonalDetails();
    	personalDetails.setIntials(" ");
		personalDetails.setFirstName(" ");
		personalDetails.setMiddleName(" ");
		personalDetails.setLastName(" ");
		personalDetails.setAddress1(" ");
		personalDetails.setAddress2(" ");
		personalDetails.setAddress3(" ");
		personalDetails.setMobileNo(0);
		personalDetails.setAadharNo(0);
		personalDetails.setEmail(" ");
		personalDetails.setCode(" ");
		personalDetails.setPersonType(new PersonType());
    	RfidTag rfidTag = new RfidTag();
    	rfidTag.setActivationDate(new Date(System.currentTimeMillis()));
    	rfidTag.setStatus(RfidTagStatus.DEPOSITE);
    	
    	personDepartments.setPersonalDetails(personalDetails);
    	personDepartments.setRfidTag(rfidTag);
    	model.addAttribute("pd",personDepartments);
    	model.addAttribute("wsAddress",getWsAddress1());
    	model.addAttribute("wsPort",getWsPort());
    	
    	UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String access=null;
        if(userDetails.getAuthorities()!=null &&  userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")))
        {
        	access="MODULE";
        }
        else
        {
        	access="DEPT";
        }
    	
        List<PersonType> personTypes = new ArrayList<PersonType>();
        personTypeService.listAll().forEach(personTypes::add);
    	mav.addObject("allPersonTypes",personTypes);
    	
        return mav;

    }
    @RequestMapping(value="Ajax",method={ RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody String getPersonTypeCount(@RequestParam(value="personTypeId") Long personTypeId ) {
    	
        String code =  personTypeService.get(personTypeId).getCode()+""+(personTypeService.get(personTypeId).getPersonalDetailsList().size()+1);
        log.info(code);
        return code;
    }
    @PostMapping(value = "/admin/registration/save")
	public RedirectView saveRegistration(@ModelAttribute("pd") PersonDepartments pd,
				    		Model model,
				    		Authentication authentication,
				    		@RequestParam("page") Optional<Integer> page, 
				  	        @RequestParam("size") Optional<Integer> size,
				  	        RedirectAttributes redirectAttributes) {
    	PersonalDetails personalDetails= personalDetailsRepository.getPersonalDetailsByRfidTagHexNo(pd.getRfidTag().getRfidTagHexNo());
    	//log.info(""+personalDetails.getRfidTag().getRfidTagHexNo());
    	if (personalDetails!=null ) {
    		pd.getRfidTag().setRfidTagHexNo(null);
    		RedirectView redirectView = new RedirectView("/admin/registration/add", true);
    		redirectView.addStaticAttribute("pd", pd);
            return new RedirectView("/admin/registration/add", true);
            
        }
    	else
    	{	
    		
    		pd.getRfidTag().setCode(pd.getCode());
			RfidTag rfidTag=rfidTagRepository.save(pd.getRfidTag());
    		pd.getPersonalDetails().setRfidTag(rfidTag);
        	PersonType personType = personTypeRepository.getId(pd.getPersonalDetails().getPersonType().getId());
        	pd.getPersonalDetails().setPersonType(personType);
        	pd.getPersonalDetails().setCode(pd.getCode());
    		PersonalDetails personalDetails1=personalDetailsRepository.save(pd.getPersonalDetails());
        	
    		pd.setPersonalDetails(personalDetails1);
        	redirectAttributes.addFlashAttribute("pd", pd);
            return new RedirectView("/admin/personalDetails/success", true);
    	}
    }	
    
}
