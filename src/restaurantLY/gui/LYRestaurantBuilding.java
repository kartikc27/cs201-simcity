package restaurantLY.gui;

import javax.swing.JFrame;

import restaurantLY.gui.RestaurantGui;
import trace.AlertLog;
import trace.AlertTag;
import mainGUI.MainGui;
import city.Building;
import city.CityData;
import city.PersonAgent;

public class LYRestaurantBuilding extends Building
{
	public static final int MAXCUSTOMERS = 8;
	
	RestaurantGui restGui;
	RestaurantPanel restPanel;
	
	private boolean printed = false;
	
	public LYRestaurantBuilding(int xPos, int yPos, int width, int height,
			String name, BuildingType type, MainGui mainGui, CityData cd) 
	{
		super(xPos, yPos, width, height, name, type, mainGui);

		restGui = new RestaurantGui(null, this);//assign market later

		restPanel = restGui.getRestaurantPanel();
	}
	
	public void EnterBuilding(PersonAgent person, String roleRequest)
	{
		System.out.println(person.getName() + ": Entering RestaurantLY as " + roleRequest);
		if(roleRequest.equals("Customer"))
		{
			restPanel.addCustomer(person);
		}
		else if(roleRequest.equals("Waiter"))
		{
			restPanel.addWaiter(person);
		}
		else if(roleRequest.equals("Cook"))
		{
			restPanel.addCook(person);
		}
		else if(roleRequest.equals("Cashier"))
		{
			restPanel.addCashier(person);
		}
		else if(roleRequest.equals("Host"))
		{
			restPanel.addHost(person);
			isOpen = true;
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTLY, this.name, "RestaurantLY is open for employees only");
		}
		
	}
	
	public boolean isOpen() {
		if(isOpen && (restPanel.activeCustomers() < MAXCUSTOMERS) && restPanel.fullyStaffed() && !printed) {
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTLY, this.name, "RestaurantLY is fully employed");
			AlertLog.getInstance().logInfo(AlertTag.RESTAURANTLY, this.name, "RestaurantLY is open now");
			printed = true;
		}
		return isOpen && (restPanel.activeCustomers() < MAXCUSTOMERS) && restPanel.fullyStaffed();
	}
	
	public boolean openToEmployee()
	{
		return isOpen && restPanel.hasHost();
	}
	
	public boolean hasHost()
	{
		return restPanel.hasHost();
	}
	
	public boolean hasCashier()
	{
		return restPanel.hasCashier();
	}
	
	public RestaurantGui getRestaurantGui()
	{
		return restGui;
	}

	public void setOpen(Boolean b) {
		isOpen = b;
		
	}

	public JFrame getBuildingGui() {
		return restGui;
	}
}
