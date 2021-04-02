package web.model;

import java.util.List;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@NoArgsConstructor(access = AccessLevel.PUBLIC, force = true)
public class Payment {
	public int id;
	public double price;
	public List<Company> companys;
}
