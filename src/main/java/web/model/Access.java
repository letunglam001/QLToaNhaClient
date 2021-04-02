package web.model;

import java.util.Date;
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
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Access {
	public int id;
	public Date TimeIn;
	public Date TimeOut;
	public String locationIn;
	public String locationOut;
//	public static enum Location{
//		Tang1,HamB1,HamB2
//	}
	public List<EmployeeCompany> employeeCompany;
}
