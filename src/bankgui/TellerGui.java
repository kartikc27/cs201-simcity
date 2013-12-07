package bankgui;
import Gui.*;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import bank.TellerRole;
import bank.utilities.GuiPositions;

public class TellerGui implements GuiPositions, Gui {
	private TellerRole teller;
	private int xPos;
	private int xDestination;
	private int yPos;
	private int yDestination;
	private boolean isPresent = true;

	private boolean atDest = false;


	public TellerGui(TellerRole tellerRole) {
		this.teller = tellerRole;
		xPos = doorx;
		yPos = doory;
		yDestination = teller1y;
		xDestination = tellerx;
	}


	@Override
	public void updatePosition() {
		if(!atDest){
			if (xPos < xDestination)
				xPos++;
			else if (xPos > xDestination)
				xPos--;

			if (yPos < yDestination)
				yPos++;
			else if (yPos > yDestination)
				yPos--;

			if(xPos == xDestination && yPos == yDestination){
				if(!atDest){
					teller.msgGuiIsAtDest();
					atDest = true;
				}

			}

		}

	}



	@Override
	public void draw(Graphics2D g) {
		g.setColor(Color.red);
		g.fillRect(xPos, yPos, 10, 10);
	}


	@Override
	public boolean isPresent() {
		return isPresent;
	}


	@Override
	public void setPresent(boolean b) {
		isPresent = b;
	}


	public void DoLeaveBank() {
		xDestination = doorx;
		yDestination = doory;
		atDest = false;
	}

}
