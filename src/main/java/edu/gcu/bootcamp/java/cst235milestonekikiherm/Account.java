package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;


import javax.persistence.Entity;
import javax.persistence.Id;





@Entity 
public class Account {
	@Id
	private String account_num; 
	private String account_type;
	private BigDecimal account_balance;
	private int cust_id; 
	
//	private Customer customer; 
//	
//	public Account() {	
//	}
//	public Account(String account_num) {
//		this.account_num= account_num; 
//	}
	
	public String getAccount_num() {
		return account_num;
	}
	public void setAccount_num(String account_num) {
		this.account_num = account_num;
	}
	public String getAccount_type() {
		return account_type;
	}
	public void setAccount_type(String account_type) {
		this.account_type = account_type;
	}
	public BigDecimal getAccount_balance() {
		return account_balance;
	}
	public void setAccount_balance(BigDecimal account_balance) {
		this.account_balance = account_balance;
	} 
	
//	@OneToOne(cascade = CascadeType.ALL)
//	@JoinColumn (name = "CUST_ID")
//	public Customer getCustomer() {
//		return customer; 
//	}
//	public void setCustomer(Customer customer) {
//		this.customer = customer; 
//	}
	public int getCust_id() {
		return cust_id;
	}
	public void setCust_id(int cust_id) {
		this.cust_id = cust_id;
	}
	

}
