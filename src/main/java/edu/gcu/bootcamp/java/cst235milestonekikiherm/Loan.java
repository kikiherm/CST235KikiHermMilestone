package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Loan extends Account implements Withdraw{
	private BigDecimal lateFee = new BigDecimal(25.00); 
	private BigDecimal INTERESTRATELOAN = new BigDecimal(.09); 
	private BigDecimal divisor = new BigDecimal(12);
	private boolean paymentMade = false; 
	
	static BankTransaction trans = new BankTransaction();
	Bank bank = new Bank();
	static SessionFactory factory = new Configuration().configure().buildSessionFactory();


	public BigDecimal getLateFee() {
		return lateFee;
	}

	public void setLateFee(BigDecimal lateFee) {
		this.lateFee = lateFee;
	}

	public BigDecimal getINTERESTRATELOAN() {
		return INTERESTRATELOAN;
	}

	public void setINTERESTRATELOAN(BigDecimal iNTERESTRATELOAN) {
		INTERESTRATELOAN = iNTERESTRATELOAN;
	}

	public void doWithdraw(BigDecimal amount, Account account) {
			setPaymentMade(true); 
			BigDecimal result = account.getAccount_balance().subtract(amount);
			account.setAccount_balance(result);
			trans = bank.createTransaction(account, amount, "Withdraw", result);
	
			Session session = factory.openSession();
			session.beginTransaction();
			session.save(trans);
			session.getTransaction().commit();
			
			Transaction tx =session.beginTransaction();
			CriteriaBuilder bob = session.getCriteriaBuilder();
			CriteriaUpdate<Account> criteria = bob.createCriteriaUpdate(Account.class);
			Root<Account> groot = criteria.from(Account.class);
			criteria.set(groot.get("account_balance"), account.getAccount_balance());
			criteria.where(bob.equal(groot.get("account_num"), account.getAccount_num()));
			session.createQuery(criteria).executeUpdate();
			tx.commit();
		 
		}

	public boolean getPaymentMade() {
		return paymentMade;
	}

	public void setPaymentMade(boolean paymentMade) {
		this.paymentMade = paymentMade;
	}


}
