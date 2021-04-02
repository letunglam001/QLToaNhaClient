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
import web.model.Payment;
import web.model.Service;
import web.model.Service.Type;


@Slf4j
@Controller
@RequestMapping("/payment")
public class PaymentController {
	private RestTemplate rest = new RestTemplate();

	@GetMapping
	public String showPayments(Model model) {
		List<Payment> payments = Arrays
				.asList(rest.getForObject("http://localhost:8082/payment", Payment[].class));
		model.addAttribute("payments",payments);
		return "payment"; 
	}
	@GetMapping("/create")
	public String createPayment(Model model) {
		Payment payment=new Payment();
		model.addAttribute("payment",payment);
		Company company=new Company();
		model.addAttribute("company",company);
		return "createPayment"; 
	}
	@PostMapping("/create")
	@ResponseBody
	public String processDesign(@RequestParam("id") int id,@RequestParam("name") String name) {
		List<Payment> payments=new ArrayList<>();
		System.out.println(name);
		List<Company> companys = Arrays
				.asList(rest.getForObject("http://localhost:8082/company", Company[].class));
		Company company= null;
		for(int i=0;i<companys.size();i++) {
			if(name.equals(companys.get(i).getName())) company=companys.get(i);
		}
		List<Company> c=new ArrayList<>();
		c.add(company);
		Payment payment=new Payment(); 
		payment.setId(id);
		payment.setCompanys(c);
		double price = 0;
		for(int j=0;j<company.getServices().size();j++) {
			if(company.getTotalEmployee()<10&&company.getArea()<100) {
				price+=company.getServices().get(j).getPrice();
			}
			else {
				if(company.getTotalEmployee()>10&&company.getArea()<=100) {
					int number=company.getTotalEmployee();
					int t=(number-10)/5;
					price+=company.getServices().get(j).getPrice()*(1+t*0.05);
				}
				else if(company.getArea()>100&&company.getTotalEmployee()<=10) {
					double number=company.getArea();
					int t=(int)(number-100)/10;
					price+=company.getServices().get(j).getPrice()*(1+t*0.05);
				}
				else if(company.getTotalEmployee()>10&&company.getArea()>100) {
					int number=company.getTotalEmployee();
					double number2=company.getArea();
					int t=(number-10)/5;
					int t1=(int)(number2-100)/10;
					price+=company.getServices().get(j).getPrice()*(1+t*0.05+t1*0.05);
				}
			}
		}
		payment.setPrice(price);
		rest.postForObject("http://localhost:8082/payment", payment, Payment.class);
		String a="new payment created";
		return a;
	}
}