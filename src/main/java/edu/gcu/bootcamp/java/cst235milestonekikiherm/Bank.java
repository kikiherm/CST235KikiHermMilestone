package edu.gcu.bootcamp.java.cst235milestonekikiherm;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


public class Bank {
	static SessionFactory factory = new Configuration().configure().buildSessionFactory();
	static private BigDecimal bd; 
	static Scanner sc = new Scanner(System.in);
	static Menu menu = new Menu();
	static Bank b = new Bank();
	public static void main(String[] args) {
		menu.startMenu();
	}
	
	public void checkAdmin() {
        System.out.println("Enter user name: ");
        String userName = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        Employee emp = null;
        Session session = factory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Employee> criteria = cb.createQuery(Employee.class);
        Root<Employee> groot = criteria.from(Employee.class);
        criteria.select(groot);
        criteria.where(cb.and(cb.equal(groot.get("username"),userName),cb.equal(groot.get("password"),EncryptPassword.encrypt(password))));
        try {
        emp = session.createQuery(criteria).getSingleResult();
        menu.adminMenu();
        }
        catch (NoResultException e) {
            System.out.println("Wrong Username and Password ");
            b.checkAdmin();
        }    
        
    }
	
	public void createCustomer() {
		Customer user1 = new Customer();

		System.out.print("Enter first name: ");
		user1.setFirst_name(sc.nextLine());
		System.out.print("Enter last name: ");
		user1.setLast_name(sc.nextLine());
		System.out.print("Pick login name: ");
		user1.setUser_name(sc.nextLine());
		System.out.print("Pick password: ");
		user1.setPassword(EncryptPassword.encrypt(sc.nextLine()));
		
		Session session = factory.openSession();
		session.beginTransaction();
		session.save(user1);
		session.getTransaction().commit();
		
		createAccount(user1.getId());	
		
	}

	public void createAccount(int cust_id) {
		Account account = new Account();
		BankTransaction trans = new BankTransaction(); 
		account.setCust_id(cust_id);
		String userInput = "Y"; 
		do {
			System.out.print("What type of account would you like to create? 1 for Saving, "
					+ "2 for Checking, 3 for Loan: ");
			String option = sc.nextLine();
			int o = Integer.parseInt(option);
			switch(o) {
			case 1: account.setAccount_type("saving");
			break; 
			case 2: account.setAccount_type("checking");
			break; 
			case 3: account.setAccount_type("loan");
			break; 
			default: System.out.println("You have entered an incorrect number... ");
			
			}

		System.out.println("Enter an account number: ");
		String accountNum = sc.nextLine();
		account.setAccount_num(accountNum);
		
		System.out.println("Enter the balance you would like to deposit: ");
		bd = new BigDecimal(sc.nextLine());
			
		account.setAccount_balance(bd);
		trans.setAmount_trans(bd);
		trans.setTrans_type("Initial Deposit");
		trans.setDate(LocalDate.now());
		trans.setAcct_num(account.getAccount_num()); 

		Session session = factory.openSession();
		session.beginTransaction();
		session.save(account);
		session.save(trans);
		session.getTransaction().commit();
		
		System.out.println("Would you like to add an account? Y or N");
		userInput= sc.nextLine().toUpperCase();
		
		}while(userInput.equals("Y"));
		
	}
	public BankTransaction createTransaction(Account account, BigDecimal amount, String option, BigDecimal ballance) {
		BankTransaction trans = new BankTransaction();
		
		trans.setAcct_num(account.getAccount_num());
		trans.setAmount_trans(amount);
		trans.setTrans_type(option);
		trans.setDate(LocalDate.now());
		trans.setBalance(ballance);
		
		return trans; 
	}

	
	
	public void createCustomerList() {

			
			List<Customer> allUsers = null;
			
			try {
				Session session = factory.openSession();
//				allUsers = session.createCriteria(HibernateUsers.class).list();
				
				 CriteriaQuery<Customer> criteriaQuery = 
						 session.getCriteriaBuilder().createQuery(Customer.class);
						         criteriaQuery.from(Customer.class);
				allUsers = session.createQuery(criteriaQuery).getResultList();

				
			}
			catch(Exception e) {
			
			}
			for(Customer h: allUsers) {
				System.out.println(h);
			}
		}
	

	
	
