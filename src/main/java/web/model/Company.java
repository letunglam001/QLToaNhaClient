package web.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.AccessLevel;

@Data
//@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Company{
	public String code;
	public String name;
	public double characterCapital;
	public String field;
	public int totalEmployee;
	public String address;
	public String phoneNumber;
	public double area;
	public List<EmployeeCompany> employees; 
	public List<Service> services;
//	public Payment payment;
}
