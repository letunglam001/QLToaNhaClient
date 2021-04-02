package web.controller;

import java.util.Arrays;
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
import web.model.Service;
import web.model.Service.Type;

@Slf4j
@Controller
@RequestMapping("/service")
public class ServiceController {
	private RestTemplate rest = new RestTemplate();
	@GetMapping
	public String showServices(Model model) {
		List<Service> services = Arrays
				.asList(rest.getForObject("http://127.0.0.1:8082/service", Service[].class));
		model.addAttribute("services",services);
		return "service"; 
	}
	@GetMapping("/create")
	public String createService(Model model) {
		Service serivce=new Service();
		model.addAttribute("service",serivce);
		return "createService"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("id") String id,@RequestParam("name") String name,@RequestParam("price") double price,@RequestParam("type") Type type) {
		Service service=new Service(id, name, type, price);
		rest.postForObject("http://localhost:8082/service", service, Service.class);
		String a="new service created";
		return a;
	}
	@GetMapping("/update")
	public String updateService(Model model) {
		Service s1=new Service();
		model.addAttribute("s1",s1);
		return "updateService"; 
	}	
	@PutMapping("/update")
	@ResponseBody
	public String process(@RequestParam("id") String id,@RequestParam("name") String name,@RequestParam("price") double price,@RequestParam("type") Type type) {
		Service service=new Service(id, name, type, price);
		rest.put("http://localhost:8082/service/{id}",service, service.getId());
		String a="service updated";
		return a;
	}
	@GetMapping("/delete")
	public String deleteService(Model model) {
		Service s=new Service();
		model.addAttribute("s",s);
		return "deleteService"; 
	}
	@DeleteMapping("/delete")
	public String processService(@RequestParam("id") String id) {
		rest.delete("http://127.0.0.1:8082/service/{id}",id);
		return "redirect:/service";
	}
}