	public void doEndOfMonth(List<Account> acct) {
		
        
        Account checking = acct.stream().filter(x -> "checking".equals(x.getAccount_type())).findAny().orElse(null);
       Account saving = acct.stream().filter(x -> "saving".equals(x.getAccount_type())).findAny().orElse(null);
        Account loan =  acct.stream().filter(x -> "loan".equals(x.getAccount_type())).findAny().orElse(null);
        
        if (checking != null) {
            Session session = factory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BankTransaction> criteria = cb.createQuery(BankTransaction.class);
            Root<BankTransaction> groot = criteria.from(BankTransaction.class);
            criteria.select(groot);
            criteria.where(cb.equal(groot.get("acct_num"), checking.getAccount_num()));
            List<BankTransaction> checkingTransactions = session.createQuery(criteria).getResultList();
            for(BankTransaction trans : checkingTransactions) {
                System.out.println(trans);
            }
        }
        
        if (saving != null) {
            Saving.savingEndOfMonth(saving);
            Session session = factory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BankTransaction> criteria = cb.createQuery(BankTransaction.class);
            Root<BankTransaction> groot = criteria.from(BankTransaction.class);
            criteria.select(groot);
            criteria.where(cb.equal(groot.get("acct_num"), saving.getAccount_num()));
            List<BankTransaction> savingTransactions = session.createQuery(criteria).getResultList();
            for(BankTransaction trans : savingTransactions) {
                System.out.println(trans);
            }
        }
        
        if (loan != null) {
            Session session = factory.openSession();
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<BankTransaction> criteria = cb.createQuery(BankTransaction.class);
            Root<BankTransaction> groot = criteria.from(BankTransaction.class);
            criteria.select(groot);
            criteria.where(cb.equal(groot.get("account_num"), loan.getAccount_num()));
            List<BankTransaction> loanTransactions = session.createQuery(criteria).getResultList();
            if(loanTransactions.size()>1) {
            	System.out.println("You have made a loan payment, you are not charged a late fee");
            	
            }
           
            for(BankTransaction trans : loanTransactions) {
                System.out.println(trans);
            }
        }

		
	}
	
	
	public void checkCustomer() {
        
        System.out.println("Enter user name: ");
        String userName = sc.nextLine();
        System.out.println("Enter password: ");
        String password = sc.nextLine();
        Customer customer = null;
        Session session = factory.openSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = cb.createQuery(Customer.class);
        Root<Customer> groot = criteria.from(Customer.class);
        criteria.select(groot);
        criteria.where(cb.and(cb.equal(groot.get("user_name"),userName),cb.equal(groot.get("password"),EncryptPassword.encrypt(password))));
        try {
        customer = session.createQuery(criteria).getSingleResult();
        
        menu.customerMenu(customer);
        }
        catch (NoResultException e) {
            System.out.println("Wrong username or password");
            b.checkCustomer();
        }    
        
    }
	
	
	
	public void addEmployee() {
        Employee emp = new Employee();
        emp.setEmp_Id(1);
        emp.setF_Name("Bill");
        emp.setL_Name("Palowski");
        emp.setUsername("palowski01");
        emp.setPassword(EncryptPassword.encrypt("Tripp$ki2211"));
        
        Employee emp1 = new Employee();
        emp1.setEmp_Id(2);
        emp1.setF_Name("Eric");
        emp1.setL_Name("Stoll");
        emp1.setUsername("EStoll");
        emp1.setPassword(EncryptPassword.encrypt("Java42"));
        
        Employee emp2 = new Employee();
        emp2.setEmp_Id(3);
        emp2.setF_Name("Christina");
        emp2.setL_Name("Herman");
        emp2.setUsername("KikiHerm");
        emp2.setPassword(EncryptPassword.encrypt("!@#KikiHerm"));
        
        Session session = factory.openSession();
        session.beginTransaction();
        session.save(emp);
        session.save(emp1);
        session.save(emp2);
        session.getTransaction().commit();    
        
    }
	
}
