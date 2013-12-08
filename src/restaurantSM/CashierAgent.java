package restaurantSM;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import restaurantSM.interfaces.Cashier;
import restaurantSM.interfaces.Market;
import restaurantSM.interfaces.Waiter;

import restaurantSM.utils.*;
import agent.Agent;

public class CashierAgent extends Agent implements Cashier {
	String name;
	public Menu menu = new Menu();
	public List<Bill> pendingBills = new ArrayList<Bill>();
	public List<Bill> paidBills = new ArrayList<Bill>();
	public List<StockBill> marketBills = new ArrayList<StockBill>();
	public double total = 300.00;
	public DecimalFormat df = new DecimalFormat("#.00");
	List<MarketAgent> markets = new ArrayList<MarketAgent>();
	
	public CashierAgent(String n) {
		name = n;
	}
	
	public String getName(){
		return name;
	}
	
	public void addMarket(MarketAgent m) {
		markets.add(m);
	}
	
	public void msgComputeBill(Waiter waiter, MyCustomer myCust) {
		pendingBills.add(new Bill(waiter, myCust));
		stateChanged();
	}
	
	public void msgHeresMyBill(Double money, Bill b){
		b.tender = money;
		paidBills.add(b);
		stateChanged();
	}
	
	public void msgPayForStock(Market m, double stockPrice){
		marketBills.add(new StockBill(m, stockPrice));
		stateChanged();
	}
	
	public boolean pickAndExecuteAnAction() {
		if (!paidBills.isEmpty()){
			CalculateChange(paidBills.get(0));
			paidBills.remove(paidBills.get(0));
			return true;
		}
		
		if (!pendingBills.isEmpty()){
			CalculateBill(pendingBills.get(0));
			pendingBills.remove(pendingBills.get(0));
			return true;
		}
		
		if (!marketBills.isEmpty()){
			PayMarket(marketBills.get(0));
			marketBills.remove(marketBills.get(0));
			return true;
		}
			
		return false;
	}
	
	private void CalculateBill(Bill b) {
		double price = menu.prices.get(b.myCust.getOrder().getChoice());
		b.setTotal(price);
		b.waiter.msgHeresTheBill(b);
	}
	
	private void CalculateChange(Bill b) {
		total += b.total;
		Do("total = " + df.format(total));
		for (MarketAgent m : markets) {
			if (m.tab > 0 && total > 0) {
				double min = min(m.tab, total);
				total -= min;
				m.msgPayDownTab(min);
				Do("Paying down " + min + " on my tab at " + m.getName());
			}
		}
		b.calcChange();
		b.myCust.getCustomer().msgHeresYourChange(b);
	}
	
	private double min(double a, double b) {
		if (a < b){
			return a;
		}
		return b;
	}
	
	private void PayMarket(StockBill b) {
		double tabAmt = 0;
		if (total - b.total >= 0) {
			total -= b.total;
			Do("total = " + df.format(total));
			b.market.msgReceivePayment(b.total, tabAmt);
		}
		else {
			tabAmt = -1 * (total - b.total);
			Do("Unable to pay full amount, adding " + df.format(tabAmt) + " to the cashier's tab at " + b.market.getName());
			total = 0;
			b.market.msgReceivePayment(b.total - tabAmt, tabAmt);
		}
	}
	
}

