package restaurantMQ;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Semaphore;

import city.PersonAgent;
import city.Role;
import restaurantMQ.gui.HostGui;
import restaurantMQ.interfaces.Cook;
import restaurantMQ.interfaces.Customer;
import restaurantMQ.interfaces.Host;
import restaurantMQ.interfaces.Waiter;

public class MQHostRole extends Role implements Host
{
	/*DATA*/
	private static int NTABLES = 4;//a global for the number of tables.
	private static int NWAITINGSPOTS = 4; //a global for the number of waiting positions
	private int workingWaiters = 0;
	
	
	
	//Notice that we implement waitingCustomers using ArrayList, but type it
	//with List semantics.
	public List<Customer> waitingCustomers = Collections.synchronizedList(new ArrayList<Customer>());
	public Collection<Table> tables;
	public Collection<WaitingSpot> waitingSpots;
	public List<Waiter> waiters;
	private List<Cook> cooks = new ArrayList<Cook>();
	//note that tables is typed with Collection semantics.
	//Later we will see how it is implemented
	
	private List<Customer> customersToSeat = Collections.synchronizedList(new ArrayList<Customer>());
	
	private List<MyWaiter> myWaiters = Collections.synchronizedList(new ArrayList<MyWaiter>());
	
	enum WaiterState {Working, WantBreak, OnBreak};
	
	private class MyWaiter
	{
		Waiter waiter;
		int numCust = 0;
		WaiterState state;
		
		MyWaiter(Waiter waiter)
		{
			this.waiter = waiter;
		}
	}

	private String name;
	private Semaphore atTable = new Semaphore(0,true);
	private boolean readyForCust = true;

	public HostGui hostGui = null;
	/*END OF DATA*/
	
	/*CONSTRUCTORS*/
	public MQHostRole(PersonAgent person)
	{
		super(person);
		this.name = super.getName();
		// make some tables
		tables = Collections.synchronizedCollection(new ArrayList<Table>(NTABLES));
		for (int ix = 1; ix <= NTABLES; ix++) {
			tables.add(new Table(ix));//how you add to a collections
		}
		
		waitingSpots = Collections.synchronizedCollection(new ArrayList<WaitingSpot>(NWAITINGSPOTS));
		for(int i = 1; i <= NWAITINGSPOTS; ++i)
		{
			waitingSpots.add(new WaitingSpot(i));
		}
	}
	/*END OF CONSTRUCTOR*/

