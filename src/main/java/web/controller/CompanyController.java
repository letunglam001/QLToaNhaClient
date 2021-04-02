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
import web.model.Company;
import web.model.EmployeeCompany;
import web.model.Service;
import web.model.Service.Type;


@Slf4j
@Controller
@RequestMapping("/company")
public class CompanyController {
	private RestTemplate rest = new RestTemplate();

	@ModelAttribute
	public void addService(Model model) {
		List<Service> services = Arrays
				.asList(rest.getForObject("http://localhost:8082/service", Service[].class));
		Type[] types=Service.Type.values();
		for(Type type:types) {
			model.addAttribute(type.toString().toLowerCase(),filterByType(services,type));
		}
	}
	
	@GetMapping
	public String showCompanys(Model model) {
		List<Company> companys = Arrays
				.asList(rest.getForObject("http://localhost:8082/company", Company[].class));
		model.addAttribute("companys",companys);
		return "company"; 
	}
	@GetMapping("/create")
	public String createCompany(Model model) {
		Company company=new Company();
		model.addAttribute("company",company);
		List<EmployeeCompany> employees = Arrays
				.asList(rest.getForObject("http://localhost:8082/employeeCompany", EmployeeCompany[].class));
		model.addAttribute("employees",employees);
		return "createCompany"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("code") String code,@RequestParam("name") String name,@RequestParam("characterCapital") double characterCapital,@RequestParam("field") String field,@RequestParam("address") String address,@RequestParam("phoneNumber") String phoneNumber,@RequestParam("area") double area,@RequestParam("services") String ids,@RequestParam("employee") String CMTs) {
		List<Service> services = new ArrayList<>();
		List<EmployeeCompany> employeeCompanys= new ArrayList<>();
		Service serive1=rest.getForObject("http://localhost:8082/service/get/VeSing",Service.class);
		services.add(serive1);
		Service serive2=rest.getForObject("http://localhost:8082/service/get/BaoVe",Service.class);
		services.add(serive2);
		for(String id:ids.split(",")) {
			Service service = rest.getForObject("http://localhost:8082/service/{id}",Service.class,id);
			services.add(service);
		}
		for(String CMT: CMTs.split(",")) {
			EmployeeCompany employeeCompany = rest.getForObject("http://localhost:8082/employeeCompany/{CMT}",EmployeeCompany.class,CMT);
			employeeCompanys.add(employeeCompany);
		}
		int totalEmployee=employeeCompanys.size();
		Company company=new Company(); 
		company.setCode(code);
		company.setName(name);
		company.setAddress(address);
		company.setArea(area);
		company.setCharacterCapital(characterCapital);
		company.setField(field);
		company.setPhoneNumber(phoneNumber);
		company.setTotalEmployee(totalEmployee);
		company.setEmployees(employeeCompanys);
		company.setServices(services);
//		System.out.println(services);
//		System.out.println(company);
		rest.postForObject("http://localhost:8082/company", company, Company.class);
		String a="new company created";
		return a;
	}
	@GetMapping("/delete")
	public String deleteCompany(Model model) {
		Company c=new Company();
		model.addAttribute("c",c);
		return "deleteCompany"; 
	}
	@DeleteMapping("/delete")
	public String processCompany(@RequestParam("code") String code) {
		rest.delete("http://127.0.0.1:8082/company/{code}",code);
		return "redirect:/company";
	}
	@GetMapping("/update")
	public String updateCompany(Model model) {
		Company c1=new Company();
		model.addAttribute("c1",c1);
		List<EmployeeCompany> employees = Arrays
				.asList(rest.getForObject("http://localhost:8082/employeeCompany", EmployeeCompany[].class));
		model.addAttribute("employees",employees);
		return "updateCompany"; 
	}	
	@PutMapping("/update")
	@ResponseBody
	public String process(@RequestParam("code") String code,@RequestParam("name") String name,@RequestParam("characterCapital") double characterCapital,@RequestParam("field") String field,@RequestParam("address") String address,@RequestParam("phoneNumber") String phoneNumber,@RequestParam("area") double area,@RequestParam("services") String ids,@RequestParam("employee") String CMTs) {
		List<Service> services = new ArrayList<>();
		List<EmployeeCompany> employeeCompanys= new ArrayList<>();
		Service serive1=rest.getForObject("http://localhost:8082/service/get/VeSing",Service.class);
		services.add(serive1);
		Service serive2=rest.getForObject("http://localhost:8082/service/get/BaoVe",Service.class);
		services.add(serive2);
		for(String id:ids.split(",")) {
			Service service = rest.getForObject("http://localhost:8082/service/{id}",Service.class,id);
			services.add(service);
		}
		for(String CMT: CMTs.split(",")) {
			EmployeeCompany employeeCompany = rest.getForObject("http://localhost:8082/employeeCompany/{CMT}",EmployeeCompany.class,CMT);
			employeeCompanys.add(employeeCompany);
		}
		int totalEmployee=employeeCompanys.size();
		Company company=new Company();
		company.setServices(services);
		company.setEmployees(employeeCompanys);
		company.setCode(code);
		company.setName(name);
		company.setAddress(address);
		company.setArea(area);
		company.setCharacterCapital(characterCapital);
		company.setField(field);
		company.setPhoneNumber(phoneNumber);
		company.setTotalEmployee(totalEmployee);
		rest.put("http://localhost:8082/company/{code}",company, company.getCode());
		String a="company updated";
		return a;
	}
	public List<Service> filterByType(List<Service> services,Type type){
		List<Service> serlist=new ArrayList<Service>();
		for (Service service : services) {
			if(service.getType().equals(type)) serlist.add(service);
		}
		return serlist;
		
	}
}