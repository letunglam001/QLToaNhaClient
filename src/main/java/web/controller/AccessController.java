package web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;

import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;

import com.sun.research.ws.wadl.Response;

import lombok.extern.slf4j.Slf4j;
import web.model.Access;
import web.model.Company;
import web.model.EmployeeCompany;
import web.model.Payment;
import web.model.Service;
import web.model.Service.Type;


@Slf4j
@Controller
@RequestMapping("/access")
public class AccessController {
	private RestTemplate rest = new RestTemplate();

	@GetMapping
	public String showAccesss(Model model) {
		List<Access> accesss = Arrays
				.asList(rest.getForObject("http://localhost:8082/access", Access[].class));
		model.addAttribute("accesss",accesss);
		return "access"; 
	}
	@GetMapping("/create")
	public String createAccess(Model model) {
		Access access=new Access();
		model.addAttribute("access",access);
		EmployeeCompany employeeCompany=new EmployeeCompany();
		model.addAttribute("employeeCompany",employeeCompany);
		return "createAccess"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("id") int id,@RequestParam("TimeIn") Date TimeIn,@RequestParam("TimeOut") Date TimeOut,@RequestParam("locationIn") String locationIn,@RequestParam("locationOut") String locationOut,@RequestParam("name") String name) {
		List<Access> accesss=new ArrayList<>();
		System.out.println(name);
		List<EmployeeCompany> employeeCompanys = Arrays
				.asList(rest.getForObject("http://localhost:8082/employeeCompany", EmployeeCompany[].class));
		EmployeeCompany employeeCompany= null;
		for(int i=0;i<employeeCompanys.size();i++) {
			if(name.equals(employeeCompanys.get(i).getName())) employeeCompany=employeeCompanys.get(i);
		}
		List<EmployeeCompany> c=new ArrayList<>();
		c.add(employeeCompany);
		Access access=new Access(); 
		access.setId(id);
		access.setLocationIn(locationIn);
		access.setLocationOut(locationOut);
		access.setTimeIn(TimeIn);
		access.setTimeOut(TimeOut);
		access.setEmployeeCompany(c);
		System.out.println(access);
		rest.postForObject("http://localhost:8082/access", access, Access.class);
//		rest.postForObject("http://localhost:8082/access", access, Access.class);
		String a="new access created";
		return a;
	}
}