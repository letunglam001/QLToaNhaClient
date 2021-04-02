package web.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class EmployeeBuilding {
	public int id;
	@Size (min=2,message="Ten phai co it nhat 2 ky tu")
	public String name;
	@NotNull(message="Phải có ngày sinh")
	public Date dateOfBirth;
	@NotNull(message="Phai nhap dia chi")
	public String address;
	@Size (min=10,max=11,message="So dien thoai phai co it nhat 10 so")
	public String phoneNumber;
	@NotNull(message="Phai nhap thu hang")
	public int ranking;
	@NotNull(message="Phai nhap vi tri")
	public String position;
	@Size(min=1,message="Phai chon it nhat 1 dich vu")
	public List<Service> services;
	public double salary;
}	
