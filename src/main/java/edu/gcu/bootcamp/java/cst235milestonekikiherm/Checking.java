package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Checking extends Account implements Withdraw, Deposit{

	private BigDecimal overDraftFee = new BigDecimal(45.00); 
	
	static SessionFactory factory = new Configuration().configure().buildSessionFactory();
	static BankTransaction trans = new BankTransaction();
	Bank bank = new Bank();

	public BigDecimal getOverDraftFee() {
		return overDraftFee;
	}


	public void doDeposit(BigDecimal amount,Account account) {
		BigDecimal result = amount.add(account.getAccount_balance());
		account.setAccount_balance(result);
		trans = bank.createTransaction(account, amount, "Deposit", result);
		
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

	public void doWithdraw(BigDecimal amount, Account account) {
		BigDecimal newBalance = account.getAccount_balance().subtract(amount);
		if(newBalance.compareTo(BigDecimal.ZERO) <0 ) {
			newBalance = (account.getAccount_balance().subtract(amount)).subtract(this.getOverDraftFee());
			account.setAccount_balance(newBalance);
			trans = bank.createTransaction(account, amount, "Withdraw", newBalance);
			
			
		}
		else {
			BigDecimal result = account.getAccount_balance().subtract(amount);
			account.setAccount_balance(result);
			trans = bank.createTransaction(account, amount, "Withdraw", result);

		}
	
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

}
