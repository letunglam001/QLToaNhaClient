package web.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import web.model.Company;
import web.model.EmployeeCompany;
import web.model.Service;

@Slf4j
@Controller
@RequestMapping("/employeeCompany")
public class EmployeeCompanyController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping
	public String showEmployees(Model model) {
		List<EmployeeCompany> employees = Arrays
				.asList(rest.getForObject("http://127.0.0.1:8082/employeeCompany", EmployeeCompany[].class));
		model.addAttribute("employees",employees);
		return "employeeCompany"; 
	}
	@GetMapping("/create")
	public String createemployee(Model model) {
		EmployeeCompany employee=new EmployeeCompany();
		model.addAttribute("employee",employee);
		return "createEmployeeCompany"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("CMT") String CMT,@RequestParam("name") String name,@RequestParam("dateOfBirth") Date dateOfBirth,@RequestParam("phoneNumber") String phoneNumber) {
		EmployeeCompany employee=new EmployeeCompany(CMT, name, dateOfBirth, phoneNumber);
		rest.postForObject("http://localhost:8082/employeeCompany", employee, EmployeeCompany.class);
		String a="new employee company created";
		return a;
	}
	@GetMapping("/update")
	public String updateEmployee(Model model) {
		EmployeeCompany e1=new EmployeeCompany();
		model.addAttribute("e1",e1);
		return "updateEmployeeCompany"; 
	}	
	@PutMapping("/update")
	@ResponseBody
	public String process(@RequestParam("CMT") String CMT,@RequestParam("name") String name,@RequestParam("dateOfBirth") Date dateOfBirth,@RequestParam("phoneNumber") String phoneNumber) {
		EmployeeCompany employee=new EmployeeCompany(CMT, name, dateOfBirth, phoneNumber);
		rest.put("http://localhost:8082/employeeCompany/{CMT}",employee, employee.getCMT());
		String a="employee company updated";
		return a;
	}
	@GetMapping("/delete")
	public String deleteEmployeeCompany(Model model) {
		EmployeeCompany e=new EmployeeCompany();
		model.addAttribute("e",e);
		return "deleteEmployeeCompany"; 
	}
	@DeleteMapping("/delete")
	public String processService(@RequestParam("CMT") String CMT) {
		rest.delete("http://127.0.0.1:8082/employeeCompany/{CMT}",CMT);
		return "redirect:/employeeCompany";
	}
}