	public void msgFreeSpot(int number)
	{
		synchronized(waitingSpots)
		{
			for(WaitingSpot w : waitingSpots)
			{
				if(w.number == number)
				{
					w.setUnoccupied();
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgLeaving(Customer customer)
	{
		waitingCustomers.remove(customer);
	}

	public void msgBackFromBreak(Waiter waiter)
	{
		synchronized(myWaiters)
		{
			for(MyWaiter w : myWaiters)
			{
				if(w.waiter == waiter && w.state == WaiterState.OnBreak)
				{
					w.state = WaiterState.Working;
					workingWaiters++;
					stateChanged();
					return;
				}
			}
		}
	}
	
	public void msgIWantFood(Customer cust) {
		customersToSeat.add(cust);
		stateChanged();
	}

	public void msgLeavingTable(Customer cust) {
		synchronized(tables)
		{
			for (Table table : tables) {
				if (table.getOccupant() == cust) {
					System.out.println(cust.getName() + " leaving " + table);
					table.setUnoccupied();
					stateChanged();
				}
			}
		}
	}

	public void msgAtTable() {//from animation
		//print("msgAtTable() called");
		atTable.release();// = true;
		stateChanged();
	}
	
	public void msgReadyForCust() //from animation
	{
		readyForCust = true;
		stateChanged();
	}
	
	public void msgTableEmpty(int table)
	{
		synchronized(tables)
		{
			for(Table t : tables)
			{
				if(t.tableNumber == table)
				{
					t.setUnoccupied();
				}
			}
		}
		stateChanged();
	}
	
	public void msgTableEmpty(int table, Waiter waiter)
	{
		synchronized(myWaiters)
		{
			synchronized(tables)
			{
				for(Table t : tables)
				{
					if(t.tableNumber == table)
					{
						t.setUnoccupied();
					}
				}
			}
			
			for(MyWaiter w : myWaiters)
			{
				if(w.waiter.equals(waiter))
				{
					w.numCust--;
				}
			}
		}
		stateChanged();
	}
	
	public void msgIWantBreak(Waiter waiter)
	{
		synchronized(myWaiters)
		{
			for(MyWaiter w : myWaiters)
			{
				if(w.waiter.equals(waiter))
				{
					w.state = WaiterState.WantBreak;
					stateChanged();
					return;
				}
			}
		}
	}

	public boolean pickAndExecuteAnAction() {
		/* Think of this next rule as:
            Does there exist a table and customer,
            so that table is unoccupied and customer is waiting.
            If so seat him at the table.
		 */
		
		synchronized(customersToSeat)
		{
			synchronized(waitingSpots)
			{
				for(WaitingSpot w : waitingSpots)
				{
					if(!w.isOccupied())
					{
						if(!customersToSeat.isEmpty())
						{
							w.setOccupant(customersToSeat.get(0));
							waitingCustomers.add(customersToSeat.get(0));
							customersToSeat.get(0).msgGoToSpot(w.number);
							customersToSeat.remove(0);
							return true;
						}
					}
				}
			}
		}
		
		synchronized(myWaiters)
		{
			synchronized(tables)
			{
				for (Table table : tables) {
					if (!table.isOccupied()) {
						if (!waitingCustomers.isEmpty() && !waiters.isEmpty()) {
							seatCustomer(waitingCustomers.get(0), table);//the action
							return true;//return true to the abstract agent to reinvoke the scheduler.
						}
					}
				}
			}
		}
		
		if(tablesOccupied() && !waitingCustomers.isEmpty())
		{
			TablesFull();
		}
		
		synchronized(myWaiters)
		{
			for (MyWaiter w : myWaiters)
			{
				if(workingWaiters > 1 && w.state == WaiterState.WantBreak)
				{
					SendOnBreak(w);
					return true;
				}
			}
		}

		return false;
		//we have tried all our rules and found
		//nothing to do. So return false to main loop of abstract agent
		//and wait.
	}
	
	private void SendOnBreak(MyWaiter w)
	{
		w.state = WaiterState.OnBreak;
		workingWaiters--;
		System.out.println("Host: " + ((MQWaiterRole)w.waiter).getName() + " is cleared to go on break.");
		w.waiter.msgGoOnBreak();
	}
	
	private void seatCustomer(Customer customer, Table table) {
		table.setOccupant(customer);
		int spotNum = 1;
		for(WaitingSpot w : waitingSpots)
		{
			if(w.getOccupant() == customer)
			{
				spotNum = w.number;
			}
		}
		MyWaiter w;
		for(int i = 0; i < myWaiters.size(); ++i)
		{
			w = myWaiters.get(i);
			if(w.state != WaiterState.OnBreak)
			{
				w.waiter.msgSeatCustomer(customer, spotNum, table.tableNumber); 
				w.numCust++;
				sortWaiters();
				break;
			}
		}
		//always the first because this is a priority queue
		
		
		//DoSeatCustomer(customer, table);
		System.out.println("Host: Seat " + customer.getName() + " at table " + table.tableNumber);
		waitingCustomers.remove(customer);
		/*
		try {
			atTable.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.setOccupant(customer);
		waitingCustomers.remove(customer);
		hostGui.DoLeaveCustomer();
		*/
	}

	// The animation DoXYZ() routines
	private void DoSeatCustomer(Customer customer, Table table) {
		//Notice how we print "customer" directly. It's toString method will do it.
		//Same with "table"
		System.out.println("Seating " + customer.getName() + " at " + table);
		hostGui.DoBringToTable(customer, table.tableNumber); 

	}
	
	private void TablesFull()
	{
		waitingCustomers.get(0).msgTablesFull();
	}
	
	/*GETTERS AND SETTERS*/
	public void setGui(HostGui gui) {
		hostGui = gui;
	}

	public HostGui getGui() {
		return hostGui;
	}
	
	public void setCooks(List<Cook> cooks)
	{
		this.cooks = cooks;
	}
	
	public void setWaiters(List<Waiter> waiters)
	{
		this.waiters = waiters;
	}
	
	public void addWaiter(Waiter waiter)
	{
		waiters.add(waiter);
		myWaiters.add(new MyWaiter(waiter));
		workingWaiters++;
		stateChanged();
	}
	
	public void removeWaiter(Waiter waiter)
	{
		synchronized(myWaiters)
		{
			for(MyWaiter w : myWaiters)
			{
				if(w.waiter == waiter)
				{
					myWaiters.remove(w);
					return;
				}
			}
		}
	}
	
	public void addTable()
	{
		NTABLES++;
		tables.add(new Table(NTABLES));
	}

	public String getMaitreDName() {
		return name;
	}

	public String getName() {
		return name;
	}

	public List getWaitingCustomers() {
		return waitingCustomers;
	}

	public Collection getTables() {
		return tables;
	}
	/*END OF GETTERS AND SETTERS*/
	
	/*UTILITIES*/
	private boolean tablesOccupied()
	{
		for(Table t: tables)
		{
			if(!t.isOccupied())
			{
				return false;
			}
		}
		
		return true;
	}
	
	private void sortWaiters()
	{
		for(int i = 0; i < myWaiters.size() - 1; ++i)
		{
			for(int j = i; j < myWaiters.size() - 1; ++j)
			{
				if(myWaiters.get(j).numCust > myWaiters.get(j+1).numCust)
				{
					MyWaiter temp = myWaiters.get(j);
					myWaiters.set(j, myWaiters.get(j+1));
					myWaiters.set(j+1, temp);
				}
			}
		}
	}
	
	private class WaitingSpot
	{
		Customer occupiedBy = null;
		int number;
		
		WaitingSpot(int number)
		{
			this.number = number;
		}
		
		void setOccupant(Customer cust)
		{
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}
	}
	
	private class Table {
		Customer occupiedBy = null;
		int tableNumber;

		Table(int tableNumber) {
			this.tableNumber = tableNumber;
		}

		void setOccupant(Customer cust) {
			occupiedBy = cust;
		}

		void setUnoccupied() {
			occupiedBy = null;
		}

		Customer getOccupant() {
			return occupiedBy;
		}

		boolean isOccupied() {
			return occupiedBy != null;
		}

		public String toString() {
			return "table " + tableNumber;
		}
	}
}
