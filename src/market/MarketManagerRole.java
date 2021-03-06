package market;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Semaphore;

import market.gui.ManagerGui;
import market.interfaces.MarketCustomer;
import market.interfaces.MarketEmployee;
import market.interfaces.MarketManager;
import restaurantKC.KCCashierRole;
import restaurantKC.KCCookRole;
import restaurantKC.test.mock.EventLog;
import restaurantKC.test.mock.LoggedEvent;
import trace.AlertLog;
import trace.AlertTag;
import bank.BankManagerRole;
import city.CityData;
import city.DeliveryDrone;
import city.PersonAgent;
import city.Role;

public class MarketManagerRole extends Role implements MarketManager{
	public enum ManagerState {nothing, entering, setting, managing, working, leaving};
	public ManagerState state;
	double undepositedMoney;
	BankManagerRole bankManager;
	Inventory inventory = null;
	Market market = null;
	private ManagerGui gui = null;
	private int marketNum;
	public EventLog log = new EventLog();

	public List<MarketCustomer> waitingCustomers = Collections.synchronizedList(new ArrayList<MarketCustomer>());
	public List<MyEmployee> waitingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	public List<MyEmployee> workingEmployees = Collections.synchronizedList(new ArrayList<MyEmployee>());
	private List<MyCookCustomer> waitingCookCustomers = Collections.synchronizedList(new ArrayList<MyCookCustomer>());
	public List<Invoice> invoice = Collections.synchronizedList(new ArrayList<Invoice>());

	private Semaphore atHome = new Semaphore(0, true);
	private Semaphore leaving = new Semaphore(0, true);


	private class MyEmployee {
		public MarketEmployee employee;
		public boolean isBusy;

		public MyEmployee(MarketEmployee employee) {
			this.employee = employee;
			isBusy = false;
		}
	}

	public enum CookCustState {pending, delivering, done};
	public class MyCookCustomer {
		public KCCookRole cook;
		public KCCashierRole cashier;
		public List<CookMarketOrder> orders;
		public CookCustState state;

		public MyCookCustomer(KCCookRole cook, List<CookMarketOrder> orders, KCCashierRole cashier) {
			this.cook = cook;
			this.orders = orders;
			this.cashier = cashier;
			state = CookCustState.pending;
		}
	}

	public enum droneState {pending, delivered};

	public class MyDrone {
		DeliveryDrone d;
		droneState state;

		public MyDrone(DeliveryDrone d) {
			this.d = d;
			state = droneState.pending;
		}
	}

	public List<MyDrone> failedDrones = Collections.synchronizedList(new ArrayList<MyDrone>());


	public MarketManagerRole(PersonAgent person, Inventory inventory, Market market, int num) {
		super(person);
		this.inventory = inventory;
		this.market = market;
		state = ManagerState.nothing;
		marketNum = num;
	}

	/*public MarketManagerRole(TestPerson person, Inventory inventory, Market market) {
		super(person);
		this.inventory = inventory;
		this.market = market;
		state = ManagerState.nothing;
	}*/

	public void msgReportingForWork(MarketEmployee employee) {
		waitingEmployees.add(new MyEmployee(employee));
		stateChanged();
	}

	public void msgNeedToOrder(MarketCustomer cust) {
		waitingCustomers.add(cust);
		stateChanged();
	}

	public void msgHereIsMarketOrder(KCCookRole cook, KCCashierRole cashier, List<CookMarketOrder>ordersToSend) {
		waitingCookCustomers.add(new MyCookCustomer(cook, ordersToSend, cashier));
		stateChanged();
	}

	public void msgLeavingWork(MarketEmployee employee) {	

		for(Iterator<MyEmployee> iter = workingEmployees.iterator(); iter.hasNext(); ) {
			MyEmployee m = iter.next();
			if(employee.equals(employee)) {
				iter.remove();
			}
		}
		workingEmployees.remove(employee);
		stateChanged();
	}

	public void msgHereIsPayment(double money) {
		undepositedMoney += money;
		stateChanged();
	}

	public void msgHereIsMoney(double money, MarketEmployee employee) {
		undepositedMoney += money;
		for (MyEmployee e : workingEmployees){
			if (e.employee.equals(employee)) {
				e.isBusy = false;
			}
		}
		stateChanged();
	}

	public void msgDeliveryFailed(DeliveryDrone d) {
		failedDrones.add(new MyDrone(d));
		System.err.println("Drone failed ");

		stateChanged();
	}



