package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class BankTransaction {
	@Id
	@Column(name = "TRANS_ID", updatable = false, nullable = false) 
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int trans_id; 
	private String acct_num; 
	private String trans_type; 
	private BigDecimal amount_trans; 
	private LocalDate date;
	private BigDecimal balance; 
	public int getTrans_id() {
		return trans_id;
	}
	public void setTrans_id(int trans_id) {
		this.trans_id = trans_id;
	}
	public String getTrans_type() {
		return trans_type;
	}
	public void setTrans_type(String trans_type) {
		this.trans_type = trans_type;
	}
	public String getAcct_num() {
		return acct_num;
	}
	public void setAcct_num(String acct_num) {
		this.acct_num = acct_num;
	}
	public BigDecimal getAmount_trans() {
		return amount_trans;
	}
	public void setAmount_trans(BigDecimal amount_trans) {
		this.amount_trans = amount_trans;
	}
	public LocalDate getDate() {
		this.date = LocalDate.now();
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}



	@Override
	   public String toString() {
	       return this.date + "\t" + this.acct_num + "\t" + this.trans_type + "\t" + this.amount_trans + "\t" + this.balance;

	   }

}
