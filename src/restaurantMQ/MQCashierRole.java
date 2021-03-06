package restaurantMQ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import market.MarketEmployeeRole;
import city.PersonAgent;
import city.Role;
import restaurantMQ.gui.RestaurantPanel;
import restaurantMQ.interfaces.Cashier;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Market;
import restaurantMQ.interfaces.Waiter;
import restaurantMQ.test.mock.EventLog;
import restaurantMQ.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;

public class MQCashierRole extends Role implements Cashier
{
	/*DATA*/
	String name;
	private Map<String, Double> foodMap = new HashMap<String, Double>();
	public Map<Customer, Double> checks = new HashMap<Customer, Double>();
	public List<Payment> payments = Collections.synchronizedList(new ArrayList<Payment>());
	public EventLog log = new EventLog();
	public double money = 10000;
	public RestaurantPanel restPanel;
	
	public class CheckRequest
	{
		Customer customer;
		Waiter waiter;
		String choice;
		
		public CheckRequest(Waiter waiter, Customer customer, String choice)
		{
			this.waiter = waiter;
			this.customer = customer;
			this.choice = choice;
		}
	}
	
	public class Payment
	{
		Customer customer;
		double payment;
		
		Payment(Customer customer, double payment)
		{
			this.customer = customer;
			this.payment = payment;
		}
	}
	
	public List<CheckRequest> checkRequests = Collections.synchronizedList(new ArrayList<CheckRequest>());
	
	private class Bill
	{
		MarketEmployeeRole marketEmployee;
		double bill;
		
		Bill(MarketEmployeeRole marketEmployee, double bill)
		{
			this.marketEmployee = marketEmployee;
			this.bill = bill;
		}
	}
	
	public List<Bill> bills = Collections.synchronizedList(new ArrayList<Bill>());
	/*END OF DATA*/
	
	/*CONSTRUCTORS*/
	public MQCashierRole(PersonAgent person, RestaurantPanel rp)
	{
		super(person);
		restPanel = rp;
		this.name = super.getName();
		foodMap.put("Steak", 15.99);
		foodMap.put("Chicken", 10.99);
		foodMap.put("Salad", 5.99);
		foodMap.put("Pizza", 8.99);
	}
	/*END OF CONSTRUCTOR*/

	/*MESSAGES*/
	public void msgProduceCheck(Waiter waiter, Customer customer, String choice)
	{
		checkRequests.add(new CheckRequest(waiter, customer, choice));
		log.add(new LoggedEvent("Received check request."));
		stateChanged();
	}
	
	public void msgHereIsMoney(Customer customer, double payment)
	{
		payments.add(new Payment(customer, payment));
		log.add(new LoggedEvent("Received payment."));
		stateChanged();
	}
	
	public void msgHereIsBill(MarketEmployeeRole marketEmployee, double bill)
	{
		bills.add(new Bill(marketEmployee, bill));
		log.add(new LoggedEvent("Received bill."));
		stateChanged();
	}
	/*END OF MESSAGES*/

	/*SCHEDULER*/
	public boolean pickAndExecuteAnAction()
	{
		synchronized(payments)
		{
			for(Payment p : payments)
			{
				payments.remove(p);
				processPayment(p);
				return true;
			}
		}
		
		synchronized(checkRequests)
		{
			for(CheckRequest c : checkRequests)
			{
				checkRequests.remove(c);
				processRequest(c);
				return true;
			}
		}
		
		synchronized(bills)
		{
			for(Bill b : bills)
			{
				bills.remove(b);
				payBill(b);
				return true;
			}
		}
		
		if(person.cityData.hour >= restPanel.CLOSINGTIME && !restPanel.isOpen() 
				&& restPanel.justCashier())
		{
			LeaveRestaurant();
			return true;
		}
		
		return false;
	}
	/*END OF SCHEDULER*/
	
	private void LeaveRestaurant() {
		restPanel.cashierLeaving();
		person.msgDoneWithJob();
		person.exitBuilding();
		doneWithRole();
		
	}

	/*ACTIONS*/
	private void processRequest(CheckRequest c)
	{
		double price = foodMap.get(c.choice);
		if(checks.containsKey(c.customer))
			checks.put(c.customer, (checks.get(c.customer) + price));
		else
			checks.put(c.customer, price);
		c.waiter.msgHereIsCheck(c.customer, price);
		System.out.println("Cashier: Giving check to waiter");
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTMQ_CASHIER, this.getName(), "Giving check to waiter");
	}
	
	private void processPayment(Payment p)
	{
		money += p.payment;
		double balance = checks.get(p.customer);
		
		//Customer has enough money, good to go
		if(p.payment == balance)
		{
			checks.remove(p.customer);
			p.customer.msgGoodToGo();
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTMQ_CASHIER, this.getName(), "Received payment of $"+p.payment);
		}
		else
		{
			double difference = round(checks.get(p.customer)- p.payment);
			checks.put(p.customer, difference);
			System.out.println("Cashier: Pay the other $" + difference + " next time!");
			AlertLog.getInstance().logMessage(AlertTag.RESTAURANTMQ_CASHIER, this.getName(), "Pay the other $" + difference + " next time!");
			p.customer.msgNotEnough();
		}
	}
	
	private void payBill(Bill bill)
	{
		//IMPLEMENT EXTRA CREDIT LATER
		double payment;
		if(money < bill.bill)
		{
			payment = money;
		}
		else
		{
			payment = bill.bill;
		}
		money = round(money - payment);
		System.out.println("Cashier: Paying $" + payment + " to " + bill.marketEmployee.getName());
		AlertLog.getInstance().logMessage(AlertTag.RESTAURANTMQ_CASHIER, this.getName(), "Paying $" + payment + " to " + bill.marketEmployee.getName());
		bill.marketEmployee.msgHereIsPayment(payment);
	}
	/*END OF ACTIONS*/
	
	private double round(double d)
	{
		d *= 1000;
		int n = (int)d;
		if(n % 10 < 5)
		{
			n /= 10;
		}
		else
		{
			n = (n/10) + 1;
		}
		return ((double)n)/100;
	}
	public void setMoney(double money)
	{
		this.money = money;
	}
	
	public void startThread() {
		
	}
}