	@Override
	public boolean pickAndExecuteAnAction() {

		if (person.cityData.restaurants.get(2).isOpen()) {
			if (!failedDrones.isEmpty()) {
				for (MyDrone d : failedDrones) {
					System.err.println("here 2");
					if (d.state == droneState.pending) {
						d.d.isPresent = true;
						d.d.xDestination = d.d.origX;
						d.d.yDestination = d.d.origY;
						d.d.command = d.d.command.goToBuilding;
						d.state = droneState.delivered;
					}

				}
			}
		}
		
		if(person.cityData.hour >= market.CLOSINGTIME && market.isOpen())
		{
			AlertLog.getInstance().logInfo(AlertTag.MARKET, this.getName(), "Market is closed");
			AlertLog.getInstance().logMessage(AlertTag.MARKET_MANAGER, this.getName(), "CLOSING THE MARKET");
			market.setClosed(person);
			return true;
		}

		if(person.cityData.hour >= market.CLOSINGTIME && !market.isOpen() && workingEmployees.isEmpty())
		{
			LeaveRestaurant();
			return true;
		}

		if ((!market.isOpenForEmployees) && (state == ManagerState.setting))  {
			market.isOpenForEmployees = true;
			state = ManagerState.managing;
			log.add (new LoggedEvent ("Market open for employees"));
			print ("The market is now open for employees only");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is open for employees only");
			return true;
		}

		if ((!workingEmployees.isEmpty()) && (!market.isOpen()) && (state == ManagerState.managing)) {
			state = ManagerState.working;
			market.setOpen(person);
			log.add (new LoggedEvent ("Market open"));
			print ("The market is now open");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is fully employed");
			AlertLog.getInstance().logInfo(AlertTag.MARKET, market.name, "Market is open now");
			return true;
		}

		if (workingEmployees.isEmpty() && market.isOpen()) {
			market.setClosed(person);
			return true;
		}

		if (state == ManagerState.entering) {
			gui.GoToRoom();
			state = ManagerState.managing;
			try {
				atHome.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return true;
		}

		if (!waitingEmployees.isEmpty()) {
			if (workingEmployees.size() < 10) {
				workingEmployees.add(waitingEmployees.get(0));
				waitingEmployees.get(0).employee.msgGoToDesk(workingEmployees.size()-1);
				waitingEmployees.remove(0);
			}
			else {
				waitingEmployees.get(0).employee.msgLeave();
			}
			return true;
		}

		if (!waitingCustomers.isEmpty()) {
			for (MyEmployee e : workingEmployees) {
				if (!e.isBusy) {
					e.isBusy = true;
					e.employee.msgServiceCustomer(waitingCustomers.get(0));
					waitingCustomers.remove(0);
					return true;
				}
			}
		}
		if (!waitingCookCustomers.isEmpty()) {
			DeliverRestaurantOrder();
			waitingCookCustomers.clear();
			return true;
		}

		for(Iterator<MyCookCustomer> iter = waitingCookCustomers.iterator(); iter.hasNext(); ) {
			MyCookCustomer c = iter.next();
			if(c.state == CookCustState.delivering) {
				iter.remove();
			}
		}

		return false;
	}

	private void DeliverRestaurantOrder() {
		AlertLog.getInstance().logMessage(AlertTag.MARKET_EMPLOYEE, this.getName(), "Fulfilling restaurant's order");
		int amountDue = 0; 
		synchronized (waitingCookCustomers) {
			for (MyCookCustomer c : waitingCookCustomers )	{
				if (c.state == CookCustState.pending) {
					for (CookMarketOrder o : c.orders) {
						if(o.amount <= inventory.inventory.get(o.type).amount) {
							inventory.inventory.get(o.type).amount -= o.amount;
							inventory.update();
							double price = o.amount * inventory.inventory.get(o.type).price;
							Invoice i = new Invoice(o.type, o.amount, price*o.amount);
							invoice.add(i);
							amountDue += price;
						}
					}

					DeliveryDrone d = new DeliveryDrone(invoice, person.cityData, (int)person.cityData.restaurants.get(2).getX()
							, (int)person.cityData.restaurants.get(2).getY(), c.cook, (int)person.cityData.markets.get(marketNum).getX(), 
							(int) (int)person.cityData.markets.get(marketNum).getY(), this);

					c.cashier.msgHereIsMarketBill(amountDue, invoice, this);
					c.state = CookCustState.delivering;

				}
			}
		}
	}


	private void LeaveRestaurant() {
		person.cityData.banks.get(0).depositMoney(person.cityData.markets.get(marketNum), undepositedMoney);
		undepositedMoney = 0;
		System.out.println("Manager leaving");
		AlertLog.getInstance().logMessage(AlertTag.MARKET_MANAGER, this.getName(), "Leaving the market");
		gui.DoLeaveMarket();
		try{
			leaving.acquire();
		}
		catch(Exception e){}
		person.msgFull();
		person.msgDoneWithJob();
		person.exitBuilding();
		doneWithRole();

	}

	public void setGui (ManagerGui gui) {
		this.gui = gui;
		state = ManagerState.entering;
	}

	public void msgEntered() {
		atHome.release();
		state = ManagerState.setting;
		stateChanged();
	}

	public void msgLeft() {
		leaving.release();
		stateChanged();
	}




}
