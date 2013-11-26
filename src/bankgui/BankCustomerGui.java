package bankgui;

import java.awt.Color;
import java.awt.Graphics2D;

import bank.CustomerRole;
import bank.utilities.GuiPositions;
import Gui.*;

public class BankCustomerGui implements GuiPositions, Gui {

	CustomerRole c;
	private int xPos = doorx, yPos = doory;
	private int xDestination = 50, yDestination = 50;
	public BankCustomerGui(CustomerRole c) {
		this.c = c;
	}
	@Override
	public void updatePosition() {
		if (xPos < xDestination)
			xPos++;
		else if (xPos > xDestination)
			xPos--;

		if (yPos < yDestination)
			yPos++;
		else if (yPos > yDestination)
			yPos--;

		if (xPos == xDestination && yPos == yDestination) {
			
		}
	}

	@Override
	public void draw(Graphics2D g) {
		Color employeeColor = new Color (46, 204, 113);
		g.setColor(employeeColor);
		g.fillRect(xPos, yPos, 30, 30);
	}

	@Override
	public boolean isPresent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setPresent(boolean b) {
		
		
	}

}
