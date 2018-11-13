package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class Menu {
	static SessionFactory factory = new Configuration().configure().buildSessionFactory();
	static private BigDecimal bd;
	static Scanner sc = new Scanner(System.in);
	static Bank b = new Bank();
	static Menu menu = new Menu();

public void customerMenu(Customer customer) {
	Session session = factory.openSession();
	session.beginTransaction();
	session.getTransaction().commit();
	Transaction tx =session.beginTransaction();
	CriteriaBuilder bob = session.getCriteriaBuilder();
	CriteriaUpdate<Account> criteria = bob.createCriteriaUpdate(Account.class);
	Root<Account> groot = criteria.from(Account.class);
	criteria.set(groot.get("login"), customer.getLogin());
	criteria.where(bob.equal(groot.get("cust_id"), customer.getId()));
	session.createQuery(criteria).executeUpdate();
	tx.commit();
		String option;
		do {
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("            MAIN MENU");
			System.out.println("              "+ "GCU BANK");
			System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
			System.out.println("Pick an option: ");
			System.out.println("-----------------------");
			System.out.println(" 1: : Deposit to Checking");
			System.out.println(" 2: : Deposit to Savings");
			System.out.println(" 3: : Write a Check");
			System.out.println(" 4: : Withdraw from Savings");			
			System.out.println(" 5: : Get balance");
			System.out.println(" 6: : Make Loan Payment");
			System.out.println(" 7: : Close month");
			System.out.println("------------------------");
			System.out.println(" 9: : Logout");
			option = sc.nextLine();
			menu.actionMenu(Integer.parseInt(option), customer);
		} while (Integer.parseInt(option) != 9);
	}
private void actionMenu(int option, Customer customer) {
    List<Account> accountList = null;
    Checking checking = new Checking();
    Saving saving = new Saving();
    Loan loan = new Loan();
    Account acct;
    int id = customer.getId();
    Session session = factory.openSession();
    CriteriaBuilder cb = session.getCriteriaBuilder();
    CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
    Root<Account> groot = criteria.from(Account.class);
    criteria.select(groot);
    criteria.where(cb.equal(groot.get("cust_id"),id));
    accountList = session.createQuery(criteria).getResultList();
    
            switch(option) {
            case 1: criteria.where(cb.and(cb.equal(groot.get("cust_id"),id),cb.equal(groot.get("account_type"),"checking")));
                try {
                    acct = session.createQuery(criteria).getSingleResult();
                    System.out.println("How much would you like to deposit: ");
                    String amount = sc.nextLine();
                    bd = new BigDecimal(amount);
                    checking.doDeposit(bd, acct);
                }
                catch (NoResultException e) {
                    System.out.println("You do not have a checking account");
                    menu.customerMenu(customer);
                }
                break;
            case 2:  criteria.where(cb.and(cb.equal(groot.get("cust_id"),id),cb.equal(groot.get("account_type"),"saving")));
                try {
                    acct = session.createQuery(criteria).getSingleResult();
                    System.out.println("How much would you like to deposit: ");
                    String amount = sc.nextLine();
                    bd = new BigDecimal(amount);
                    saving.doDeposit(bd, acct);
                }
                catch (NoResultException e) {
                    System.out.println("You do not have a savings account");
                    menu.customerMenu(customer);
                }
                break;
            case 3:  criteria.where(cb.and(cb.equal(groot.get("cust_id"),id),cb.equal(groot.get("account_type"),"checking")));
                try {
                    acct = session.createQuery(criteria).getSingleResult();
                    System.out.println("How much would you like to withdraw: ");
                    String amount = sc.nextLine();
                    bd = new BigDecimal(amount);
                    checking.doWithdraw(bd, acct);
                }
                catch (NoResultException e) {
                    System.out.println("You do not have a checking account");
                    menu.customerMenu(customer);
                }
                break;
            case 4:  criteria.where(cb.and(cb.equal(groot.get("cust_id"),id),cb.equal(groot.get("account_type"),"saving")));
                try {
                    acct = session.createQuery(criteria).getSingleResult();
                    System.out.println("How much would you like to withdraw: ");
                    String amount = sc.nextLine();
                    bd = new BigDecimal(amount);
                    saving.doDeposit(bd, acct);
                }
                catch (NoResultException e) {
                    System.out.println("You do not have a savings account");
                    menu.customerMenu(customer);
                }
                break;
            case 5: for (Account a : accountList) {
                System.out.println("You account balance for " + a.getAccount_num() + " is " + a.getAccount_balance());
            }
            break;
            case 6:  criteria.where(cb.and(cb.equal(groot.get("cust_id"),id),cb.equal(groot.get("account_type"),"loan")));
                try {
                    acct = session.createQuery(criteria).getSingleResult();
                    System.out.println("How much would you like to make a payment: ");
                    String amount = sc.nextLine();
                    bd = new BigDecimal(amount);
                    loan.doWithdraw(bd, acct);
                }
                catch (NoResultException e) {
                    System.out.println("You do not have a loan");
                    menu.customerMenu(customer);
                }
                break;
            case 7: b.doEndOfMonth(accountList);
                break;
            case 9: System.out.println("User has logged out ");
                break;
            default: System.out.println("Wrong Entry");
            }        
}

public void adminActionMenu(int option) {
    
    switch(option) {
    case 1: b.createCustomerList();
        break;
    case 2: b.createCustomer();
        break;
    case 3: System.out.println("Enter id for customer statement: ");
    	String customerId = sc.nextLine();
    	Session session = factory.openSession();
    	CriteriaBuilder cb = session.getCriteriaBuilder();
    	CriteriaQuery<Account> criteria = cb.createQuery(Account.class);
    	Root<Account> groot = criteria.from(Account.class);
    	criteria.select(groot);
    	criteria.where(cb.equal(groot.get("cust_id"),Integer.parseInt(customerId)));
    try {
    	List<Account> acct = session.createQuery(criteria).getResultList();
    	b.doEndOfMonth(acct);
    }
    catch (NoResultException e) {
        System.out.println("Customer does not exist");
        menu.adminMenu();
    }
        break;
    case 9:System.out.println("User has logged out"); 
    break; 
    default: System.out.println("Wrong Entry");
    }
}
public void adminMenu() {
	String option;
	do {
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("              ADMIN MENU");
		System.out.println("               "+ "GCU BANK");
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("Pick an option: ");
		System.out.println("-----------------------");
		System.out.println(" 1: : Customer List");
		System.out.println(" 2: : Create new Customer");
		System.out.println(" 3: : Close month");
		System.out.println("------------------------");
		System.out.println(" 9: : Logout");
		option = sc.nextLine();
		menu.adminActionMenu(Integer.parseInt(option));
	} while (Integer.parseInt(option)!= 9);

}

public void startMenu() {
	
	String option;
	int o;
	do {
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println();
		System.out.println("          WELCOME TO GCU BANK");
		System.out.println();
		System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
		System.out.println("Pick an option: ");
		System.out.println("------------------------");
		System.out.println(" 1: : Customer sign in");
		System.out.println(" 2: : Admin sign in");
		System.out.println(" 3: : Exit");
		System.out.println("------------------------");
		option = sc.nextLine();
		o = Integer.parseInt(option);
		switch (o) {
		case 1: 
			b.checkCustomer();
			break; 
		case 2: 
			b.checkAdmin();
			break;
		}
		
	} while (o != 3);
	menu.displayExitScreen();
}
public void displayExitScreen() {

    System.out.println("Goodbye, thank you for banking with GCU Credit Union ");
}
}
