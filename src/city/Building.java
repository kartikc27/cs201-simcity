package city;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.Timer;

import restaurantCM.gui.CMRestaurantBuilding;
import mainGUI.BuildingPanel;
import mainGUI.MainGui;

public abstract class Building extends Rectangle2D.Double {

	public String name;
	public BuildingType type;
	public enum BuildingType {home, apartment, restaurant, bank, market, room};
	protected boolean isOpen = false;
	public PersonAgent manager;
	public CityData cityData;
	
	Timer buildingTimer;
	
	public int buildingNumber;

	BuildingPanel buildingPanel;
	MainGui mainGui;

	//private List<Role> roles = Collections.synchronizedList(new ArrayList<Role>());
	protected Map<PersonAgent, Role> existingRoles = Collections.synchronizedMap(new HashMap<PersonAgent, Role>());

	public BusStopAgent busStop;
	public boolean hasManager=false;
	
	public RGrid closest;
	
	public Building(int xPos, int yPos, int width, int height, MainGui mainGui) {
		super(xPos, yPos, width, height);
		this.mainGui = mainGui;
		buildingPanel = new BuildingPanel(mainGui);
	}

	public Building(int xPos, int yPos, int width, int height, String name, BuildingType type, MainGui mainGui) {
		super(xPos, yPos, width, height);
		this.name = name;
		this.type = type;
		this.mainGui = mainGui;
		buildingPanel = new BuildingPanel(mainGui);
	}

	public void ExitBuilding(PersonAgent p) {
		
	}

	public  void EnterBuilding(PersonAgent p, String roleRequest) {

		/* Each specific building that extends Building.java will have to override this function according to its needs/capabilities.
		 * 
		 * Here is a general outline of how that code should look using the Market as an example:
		 * 
		 * if (market.isOpen()) {
		 * 		if (existingRoles.get(p)) {
		 * 			p.msgAssignRole(existingRoles.get(p));
		 *		 }
		 * 	   else if (roleRequest.equals("customer")) {
		 *     		p.msgAssignRole(new MarketCustomerRole(p, manager));
		 *     }
		 *     else if (roleRequest.equals("employee")) {
		 *     		p.msgAssignRole(new MarketEmployeeRole(p, manager));
		 *     }
		 * }
		 * else {
		 * 		if (p.equals(manager)) {
		 * 			if existingRoles.get(p) {
		 * 				p.msgAssignRole(existingRoles.get(p));
		 *			 }
		 *			else
		 * 				p.msgAssignRole(new MarketManagerRole(p));
		 * 		}
		 * 		else msgAccessDenied()
		 * }
		 * 
		 * 
		 * 
		 * 
		 */
	}
	
	public boolean hasManager() {
		return hasManager;
	}
	public void LeaveBuilding(PersonAgent p) {
		
	}

	// assign relations between new comer and person in charge both ways
	/*protected void GoIntoBuilding(PersonAgent p) {
		p.msgAssignRole(manager.role);
		waitingPeople.add(p);
	}

	protected void ComeIntoBuilding() {
		manager = new Manager();
	}

	protected void LeaveBuilding(PersonAgent p) {
		waitingPeople.remove(p);
	}*/

	//only manager can set the building to Open or Closed

	public boolean isOpen() {
		return isOpen;
	}
	
	public void setOpen(PersonAgent p) {
		if (p.equals(manager)) {
			isOpen = true;
		}
	}

	public void setClosed(PersonAgent p) {
		if (p.equals(manager)) {
			isOpen = false;
		}
	}


	public void setType(BuildingType type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setBuildingNumber(int number) {
		buildingNumber = number;
	}
	
	public void setManager(PersonAgent p) {
		manager = p;
		hasManager = true;
	}
	
	public void setBusStop(BusStopAgent bs, int routeNumber) {
		busStop = bs;
	}

	public void display(Building building, int buildingNumber) {
		buildingPanel.displayBuildingPanel(building, buildingNumber);
		if(building instanceof CMRestaurantBuilding){
		//	((CMRestaurantBuilding) building).test();
		}
	}
	
	public abstract JFrame getBuildingGui();
	
	public void setClosestRGrid(RGrid rg) {
		closest = rg;
	}

	public boolean hasHost() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean openToEmployee() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean hasCashier() {
		// TODO Auto-generated method stub
		return false;
	}
}