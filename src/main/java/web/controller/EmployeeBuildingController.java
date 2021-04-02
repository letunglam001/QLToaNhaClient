package web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;
import web.model.Company;
import web.model.EmployeeBuilding;
import web.model.EmployeeCompany;
import web.model.Service;
import web.model.Service.Type;

@Slf4j
@Controller
@RequestMapping("/employeeBuilding")
public class EmployeeBuildingController {
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
	public String showEmployees(Model model) {
		List<EmployeeBuilding> employees = Arrays
				.asList(rest.getForObject("http://localhost:8082/employeeBuilding", EmployeeBuilding[].class));
		model.addAttribute("employees",employees);
		return "employeeBuilding"; 
	}
	@GetMapping("/create")
	public String createemployee(Model model) {
		EmployeeBuilding employee=new EmployeeBuilding();
		model.addAttribute("employee",employee);
		List<Service> services = Arrays
				.asList(rest.getForObject("http://localhost:8082/service", Service[].class));
		model.addAttribute("services",services);
		return "createEmployeeBuilding"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("name") String name,@RequestParam("dateOfBirth") Date dateOfBirth,@RequestParam("address") String address,@RequestParam("phoneNumber") String phoneNumber,@RequestParam("ranking") int ranking,@RequestParam("position") String position,@RequestParam("services") String ids) {
		EmployeeBuilding employee=new EmployeeBuilding();
		List<Service> services = new ArrayList<>();
		for(String id1:ids.split(",")) {
			Service service = rest.getForObject("http://localhost:8082/service/{id}",Service.class,id1);
			services.add(service);
		}
		double salary=0;
		employee.setServices(services);
		for(int i = 0 ;i<employee.getServices().size();i++) {
			if(employee.getServices().get(i).getType().toString().equals("VeSinh")) {
				salary = salary + 3000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("AnUong")) {
				salary = salary + 1000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("TrongGiuXe")) {
				salary = salary + 400;
			}
			else if(employee.getServices().get(i).getType().toString().equals("BaoVe")) {
				salary = salary + 5000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("BaoTriThietBi")) {
				salary = salary + 8000;
			}
		}
		
		employee.setSalary(salary);
//		employee.setId(id);
		employee.setAddress(address);
		employee.setDateOfBirth(dateOfBirth);
		employee.setName(name);
		employee.setPhoneNumber(phoneNumber);
		employee.setPosition(position);
		employee.setRanking(ranking);
		
		rest.postForObject("http://localhost:8082/employeeBuilding", employee, EmployeeBuilding.class);
		String a="New employee building created";
		return a;
	}
	@GetMapping("/update")
	public String updateEmployee(Model model) {
		EmployeeBuilding e1=new EmployeeBuilding();
		model.addAttribute("e1",e1);
		return "updateEmployeeBuilding"; 
	}	
	@PutMapping("/update")
	@ResponseBody
	public String process(@RequestParam("id") int id,@RequestParam("name") String name,@RequestParam("dateOfBirth") Date dateOfBirth,@RequestParam("address") String address,@RequestParam("phoneNumber") String phoneNumber,@RequestParam("ranking") int ranking,@RequestParam("position") String position,@RequestParam("services") String ids) {
		EmployeeBuilding employee=new EmployeeBuilding();
		List<Service> services = new ArrayList<>();
		for(String id1:ids.split(",")) {
			Service service = rest.getForObject("http://localhost:8082/service/{id}",Service.class,id1);
			services.add(service);
		}
		employee.setServices(services);
		double salary=0;
		for(int i = 0 ;i<employee.getServices().size();i++) {
			if(employee.getServices().get(i).getType().toString().equals("VeSinh")) {
				salary = salary + 3000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("AnUong")) {
				salary = salary + 1000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("TrongGiuXe")) {
				salary = salary + 400;
			}
			else if(employee.getServices().get(i).getType().toString().equals("BaoVe")) {
				salary = salary + 5000;
			}
			else if(employee.getServices().get(i).getType().toString().equals("BaoTriThietBi")) {
				salary = salary + 8000;
			}
		}
		employee.setSalary(salary);
		employee.setId(id);
		employee.setAddress(address);
		employee.setDateOfBirth(dateOfBirth);
		employee.setName(name);
		employee.setPhoneNumber(phoneNumber);
		employee.setPosition(position);
		employee.setRanking(ranking);
		rest.put("http://localhost:8082/employeeBuilding/{id}",employee, employee.getId());
		String a="Employee building updated";
		return a;
	}
	@GetMapping("/delete")
	public String deleteEmployeeBuilding(Model model) {
		EmployeeBuilding e=new EmployeeBuilding();
		model.addAttribute("e",e);
		return "deleteEmployeeBuilding"; 
	}
	@DeleteMapping("/delete")
	public String processService(@RequestParam("id") int id) {
		rest.delete("http://localhost:8082/employeeBuilding/{id}",id);
		return "redirect:/employeeBuilding";
	}
	public List<Service> filterByType(List<Service> services,Type type){
		List<Service> serlist=new ArrayList<Service>();
		for (Service service : services) {
			if(service.getType().equals(type)) serlist.add(service);
		}
		return serlist;
		
	}
}
